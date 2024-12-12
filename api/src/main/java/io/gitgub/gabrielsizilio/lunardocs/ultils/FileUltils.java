package io.gitgub.gabrielsizilio.lunardocs.ultils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Component
public class FileUltils {

    @Value("${upload.document.directory}")
    private String uploadDirectory;

    private void ensureDirectoryExists() {
        File file = new File(uploadDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String generateFileName(String fileName, UUID id) {
        return id + "_" + fileName;
    }

    public String uploadDocument(MultipartFile file, String fileNameId) throws IOException {
        ensureDirectoryExists();
        Path path = Paths.get(uploadDirectory, fileNameId);

        Files.copy(file.getInputStream(), path);
        return path.toString();
    }
}
