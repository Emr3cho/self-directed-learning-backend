package fmi.sdl_backend.mapper;

import fmi.sdl_backend.presistance.model.Document;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponse;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponseWithDetails;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubsectionMapper.class})
public interface PdfMapper {

    @Mapping(target = "filename", source = "document.fileName")
    @Mapping(target = "secondaryFilename", source = "document.secondaryFileName")
    @Named("toUploadedPdfResponse")
    UploadedPdfResponse toUploadedPdfResponse(Document document);

    @InheritConfiguration
    @Mapping(target = "subsections", source = "subsections", qualifiedByName = "subsectionsToSubsectionResponseList")
    UploadedPdfResponseWithDetails toUploadedPdfResponseWithDetails(Document document);

    @IterableMapping(qualifiedByName = "toUploadedPdfResponse")
    List<UploadedPdfResponse> toUploadedPdfResponseList(List<Document> documents);

}
