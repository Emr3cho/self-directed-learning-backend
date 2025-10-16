package fmi.sdl_backend.rest.response.pdf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadedPdfResponse {
    private UUID id;
    private String filename;
    private String secondaryFilename;
    private OffsetDateTime createdAt;
}
