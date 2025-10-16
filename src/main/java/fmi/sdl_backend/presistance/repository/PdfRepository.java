package fmi.sdl_backend.presistance.repository;

import fmi.sdl_backend.presistance.model.Document;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PdfRepository extends SoftDeletableRepository<Document, UUID> {
    boolean existsBySecondaryFileName(String secondaryFileName);
}
