package fmi.sdl_backend.rest.endpoint;

import fmi.sdl_backend.rest.response.cycle.CreateCycleResponse;
import fmi.sdl_backend.rest.response.QuestionToAsk;
import fmi.sdl_backend.service.CycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Cycle Operations", description = "Operations related to interview cycles")
@RequiredArgsConstructor
@RequestMapping("/api/v1/cycles")
@RestController
public class CycleController {

    private final CycleService cycleService;

    @Operation(
            summary = "Create a new cycle",
            description = "Creates a new interview cycle within a session. The cycle will be associated with the specified session and will have an order number assigned automatically based on existing cycles."
    )
    @PostMapping("/{session_id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cycle created successfully",
                    content = @Content(schema = @Schema(implementation = CreateCycleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters or validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<CreateCycleResponse> createCycle(@PathVariable("session_id") UUID sessionId) {
        CreateCycleResponse response = cycleService.createCycleBySessionId(sessionId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get next cycle question",
            description = "Retrieves the next question to ask in the specified cycle"
    )
    @GetMapping("/next/{cycle_id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Next question retrieved successfully",
                    content = @Content(schema = @Schema(implementation = QuestionToAsk.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid interview ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Interview not found or no more questions available",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions to access cycle",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<QuestionToAsk> getNextInterviewQuestion(
            @PathVariable("cycle_id") UUID cycleId) {
        QuestionToAsk nextQuestion = cycleService.getNextQuestionForCycle(cycleId);
        return ResponseEntity.ok(nextQuestion);
    }
}