package io.gitgub.gabrielsizilio.lunardocs.repository;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
