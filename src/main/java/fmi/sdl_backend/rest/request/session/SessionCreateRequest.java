package fmi.sdl_backend.rest.request.session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new session")
public class SessionCreateRequest {
    private String name;
    private String description;
    private UUID documentId;
    private List<UUID> subsectionIds;
}