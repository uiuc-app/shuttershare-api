package com.apollo.shuttershare.core.photo;

import com.apollo.shuttershare.common.UnauthorizedException;
import com.apollo.shuttershare.core.city.CityService;
import com.apollo.shuttershare.core.city.CityVO;
import com.apollo.shuttershare.core.photoentry.PhotoEntryService;
import com.apollo.shuttershare.core.photoentry.PhotoEntryVO;
import com.apollo.shuttershare.web.PhotoElements;
import lombok.extern.slf4j.Slf4j;
import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.core.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
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

	@Autowired
	CityService cityService;

    @Value("${file.basePath}")
    private String basePath;

    private static final Integer THUMBNAIL_WIDTH = 360;

    public PhotoVO getPhoto(long id) {
        log.debug("Loading PhotoVO with id {}.", id);
        PhotoVO result = photoMapper.get(id);
        if (result == null) {
            throw new ShutterShareException("PhotoVO with id {} is not found in the Database.");
        } else {
            return result;
        }
    }

    public PhotoVO createPhoto(InputStream in, UserVO user, Double latitude, Double longitude) {
        log.debug("Saving photo for user {} from InputStream into file system, create PhotoVO and save the information in DB.", user);
        PhotoVO photo = new PhotoVO();
        photo.setCreateAt(System.currentTimeMillis());
        photo.setUserId(user.getId());
        photo.setLatitude(latitude);
        photo.setLongitude(longitude);
	    if (latitude != null && longitude != null) {
		    CityVO city = cityService.getClosestCityFromCoordinate(latitude, longitude);
		    if (city != null) {
			    photo.setCityId(city.getId());
		    } else {
			    log.warn("Closest city does not exist");
		    }
	    }
        photoMapper.save(photo);

        try {
            BufferedImage img = ImageIO.read(in);
            createAndSaveThumbnailPhoto(img, getPermanentFileName(photo), getPermanentFileDirectoryPath(photo, PhotoQuality.thumbnail));
            saveRealFileToFileSystem(img, getPermanentFileName(photo), getPermanentFileDirectoryPath(photo, PhotoQuality.full));
        } catch (IOException e) {
            throw new ShutterShareException("Unable to read image file", e);
        }
        return photo;
    }

    public void createAndSaveThumbnailPhoto(BufferedImage srcImg, String fileName, String directoryPath) {
        log.debug("Creating thumbnail image and save the file {} in the directory {}", fileName, directoryPath);

	    int scaledHeight = (int) (srcImg.getHeight() * ((double) THUMBNAIL_WIDTH / srcImg.getWidth()));
			    BufferedImage img = new BufferedImage(THUMBNAIL_WIDTH, scaledHeight, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(srcImg.getScaledInstance(THUMBNAIL_WIDTH, scaledHeight, Image.SCALE_SMOOTH), 0, 0, null);
        saveRealFileToFileSystem(img, fileName, directoryPath);
    }

    private File saveRealFileToFileSystem(BufferedImage img, String fileName, String directoryPath) {
        log.debug("Saving a file into directory {} with file name {}", directoryPath, fileName);
        File file = new File(directoryPath, fileName);
        file.getParentFile().mkdirs();
        try {
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            throw new ShutterShareException("Failed to save photo into file system.", e);
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

    public List<PhotoElements.JsonPhoto> getGroupJsonPhotosList(Long groupId, Long limit, Long before, Long after) {
        log.debug("Getting group {}'s photos list in JsonPhoto with limit {}, before {}, after {}", limit, before, after);
        List<PhotoVO> photos = photoMapper.getListWithGroupId(groupId, limit, before, after);
        List<PhotoEntryVO> photoEntries = photoEntryService.getPhotoEntriesWithPhotos(photos);

        Map<Long, Set<Long>> photoToGroupIdsMap = new LinkedHashMap<>();
        for (PhotoEntryVO photoEntry : photoEntries) {
            if (photoToGroupIdsMap.containsKey(photoEntry.getPhotoId())) {
                photoToGroupIdsMap.get(photoEntry.getPhotoId()).add(photoEntry.getGroupId());
            } else {
                photoToGroupIdsMap.put(photoEntry.getPhotoId(), new HashSet<Long>());
                photoToGroupIdsMap.get(photoEntry.getPhotoId()).add(photoEntry.getGroupId());
            }
        }

        List<PhotoElements.JsonPhoto> result = new ArrayList<>();
        for (PhotoVO photo : photos) {
            result.add(new PhotoElements.JsonPhoto(photo, (photoToGroupIdsMap.get(photo.getId()).toArray(new Long[1]))));
        }
        return result;
    }

	public List<PhotoElements.JsonPhoto> getCityJsonPhotosList(Long cityId, int limit, Long before, Long after) {
		log.debug("Getting city {}'s photos list in JsonPhoto with limit {}, before {}, after {}", cityId, limit, before, after);
		List<PhotoVO> photos = photoMapper.getListWithCityId(cityId, limit, before, after);
		List<PhotoEntryVO> photoEntries = photoEntryService.getPhotoEntriesWithPhotos(photos);

		Map<Long, Set<Long>> photoToGroupIdsMap = new LinkedHashMap<>();
		for (PhotoEntryVO photoEntry : photoEntries) {
			if (photoToGroupIdsMap.containsKey(photoEntry.getPhotoId())) {
				photoToGroupIdsMap.get(photoEntry.getPhotoId()).add(photoEntry.getGroupId());
			} else {
				photoToGroupIdsMap.put(photoEntry.getPhotoId(), new HashSet<Long>());
				photoToGroupIdsMap.get(photoEntry.getPhotoId()).add(photoEntry.getGroupId());
			}
		}

		List<PhotoElements.JsonPhoto> result = new ArrayList<>();
		for (PhotoVO photo : photos) {
			result.add(new PhotoElements.JsonPhoto(photo, (photoToGroupIdsMap.get(photo.getId()).toArray(new Long[1]))));
		}
		return result;
	}

	public List<PhotoElements.JsonPhoto> getViewableUserJsonPhotosList(UserVO user, UserVO viewer, int limit, Long before, Long after) {
		log.debug("Getting photos by {} that are viewable by viewer {}", user, viewer);
		List<PhotoVO> photos = photoMapper.getListUserAndViewerId(user.getId(), viewer.getId(), limit, before, after);
		List<PhotoEntryVO> photoEntries = photoEntryService.getPhotoEntriesWithPhotos(photos);

		Map<Long, Set<Long>> photoToGroupIdsMap = new LinkedHashMap<>();
		for (PhotoEntryVO photoEntry : photoEntries) {
			if (photoToGroupIdsMap.containsKey(photoEntry.getPhotoId())) {
				photoToGroupIdsMap.get(photoEntry.getPhotoId()).add(photoEntry.getGroupId());
			} else {
				photoToGroupIdsMap.put(photoEntry.getPhotoId(), new HashSet<Long>());
				photoToGroupIdsMap.get(photoEntry.getPhotoId()).add(photoEntry.getGroupId());
			}
		}

		List<PhotoElements.JsonPhoto> result = new ArrayList<>();
		for (PhotoVO photo : photos) {
			result.add(new PhotoElements.JsonPhoto(photo, (photoToGroupIdsMap.get(photo.getId()).toArray(new Long[1]))));
		}
		return result;
	}
}
