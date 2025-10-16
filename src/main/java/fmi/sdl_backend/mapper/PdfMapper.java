package fmi.sdl_backend.mapper;

import fmi.sdl_backend.presistance.model.Document;
import fmi.sdl_backend.rest.response.pdf.UploadedPdfResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PdfMapper {

    @Mapping(target = "filename", source = "document.fileName")
    @Mapping(target = "secondaryFilename", source = "document.secondaryFileName")
    @Named("toUploadedPdfResponse")
    UploadedPdfResponse toUploadedPdfResponse(Document document);

    @IterableMapping(qualifiedByName = "toUploadedPdfResponse")
    List<UploadedPdfResponse> toUploadedPdfResponseList(List<Document> documents);

}
