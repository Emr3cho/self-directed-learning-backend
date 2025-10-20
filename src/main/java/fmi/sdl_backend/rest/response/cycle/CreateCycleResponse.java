package fmi.sdl_backend.rest.response.cycle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCycleResponse {
    private UUID cycleId;
    private String cycleName;
}
