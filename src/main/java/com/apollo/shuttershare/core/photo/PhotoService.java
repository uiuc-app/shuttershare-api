package com.apollo.shuttershare.core.photo;

import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.photoentry.PhotoEntryService;
import com.apollo.shuttershare.core.photoentry.PhotoEntryVO;
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

    @Autowired
    PhotoEntryService photoEntryService;

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

    public PhotoVO createPhoto(InputStream in, UserVO user) {
        log.debug("Saving photo for user {} from InputStream into file system, create PhotoVO and save the information in DB.", user);
        PhotoVO photo = new PhotoVO();
        photo.setCreateAt(System.currentTimeMillis());
        photo.setUserId(user.getId());
        photoMapper.save(photo);

        saveRealFileToFileSystem(in, getPermanentFileName(photo), getPermanentFileDirectoryPath(photo, PhotoQuality.full));
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

    private String getPermanentFileDirectoryPath(PhotoVO photo, PhotoQuality quality) {
        log.debug("Generate directory path for PhotoVO {} with its id.", photo);

        StringBuilder builder = new StringBuilder(basePath);

        builder.append("/").
                append(quality.toString()).
                append("/").
                append(photo.getId() / 10000);
        return builder.toString();
    }

    private String getPermanentFileName(PhotoVO photo) {
        return "" + photo.getId();
    }

    public void authenticateUserWithPhoto(UserVO user, PhotoVO photo) {
        log.debug("Authenticating user {} for photo {}", user, photo);
        List<PhotoEntryVO> photoEntries = photoEntryService.getPhotoEntriesWithUserAndPhoto(user, photo);
        if (photoEntries.isEmpty()) {
            throw new UnauthorizedException("The user has no permission to view the photo");
        }
    }

    public File getFileForPhotoWithQuality(PhotoVO photo, PhotoQuality quality) {
        log.debug("Getting File object for the photo {}, with given quality {}");
        File file = new File(getPermanentFileDirectoryPath(photo, quality), getPermanentFileName(photo));
        return file;
    }
}
