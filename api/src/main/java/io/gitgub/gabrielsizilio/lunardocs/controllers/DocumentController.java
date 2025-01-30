package io.gitgub.gabrielsizilio.lunardocs.controllers;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.*;
import io.gitgub.gabrielsizilio.lunardocs.services.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                                 @RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "description", required = false) String description,
                                                 @RequestParam("statusDocument") String status) throws IOException {

        if(file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        DocumentDTO documentDTO = new DocumentDTO(file, new DocumentRequestDTO(name, description, status));
        documentService.create(documentDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("findAll")
    public ResponseEntity<List<DocumentResponseDTO>> findAllDocuments() {
        List<DocumentResponseDTO> documents = documentService.findAllDocuments();

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("myDocuments")
    public ResponseEntity<List<DocumentResponseDTO>> findMyDocuments() {
        List<DocumentResponseDTO> documents = documentService.findMyDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(@PathVariable UUID id, @RequestBody DocumentRequestDTO documentRequestDTO) throws IOException {
        DocumentResponseDTO documentResponseDTO = documentService.update(id, documentRequestDTO);

        return new ResponseEntity<>(documentResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{documentId}/signers/{signerId}")
    public ResponseEntity<String> addSigner(@PathVariable UUID documentId, @PathVariable UUID signerId) {
        documentService.addSigner(documentId, signerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{documentId}/signers")
    public ResponseEntity<DocumentSignerResponseDTO> getSigners(@PathVariable UUID documentId) {
        List<DocumentSignerDTO> signers = documentService.getSignerByDocumentId(documentId);
        DocumentSignerResponseDTO documentSignerResponseDTO = new DocumentSignerResponseDTO(signers);
        return new ResponseEntity<>(documentSignerResponseDTO, HttpStatus.OK);
    }
}
