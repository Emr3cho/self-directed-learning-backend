package fmi.sdl_backend.mapper;

import fmi.sdl_backend.presistance.model.Session;
import fmi.sdl_backend.rest.request.session.SessionCreateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    Session toSession(SessionCreateRequest request, subsections);
}
