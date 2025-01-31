package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.domain.credential.Credential;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.Document;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.DocumentSigner;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.StatusDocument;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.*;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.repository.DocumentRepository;
import io.gitgub.gabrielsizilio.lunardocs.repository.DocumentSignedRepository;
import io.gitgub.gabrielsizilio.lunardocs.ultils.FileUltils;
import io.gitgub.gabrielsizilio.lunardocs.ultils.SecurityUtils;
import io.gitgub.gabrielsizilio.lunardocs.ultils.UUIDUtils;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final FileUltils fileUltils;
    private final UserService userService;
    private final DocumentSignedRepository documentSignedRepository;
    DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository, FileUltils fileUltils, UserService userService, DocumentSignedRepository documentSignedRepository) {
        this.documentRepository = documentRepository;
        this.fileUltils = fileUltils;
        this.userService = userService;
        this.documentSignedRepository = documentSignedRepository;
    }

//    CREATE
    @Transactional
    public UUID create(DocumentDTO data) throws IOException {
        Credential owner = (Credential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        MultipartFile file = data.file();

        if(file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        UUID fileId = UUIDUtils.generateUUIDRandom();
        String fileName =  data.documentRequestDTO().name();

        if(fileName == null || fileName.isEmpty()) {
            fileName = file.getOriginalFilename();
        }

        String fileNameId = FileUltils.generateFileName(fileId, fileName);

        fileUltils.uploadDocument(file, fileNameId);

        Document document = new Document(fileId, owner.getUser(), fileName,
                data.documentRequestDTO().description(), fileNameId, StatusDocument.valueOf(data.documentRequestDTO().statusDocument()));

        documentRepository.save(document);
        return document.getId();
    }

//    READ
    @Transactional
    public List<DocumentResponseDTO> findAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        return documents.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<DocumentResponseDTO> findMyDocuments() {
        Credential owner = (Credential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Document> myDocuments = documentRepository.findDocumentByOwnerId(owner.getUser().getId());
        return myDocuments.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public Document findDocumentById(UUID documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id: " + documentId));
    }

//    UPDATE
    @Transactional
    public DocumentResponseDTO update(UUID documentId, DocumentRequestDTO data) throws IOException {
        UUID userLogged = SecurityUtils.getUserId();
        Optional<Document> documentOptional = documentRepository.findDocumentByIdAndOwnerId(documentId, userLogged);

        if(documentOptional.isEmpty()) {
            throw new IllegalArgumentException("Document not found");
        }

        Document document = documentOptional.get();
        document.setName(data.name());
        document.setDescription(data.description());
        document.setStatus(StatusDocument.valueOf(data.statusDocument()));

        fileUltils.updateFileName(document.getName(), document.getId());
        documentRepository.save(document);

        return convertToResponseDTO(document);
    }

//    DELETE
    @Transactional
    public Boolean deleteDocument(UUID documentId) {
        UUID userLogged = SecurityUtils.getUserId();
        Optional<Document> documentOptional = documentRepository.findDocumentByIdAndOwnerId(documentId, userLogged);

        if(documentOptional.isEmpty()) {
            throw new IllegalArgumentException("Document not found with id " + documentId);
        }

        documentRepository.delete(documentOptional.get());
        return true;
    }

    @Transactional
    public List<DocumentSignerDTO> getSignerByDocumentId(UUID documentId) {
        List<DocumentSigner> signers = documentSignedRepository.findByDocumentId(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id " + documentId));

            return signers.stream().map(this::convertToSignerDTO).collect(Collectors.toList());
    }

    @Transactional
    public UUID addSigner(UUID documentId, UUID userId) {
        try {
            Document document = findDocumentById(documentId);
            User signer = userService.findUserById(userId);

            Optional<DocumentSigner> documentSignerOptional = documentSignedRepository.findByDocumentIdAndSignerId(documentId, userId);

            if(documentSignerOptional.isPresent()) {
                throw new IllegalArgumentException("User is already a signer this document");
            }

            DocumentSigner documentSigner = new DocumentSigner();
            documentSigner.setDocument(document);
            documentSigner.setSigner(signer);

            documentSignedRepository.save(documentSigner);
            return documentSigner.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while adding signer", e);
        }
    }

    public void singDocument(UUID documentId) {

    }

    private DocumentResponseDTO convertToResponseDTO(Document document) {
        User owner = document.getOwner();
        UserResponseDTO ownerDto = new UserResponseDTO(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail(), owner.getCpf(),owner.getRole().toString());
        return new DocumentResponseDTO(document.getId(), ownerDto, document.getName(), document.getDescription(), document.getStatus().toString(), document.getCreatedAt(), document.getUpdatedAt());
    }

    private DocumentSignerDTO convertToSignerDTO(DocumentSigner documentSigner) {
        return new DocumentSignerDTO(documentSigner.getId(),
                new UserDTO(documentSigner.getSigner().getId(),
                        documentSigner.getSigner().getFirstName(),
                        documentSigner.getSigner().getLastName(),
                        documentSigner.getSigner().getEmail()),
                documentSigner.getAssignedAt(),
                documentSigner.getSignedAt(),
                documentSigner.getDocumentHash());
    }
}
