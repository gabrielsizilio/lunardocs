package io.gitgub.gabrielsizilio.lunardocs.controller;

import io.gitgub.gabrielsizilio.lunardocs.dto.documentDto.*;
import io.gitgub.gabrielsizilio.lunardocs.service.DocumentService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("")
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

    @GetMapping("")
    public ResponseEntity<List<DocumentResponseDTO>> findAllDocuments() {
        List<DocumentResponseDTO> documents = documentService.findAllDocuments();

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("mine")
    public ResponseEntity<List<DocumentResponseDTO>> findMyDocuments() {
        List<DocumentResponseDTO> documents = documentService.findMyDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(@PathVariable UUID id, @RequestBody DocumentRequestDTO documentRequestDTO) throws IOException {
        DocumentResponseDTO documentResponseDTO = documentService.update(id, documentRequestDTO);

        return new ResponseEntity<>(documentResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id, @RequestParam String version) throws IOException {
         FileSystemResource fileSystemResource = documentService.downloadDocument(id, version);

         if(fileSystemResource !=null) {
             return ResponseEntity.ok()
                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileSystemResource.getFilename())
                     .contentType(MediaType.APPLICATION_OCTET_STREAM)
                     .body(fileSystemResource);
         } else {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
    }

    @PostMapping("/{documentId}/signers")
    public ResponseEntity<DocumentResponseDTO> addSigner(@PathVariable UUID documentId, @RequestBody DocumentSignersRequestDTO documentSignersRequestDTO) throws IOException  {

        DocumentResponseDTO documentResponseDTO = documentService.addSigner(documentId, documentSignersRequestDTO.signersIds());
        return new ResponseEntity<>(documentResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}/signers/{signerId}")
    public ResponseEntity<DocumentResponseDTO> removeSigner(@PathVariable UUID documentId, @PathVariable UUID signerId) {
        DocumentResponseDTO documentResponseDTO = documentService.removeSigner(documentId, signerId);
        return new ResponseEntity<>(documentResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{documentId}/signers")
    public ResponseEntity<DocumentSignerResponseDTO> getSigners(@PathVariable UUID documentId) {
        List<DocumentSignerDTO> signers = documentService.getSignerByDocumentId(documentId);
        DocumentSignerResponseDTO documentSignerResponseDTO = new DocumentSignerResponseDTO(signers);
        return new ResponseEntity<>(documentSignerResponseDTO, HttpStatus.OK);
    }
}
