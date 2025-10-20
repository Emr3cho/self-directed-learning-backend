package fmi.sdl_backend.rest.endpoint;

import fmi.sdl_backend.presistance.model.enums.Status;
import fmi.sdl_backend.rest.request.session.SessionCreateRequest;
import fmi.sdl_backend.rest.response.session.SessionResponse;
import fmi.sdl_backend.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Session Operations", description = "Operations related to interview sessions")
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessions")
@RestController
public class SessionController {

    private final SessionService sessionService;

    @Operation(
            summary = "Create a new session",
            description = "Creates a new interview session based on a specific document. The session will be associated with the authenticated user. Optionally, specific subsections can be selected; if not provided, all subsections from the document will be available for the session."
    )
    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Session created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters or validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Document or subsection not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SessionResponse> createSession(
            @Parameter(description = "Session creation request containing name, document ID, and optional subsection IDs", required = true)
            @Valid @RequestBody SessionCreateRequest request
    ) {
        SessionResponse sessionResponse = sessionService.createSession(request);
        return new ResponseEntity<>(sessionResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get session details",
            description = "Retrieves detailed information about a specific session including its status, cycles, and questions."
    )
    @GetMapping("/{sessionId}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Session details retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid session ID format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> getSessionDetails(
            @Parameter(
                    description = "Unique identifier of the session to retrieve",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable @NotNull UUID sessionId
    ) {
        // TODO: Implement session details retrieval
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Update session status",
            description = "Updates the status of a session (ACTIVE/INACTIVE)."
    )
    @PatchMapping("/{sessionId}/status")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Session status updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid session ID or status",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> updateSessionStatus(
            @PathVariable @NotNull UUID sessionId,
            @RequestParam @NotNull Status status
    ) {
        // TODO: Implement session status update
        return ResponseEntity.ok().build();
    }
}
