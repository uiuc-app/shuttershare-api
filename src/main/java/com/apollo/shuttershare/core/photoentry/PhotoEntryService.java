package com.apollo.shuttershare.core.photoentry;

import com.apollo.shuttershare.core.group.GroupVO;
import com.apollo.shuttershare.core.photo.PhotoVO;
import com.apollo.shuttershare.core.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 2/15/14
 */
@Service
@Slf4j
@Transactional
public class PhotoEntryService {
    @Autowired
    PhotoEntryMapper photoEntryMapper;

    public PhotoEntryVO addPhotoEntry(PhotoVO photo, GroupVO group, Long userId, boolean acknowledged) {
        log.debug("Adding a new photo entry to DB with photo {}, group {} for user {}, with ack field {}", photo, group, userId, acknowledged);
        PhotoEntryVO photoEntry = new PhotoEntryVO();
        photoEntry.setCreateAt(System.currentTimeMillis());
        photoEntry.setPhotoId(photo.getId());
        photoEntry.setGroupId(group.getId());
        photoEntry.setUserId(userId);
        photoEntry.setAcknowledged(acknowledged);

        photoEntryMapper.save(photoEntry);
        return photoEntry;
    }

    public List<PhotoEntryVO> getPhotoEntriesWithUserAndPhoto(UserVO user, PhotoVO photo) {
        log.debug("Retrieving PhotoEntryVOs with user {}, and photo {}", user, photo);
        return photoEntryMapper.getListWithUserIdAndPhotoId(user.getId(), photo.getId());
    }

    public List<PhotoEntryVO> getPhotoEntriesWithUser(UserVO user, int limit) {
        return photoEntryMapper.getListWithUserIdAndLimit(user.getId(), limit);
    }
}
