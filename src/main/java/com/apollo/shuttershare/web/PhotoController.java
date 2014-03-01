package com.apollo.shuttershare.web;

import com.apollo.shuttershare.common.ShutterShareException;
import com.apollo.shuttershare.core.group.GroupService;
import com.apollo.shuttershare.core.photo.PhotoQuality;
import com.apollo.shuttershare.core.photo.PhotoVO;
import lombok.extern.slf4j.Slf4j;
import com.apollo.shuttershare.common.SqlInjectionMapper;
import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.user.UserVO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/")
@Transactional
@Slf4j
public class PhotoController {
    @Autowired
    PhotoService photoService;

    @Autowired
    GroupService groupService;

	@Autowired
	SqlInjectionMapper sqlInjectionMapper;

    @ResponseBody
    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public PhotoElements.JsonPhoto uploadPhoto(UserVO user,
                                               @RequestParam Long[] group_ids,
                                               @RequestParam MultipartFile image,
                                               @RequestParam(required = false) Double latitude,
                                               @RequestParam(required = false) Double longitude) {
        log.debug("Uploading a photo to groups {} for user {}", Arrays.toString(group_ids), user);
        // Authenticate user to all of the requested groups
        groupService.authenticateUserToGroups(group_ids, user);

        // Add photo
        PhotoVO photo;
        try {
            photo = photoService.createPhoto(image.getInputStream(), user, latitude, longitude);
        } catch (IOException e) {
            throw new ShutterShareException("Unable to handle uploaded photo", e);
        }

        // For each group, save photo entries to the group
        for (Long groupId : group_ids) {
            groupService.addPhotoToGroup(groupId, photo, user);
        }

        return new PhotoElements.JsonPhoto(photo, group_ids);
    }

    @RequestMapping(value = {"/photos/{id}", "/photos/{id}/{dummy}"}, method = RequestMethod.GET)
    public void downloadPhoto(HttpServletResponse response,
                              UserVO user,
                              @PathVariable Long id,
                              @RequestParam(defaultValue = "full") PhotoQuality quality) {
        log.debug("Downloading the photo with id {} with quality {}", id, quality);

        PhotoVO photo = photoService.getPhoto(id);
        photoService.authenticateUserWithPhoto(user, photo);

        File file = photoService.getFileForPhotoWithQuality(photo, quality);

        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(photo.getId() + "", "UTF-8").replace("+", "%20"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new ShutterShareException("Unable to process file", e);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/groups/{id}/photos", method = RequestMethod.GET)
    public PhotoElements.JsonPhotos getGroupPhotosList(
                              UserVO user,
                              @PathVariable Long id,
                              @RequestParam(defaultValue = "20") Long limit,
                              @RequestParam(defaultValue = Long.MAX_VALUE + "") Long before,
                              @RequestParam(defaultValue = "-1") Long after) {
        log.debug("Get the list of photos in group with id {}", id);

        groupService.authenticateUserToGroups(new Long[]{id}, user);

        List<PhotoElements.JsonPhoto> result = photoService.getGroupJsonPhotosList(id, limit, before, after);
        return new PhotoElements.JsonPhotos(result);
    }
}
