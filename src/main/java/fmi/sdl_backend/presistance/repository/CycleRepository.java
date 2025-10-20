package fmi.sdl_backend.presistance.repository;

import fmi.sdl_backend.presistance.model.Cycle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CycleRepository extends CrudRepository<Cycle, UUID> {
}
