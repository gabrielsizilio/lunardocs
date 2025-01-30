package io.gitgub.gabrielsizilio.lunardocs.domain.document;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "document_signer")
@Table(name = "document_signer")
public class DocumentSigner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signer_id")
    private User signer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    private LocalDateTime assignedAt;
    private LocalDateTime signedAt;
    private String documentHash;
}
