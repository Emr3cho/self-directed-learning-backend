package fmi.sdl_backend.rest.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubsectionResponse {
    private UUID id;
    private String title;
}
