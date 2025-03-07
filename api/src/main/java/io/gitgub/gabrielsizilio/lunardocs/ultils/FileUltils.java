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

    private void ensureDirectoryExists() {
        File file = new File(uploadDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String generateFileName(UUID id, String fileName) {
        return id + "_" + fileName;
    }

    public String uploadDocument(MultipartFile file, String fileNameId) throws IOException {
        ensureDirectoryExists();
        Path path = Paths.get(uploadDirectory, fileNameId);

        Files.copy(file.getInputStream(), path);
        return path.toString();
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

    public Boolean deleteFile(UUID idDocument, String fileName) {
        try {
            Path pathExist = Paths.get(uploadDirectory, generateFileName(idDocument, fileName));

            File file = pathExist.toFile();
            if (file.exists()) {
                Files.deleteIfExists(pathExist);
                return true;
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting file: " + fileName, e);
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

    public String generateFileHash(UUID idDocument) throws IOException, NoSuchAlgorithmException {
        Path filePath = findFileById(idDocument);
        byte[] fileBytes = Files.readAllBytes(filePath);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fileBytes);

        System.out.println("HASH FILE: " + Base64.getEncoder().encodeToString(hash));

        return Base64.getEncoder().encodeToString(hash);
    }
}
