package com.apollo.shuttershare.core.photo;

import lombok.extern.slf4j.Slf4j;
import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.core.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.util.List;

/**
 * @author Daniel
 */
@Service
@Transactional
@Slf4j
public class PhotoService {
    @Autowired
    PhotoMapper photoMapper;

    @Value("${file.basePath}")
    private String basePath;

    public PhotoVO getPhoto(long id) {
        log.debug("Loading PhotoVO with id {}.", id);
        PhotoVO result = photoMapper.get(id);
        if (result == null) {
            throw new ShutterShareException("PhotoVO with id {} is not found in the Database.");
        } else {
            return result;
        }
    }

    public PhotoVO createPhoto(InputStream in, String fileName, Long groupId, Long userId, Long deviceId) {
        log.debug("Save photo with name {} from InputStream into file system, create PhotoVO and save the information in DB.", fileName);
        PhotoVO photo = new PhotoVO();
        photo.setCreateAt(System.currentTimeMillis());
        photo.setUserId(userId);
        photo.setCreateAt(System.currentTimeMillis());

        saveRealFileToFileSystem(in, "" + photo.getId(), getPermanentFileDirectoryPath(photo));
        photoMapper.save(photo);
        return photo;
    }

    private File saveRealFileToFileSystem(InputStream in, String fileName, String directoryPath) {
        log.debug("Saving a file into directory {} with file name {}", directoryPath, fileName);
        File file = new File(directoryPath, fileName);
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new ShutterShareException("Failed to save photo.", e);
        }
        return file;
    }

    private String getPermanentFileDirectoryPath(PhotoVO photo) {
        log.debug("Generate directory path for PhotoVO {} with its id.", photo);

        StringBuilder builder = new StringBuilder(basePath);

        builder.append("/").
                append(photo.getId() / 10000);
        return builder.toString();
    }

    public List<PhotoVO> getPhotosForGroup(Long groupId, UserVO user) {
        //TODO authenticate user with group
        return null;
    }
}
