package io.gitgub.gabrielsizilio.lunardocs.repository;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.DocumentSigner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentSignedRepository extends JpaRepository<DocumentSigner, UUID> {
    Optional<List<DocumentSigner>> findByDocumentId(UUID documentId);
}
