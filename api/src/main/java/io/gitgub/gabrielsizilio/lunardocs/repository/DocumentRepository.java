package io.gitgub.gabrielsizilio.lunardocs.repository;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findDocumentByOwnerId(UUID ownerId);
}
