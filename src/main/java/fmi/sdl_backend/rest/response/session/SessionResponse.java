package fmi.sdl_backend.rest.response.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {
    private UUID sessionId;
    private String name;
}
