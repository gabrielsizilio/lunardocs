package io.gitgub.gabrielsizilio.lunardocs.ultils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
@Component
public class FileUltils {

    @Value("${upload.document.directory.absolute}")
    private String uploadDirectory;
    private String baseNameDocument = "document_V";

    private void ensureDirectoryExists() {
        File file = new File(uploadDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String getNextVersionFileName(Path documentFolder, String originalFileName) {
        String extension = "";
        int dotIndex = uploadDirectory.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }

        int version = 1;
        while (Files.exists(documentFolder.resolve(baseNameDocument + version + extension))) {
            version++;
        }

        return baseNameDocument + version + extension;
    }

    public static String generateFileName(UUID id, String fileName) {
        return id + "_" + fileName;
    }

    public String uploadDocument(MultipartFile file, UUID documentId) throws IOException {
        ensureDirectoryExists();

        Path documentFolder = Paths.get(uploadDirectory, documentId.toString());
        if(!Files.exists(documentFolder)) {
            Files.createDirectories(documentFolder);
        }
        String fileName = getNextVersionFileName(documentFolder, file.getOriginalFilename());

        Path filePath = documentFolder.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }

    public String updateFileName(String newName, UUID idDocument) throws IOException {
        ensureDirectoryExists();

        Path existPath = findFileById(idDocument);
        if(Files.exists(existPath)) {
            Path newPath = Paths.get(uploadDirectory, generateFileName(idDocument, newName));
            Files.move(existPath, newPath);

            return newPath.toString();
        } else {
            throw new FileNotFoundException("File not found with id: " + idDocument);
        }
    }

    public Boolean deleteFile(UUID idDocument) {
        try {
            Path documentFolder = Paths.get(uploadDirectory, idDocument.toString());
            File folder = documentFolder.toFile();

            if (documentFolder.toFile().exists()) {
                File files[] = folder.listFiles();

                for(File file : files) {
                    file.delete();
                }

                if(folder.delete()) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting file: " + e);
        }
    }

    public Path findFileById(UUID id) throws FileNotFoundException {
        File folder = new File(uploadDirectory);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.getName().startsWith(id.toString())) {
                    return file.toPath();
                }
            }
        }
        throw new FileNotFoundException("Document is not found with id: " + id);
    }

    public Path getDocumentDirectory(UUID idDocument, String fileName) {
        Path directory = Paths.get(uploadDirectory, idDocument.toString() + "_" + fileName);
        return directory;
    }

    public Path getDocumentPath(UUID idDocument, String fileName, Integer version) {
        try {
            Path documentDirectory = getDocumentDirectory(idDocument, fileName);

            if(documentDirectory != null) {
                return Paths.get(documentDirectory.toString(), "document_V" + version);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while getting document path: " + e);
        }
        return null;
    }

    public String generateFileHash(UUID idDocument) throws IOException, NoSuchAlgorithmException {
        Path filePath = findFileById(idDocument);
        byte[] fileBytes = Files.readAllBytes(filePath);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fileBytes);

        System.out.println("HASH FILE: " + Base64.getEncoder().encodeToString(hash));

        return Base64.getEncoder().encodeToString(hash);
    }
}
