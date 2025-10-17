package fmi.sdl_backend.service;


import fmi.sdl_backend.presistance.model.Document;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponse;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponseWithConcatenatedContent;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponseWithDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PdfService {
    UploadedPdfResponse savePdf(MultipartFile file, String secondaryFileName);

    UploadedPdfResponseWithDetails getDocumentDetails(UUID documentId);
    List<UploadedPdfResponse> getAllDocuments();
    void deleteDocument(UUID documentId);

    Document findDocumentByIdOrThrow(UUID documentId);
}
