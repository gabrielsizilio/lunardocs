package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.Document;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.StatusDocument;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.DocumentDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.repository.DocumentRepository;
import io.gitgub.gabrielsizilio.lunardocs.ultils.FileUltils;
import io.gitgub.gabrielsizilio.lunardocs.ultils.UUIDUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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

        String fileNameId = FileUltils.generateFileName(fileName, fileId);

        fileUltils.uploadDocument(file, fileNameId);

        Document document = new Document(fileId, owner, fileName,
                data.documentRequestDTO().description(), fileNameId, StatusDocument.valueOf(data.documentRequestDTO().statusDocument()));

        documentRepository.save(document);
        return document.getId();
    }
//    READ
//    UPDATE
//    DELETE
}
