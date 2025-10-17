package fmi.sdl_backend.presistance.repository;

import fmi.sdl_backend.presistance.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PdfRepository extends SoftDeletableRepository<Document, UUID> {
    boolean existsBySecondaryFileName(String secondaryFileName);

    @Query("""
            SELECT DISTINCT d
            FROM Document d
            LEFT JOIN FETCH d.subsections s
            WHERE d.id = :documentId
            AND d.deleted = false
            AND (s IS NULL OR (s.level = 0 AND s.parent IS NULL))
            ORDER BY s.position
            """)
    Optional<Document> findDocumentWithTopLevelSubsections(@Param("documentId") UUID documentId);
}
