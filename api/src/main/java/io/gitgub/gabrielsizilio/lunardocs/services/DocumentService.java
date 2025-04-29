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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final FileUltils fileUltils;
    private final UserService userService;
    private final DocumentSignedRepository documentSignedRepository;
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository, FileUltils fileUltils, UserService userService, DocumentSignedRepository documentSignedRepository) {
        this.documentRepository = documentRepository;
        this.fileUltils = fileUltils;
        this.userService = userService;
        this.documentSignedRepository = documentSignedRepository;
    }

//    CREATE
    @Transactional
    public UUID create(DocumentDTO data) throws IOException {
        try {

            Credential owner = (Credential) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            MultipartFile file = data.file();

            if(file.isEmpty()) {
                throw new IllegalArgumentException("File is empty or null");
            }

            String fileName =  data.documentRequestDTO().name();

            if(fileName == null || fileName.isEmpty()) {
                fileName = file.getOriginalFilename();
            }

            Document document = new Document(owner.getUser(), fileName,
                    data.documentRequestDTO().description(), StatusDocument.valueOf(data.documentRequestDTO().statusDocument()));

            document = documentRepository.save(document);
            System.out.println(">>> " + document.getId());

            List<UUID> signers = new ArrayList<>();
            signers.add(owner.getUser().getId());
            addSigner(document.getId(), signers);

            fileUltils.uploadDocument(file, document.getId());

            return document.getId();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating document: " + e.getMessage(), e);
        }
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

        if(fileUltils.deleteFile(documentId)){
            documentRepository.deleteById(documentId);
            return true;
        } else {
            throw new RuntimeException("Could not delete document");
        }
    }

    public FileSystemResource downloadDocument(UUID documentId, String version) throws IOException {
        Path filePath = fileUltils.getDocumentPath(documentId, Integer.parseInt(version));
        if(filePath != null) {
            File file = filePath.toFile();

            FileSystemResource fileSystemResource = new FileSystemResource(file);
            return fileSystemResource;
        } else {
            throw new RuntimeException("File not found");
        }
    }

    @Transactional
    public List<DocumentSignerDTO> getSignerByDocumentId(UUID documentId) {
        List<DocumentSigner> signers = documentSignedRepository.findByDocumentId(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with id " + documentId));

            return signers.stream().map(this::convertToSignerDTO).collect(Collectors.toList());
    }

    @Transactional
    public DocumentResponseDTO addSigner(UUID documentId, List<UUID> usersId) {
        try {
            Document document = findDocumentById(documentId);

            for(UUID signerId : usersId) {
                User signer = userService.findUserById(signerId);

                Optional<DocumentSigner> documentSignerOptional = documentSignedRepository.findByDocumentIdAndSignerId(documentId, signerId);

                if(documentSignerOptional.isPresent()) {
                    throw new IllegalArgumentException("User is already a signer this document");
                }

                DocumentSigner documentSigner = new DocumentSigner();
                documentSigner.setDocument(document);
                documentSigner.setSigner(signer);
                documentSigner.setAssignedAt(LocalDateTime.now());

                documentSignedRepository.save(documentSigner);
            }
            return convertToResponseDTO(document);

        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error while adding signer: " + e.getMessage());
        }
    }

    public DocumentResponseDTO removeSigner(UUID documentId, UUID signerId) {
        try {
            Optional<DocumentSigner> documentSignerOptional = documentSignedRepository.findByDocumentIdAndSignerId(documentId, signerId);

            if(documentSignerOptional.isEmpty()) {
                throw new IllegalArgumentException("User not a signer of this document ");
            }

            documentSignedRepository.delete(documentSignerOptional.get());

            return convertToResponseDTO(documentSignerOptional.get().getDocument());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error while removing signer: " + e.getMessage());
        }
    }

    public void singDocument(UUID documentId) {

    }

    private DocumentResponseDTO convertToResponseDTO(Document document) {
        User owner = document.getOwner();
        UserResponseDTO ownerDto = new UserResponseDTO(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail(), owner.getCpf(),owner.getRole().toString());
        List<UUID> signers = new ArrayList<>();
        for(DocumentSigner documentSinger : document.getSigners()) {
            signers.add(documentSinger.getSigner().getId());
        }

        return new DocumentResponseDTO(document.getId(), ownerDto, document.getName(), document.getDescription(), document.getStatus().toString(), signers);
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
