package fmi.sdl_backend.rest.response.pdf;

import fmi.sdl_backend.rest.response.SubsectionResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadedPdfResponseWithDetails extends UploadedPdfResponse{
    private List<SubsectionResponse> subsections;
}
