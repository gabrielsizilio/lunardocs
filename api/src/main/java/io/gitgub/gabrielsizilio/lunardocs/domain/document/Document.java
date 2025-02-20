package io.gitgub.gabrielsizilio.lunardocs.domain.document;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "documents")
@Entity(name = "documents")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private String name;
    private String description;
    private StatusDocument status;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DocumentSigner> signers;

    public Document() {
    }

    public Document(UUID id, User owner, String name, String description,StatusDocument status) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Document(User owner, String name, String description,StatusDocument status) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public List<DocumentSigner> getSigners() {
        return signers;
    }

    public void setSigners(List<DocumentSigner> signers) {
        this.signers = signers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusDocument getStatus() {
        return status;
    }

    public void setStatus(StatusDocument status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
