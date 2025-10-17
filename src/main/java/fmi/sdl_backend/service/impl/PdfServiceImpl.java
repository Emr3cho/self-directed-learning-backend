package fmi.sdl_backend.service.impl;

import fmi.sdl_backend.exception.DuplicateException;
import fmi.sdl_backend.exception.ResourceNotFoundException;
import fmi.sdl_backend.mapper.PdfMapper;
import fmi.sdl_backend.presistance.model.Document;
import fmi.sdl_backend.presistance.model.Subsection;
import fmi.sdl_backend.presistance.repository.PdfRepository;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponse;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponseWithConcatenatedContent;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponseWithDetails;
import fmi.sdl_backend.service.PdfService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfServiceImpl implements PdfService {

    private final RestClient documentParserRestClient;
    private final PdfRepository pdfRepository;
    private final PdfMapper pdfMapper;
    
    private static void traverseSubsectionsAndRemove0Bytes(List<Subsection> subsections) {
    	if (subsections==null)
    		return;

    	for (Subsection s:subsections) {
        	String cleanText = s.getContent().replace("\u0000", "");
        	s.setContent(cleanText);
        	traverseSubsectionsAndRemove0Bytes(s.getChildren());
    	}
    }

    @Override
    public UploadedPdfResponse savePdf(MultipartFile file, String secondaryFileName) {
        log.info("Starting PDF upload process for file: {} with secondary name: {}",
                file.getOriginalFilename(), secondaryFileName);

        validateUniqueSecondaryFileName(secondaryFileName);

        try {
            validateUploadedFile(file);
            Document documentWithSubsections = callDocumentParserService(file);
            
            // clean up the texts from 0x00 bytes as they are a problem for inserting in the database
            traverseSubsectionsAndRemove0Bytes(documentWithSubsections.getSubsections());

            documentWithSubsections.setSecondaryFileName(secondaryFileName);
            documentWithSubsections = pdfRepository.save(documentWithSubsections);

            this.logDocumentSaveSuccess(documentWithSubsections);
            return pdfMapper.toUploadedPdfResponse(this.findDocumentByIdOrThrow(documentWithSubsections.getId()));

        } catch (Exception error) {
            log.error("PDF upload failed for file: {} - {}", file.getOriginalFilename(), error.getMessage(), error);
            throw handleProcessingError(error);
        }
    }

    @Override
    public UploadedPdfResponseWithDetails getDocumentDetails(UUID documentId) {
        Optional<Document> document = pdfRepository.findDocumentWithTopLevelSubsections(documentId);
        return pdfMapper.toUploadedPdfResponseWithDetails(document.get());
    }

    @Override
    public List<UploadedPdfResponse> getAllDocuments() {
        log.debug("Retrieving all documents");

        List<Document> documents = pdfRepository.findAllActive();
        return pdfMapper.toUploadedPdfResponseList(documents);
    }

    @Override
    @Transactional
    public void deleteDocument(UUID documentId) {
        log.info("Deleting document with ID: {}", documentId);

        Document document = findDocumentByIdOrThrow(documentId);
        pdfRepository.softDeleteById(document.getId());

        log.info("Document deleted successfully: {}", documentId);
    }

    private void validateUniqueSecondaryFileName(String secondaryFileName) {
        if (pdfRepository.existsBySecondaryFileName(secondaryFileName)) {
            String message = "Document with name '" + secondaryFileName + "' already exists";
            log.warn("Duplicate secondary filename detected: {}", secondaryFileName);
            throw new DuplicateException(message);
        }
    }

    private void validateUploadedFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("File must have a valid filename");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            throw new IllegalArgumentException("File must be a PDF document");
        }

        log.debug("File validation passed for: {}", filename);
    }

    private Document callDocumentParserService(MultipartFile file) {
        log.debug("Sending file to document parser service: {}", file.getOriginalFilename());

        try {
            MultiValueMap<String, Object> requestBody = createMultipartRequestBody(file);

            Document response = documentParserRestClient
                    .post()
                    .uri("/parse")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(requestBody)
                    .retrieve()
                    .body(Document.class);

            assignDocumentAndParent(response, response.getSubsections(), null);
            String responseFilename = response != null ? response.getFileName() : "unknown";
            log.info("Document parsing completed successfully: {}", responseFilename);

            return response;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes from uploaded file", e);
        } catch (RestClientResponseException e) {
            String errorMessage = String.format("Document parser service error [%d]: %s",
                    e.getStatusCode().value(), e.getResponseBodyAsString());
            throw new RuntimeException(errorMessage, e);
        } catch (RestClientException e) {
            throw new RuntimeException("Document parser service request failed: " + e.getMessage(), e);
        }
    }

    private MultiValueMap<String, Object> createMultipartRequestBody(MultipartFile file) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        body.add("file", fileResource);
        return body;
    }

    private void collectAllSubsectionContent(List<Subsection> subsections, StringBuilder contentBuilder) {
        if (subsections == null || contentBuilder == null) return;

        for (Subsection subsection : subsections) {
            if (subsection.getContent() != null) contentBuilder.append(subsection.getContent());

            //DFS
            collectAllSubsectionContent(subsection.getChildren(), contentBuilder);
        }
    }

    private Subsection findSubsectionByTitleInDocument(List<Subsection> subsections, String title) {
        if (subsections == null || title == null) return null;

        for (Subsection subsection : subsections) {
            if (title.equalsIgnoreCase(subsection.getTitle())) return subsection;

            Subsection found = findSubsectionByTitleInDocument(subsection.getChildren(), title);
            if (found != null) return found;
        }

        return null;
    }

    private List<Subsection> buildSubsectionsHierarchy(List<Subsection> subsections) {
        if (subsections == null || subsections.isEmpty()) {
            return new ArrayList<>();
        }

        Map<UUID, List<Subsection>> childrenMap = new HashMap<>();
        List<Subsection> roots = new ArrayList<>();

        for (Subsection s : subsections) {
            if (s.getParent() == null) {
                roots.add(s);
            } else childrenMap.computeIfAbsent(s.getParent().getId(), k -> new ArrayList<>()).add(s);
        }

        roots.sort(Comparator.comparingInt(Subsection::getPosition));
        childrenMap.values().forEach(children ->
                children.sort(Comparator.comparingInt(Subsection::getPosition))
        );

        for (Subsection root : roots) {
            buildSubsectionsHierarchyRecursive(root, childrenMap);
        }

        return roots;
    }

    private void buildSubsectionsHierarchyRecursive(Subsection current, Map<UUID, List<Subsection>> childrenMap) {
        List<Subsection> children = childrenMap.get(current.getId());

        if (children != null && !children.isEmpty()) {
            current.setChildren(new ArrayList<>(children));

            for (Subsection child : children) {
                buildSubsectionsHierarchyRecursive(child, childrenMap);
            }
        }
    }

    private void assignDocumentAndParent(Document document, List<Subsection> subsections, Subsection parent) {
        if (subsections == null || subsections.isEmpty()) {
            return;
        }

        for (Subsection subsection : subsections) {
            subsection.setDocument(document);
            subsection.setParent(parent);

            if (subsection.getChildren() != null && !subsection.getChildren().isEmpty()) {
                assignDocumentAndParent(document, subsection.getChildren(), subsection);
            }
        }
    }

    public Document findDocumentByIdOrThrow(UUID documentId) {
        Document document = pdfRepository.findActiveById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("PDF document not found with ID: %s", documentId)
                ));
        document.setSubsections(buildSubsectionsHierarchy(document.getSubsections()));
        return document;
    }

    private RuntimeException handleProcessingError(Throwable error) {
        String baseErrorMessage = "Failed to process PDF document";

        if (error instanceof DuplicateException) {
            return (DuplicateException) error;
        } else if (error instanceof IllegalArgumentException e) {
            return new IllegalArgumentException(e.getMessage(), e);
        } else if (error instanceof RestClientResponseException e) {
            return new RuntimeException(
                    String.format("%s - Service error [%d]: %s",
                            baseErrorMessage, e.getStatusCode().value(), e.getResponseBodyAsString()), e);
        } else if (error instanceof RestClientException e) {
            return new RuntimeException(
                    baseErrorMessage + " - Request failed: " + e.getMessage(), e);
        } else {
            // Handles both null and default cases
            return new RuntimeException(baseErrorMessage, error);
        }
    }

    private void logDocumentSaveSuccess(Document document) {
        int subsectionCount = document.getSubsections() != null ? document.getSubsections().size() : 0;

        log.info("Document saved successfully:");
        log.info("  - Document ID: {}", document.getId());
        log.info("  - Original filename: {}", document.getFileName());
        log.info("  - Secondary filename: {}", document.getSecondaryFileName());
        log.info("  - Total subsections: {}", subsectionCount);
    }
}