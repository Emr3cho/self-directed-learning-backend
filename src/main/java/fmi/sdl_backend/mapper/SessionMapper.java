package fmi.sdl_backend.mapper;

import fmi.sdl_backend.presistance.model.Session;
import fmi.sdl_backend.presistance.model.Subsection;
import fmi.sdl_backend.rest.request.session.SessionCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "document.id", source = "request.documentId")
    Session toSession(SessionCreateRequest request, List<Subsection> subsections);
}
