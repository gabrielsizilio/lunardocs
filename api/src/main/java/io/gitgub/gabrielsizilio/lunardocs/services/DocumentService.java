package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.Document;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.StatusDocument;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.DocumentDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.DocumentRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.DocumentResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.repository.DocumentRepository;
import io.gitgub.gabrielsizilio.lunardocs.ultils.FileUltils;
import io.gitgub.gabrielsizilio.lunardocs.ultils.SecurityUtils;
import io.gitgub.gabrielsizilio.lunardocs.ultils.UUIDUtils;
import jakarta.transaction.Transactional;
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
    DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository, UserService userService, FileUltils fileUltils) {
        this.documentRepository = documentRepository;
        this.fileUltils = fileUltils;
    }

//    CREATE
    @Transactional
    public UUID create(DocumentDTO data) throws IOException {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        Document document = new Document(fileId, owner, fileName,
                data.documentRequestDTO().description(), fileNameId, StatusDocument.valueOf(data.documentRequestDTO().statusDocument()));

        documentRepository.save(document);
        return document.getId();
    }

//    READ
    public List<DocumentResponseDTO> findAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        return documents.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public List<DocumentResponseDTO> findMyDocuments() {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Document> myDocuments = documentRepository.findDocumentByOwnerId(owner.getId());
        return myDocuments.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
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

    private DocumentResponseDTO convertToResponseDTO(Document document) {
        User owner = document.getOwner();
        UserResponseDTO ownerDto = new UserResponseDTO(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail() ,owner.getRole().toString());
        return new DocumentResponseDTO(document.getId(), ownerDto, document.getName(), document.getDescription(), document.getStatus().toString());
    }
}
