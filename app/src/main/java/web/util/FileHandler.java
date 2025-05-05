package web.util;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import web.config.StorageConfig;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileHandler {

    private final StorageConfig storageConfig;

    private final static String USER_DIR_NAME = "user.dir";

    public String loadFile(MultipartFile file, String fileName) {
        val filePath = storageConfig.path() + File.separator + fileName;
        File root = new File(System.getProperty(USER_DIR_NAME));
        File targetFile = new File(root, filePath);
        targetFile.getParentFile().mkdirs();

        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла " + e.getMessage()) ;
        }

        return filePath;
    }

    public boolean deleteFile(String filePath) {
        File root = new File(System.getProperty(USER_DIR_NAME));
        File targetFile = new File(root, filePath);

        if (targetFile.exists()) {
            return targetFile.delete();
        }

        return false;
    }
}
