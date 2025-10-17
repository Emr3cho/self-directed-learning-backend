package fmi.sdl_backend.mapper;

import fmi.sdl_backend.presistance.model.Subsection;
import fmi.sdl_backend.rest.response.SubsectionResponse;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubsectionMapper {

    @Named("subsectionToSubsectionResponse")
    SubsectionResponse subsectionToSubsectionResponse(Subsection subsection);

    @IterableMapping(qualifiedByName = "subsectionToSubsectionResponse")
    @Named("subsectionsToSubsectionResponseList")
    List<SubsectionResponse> subsectionsToSubsectionResponseList(List<Subsection> subsections);


}
