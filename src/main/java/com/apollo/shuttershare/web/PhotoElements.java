package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.photo.PhotoVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Daniel
 * Date: 2/10/14
 * Time: 4:46 PM
 */
public class PhotoElements {
    public static class JsonPhoto {
        public Long id;
        public Long userId;
        public Long createAt;

        public JsonPhoto(PhotoVO photo) {
            this.id = photo.getId();
            this.userId = photo.getUserId();
            this.createAt = photo.getUserId();
        }
    }

    public static class JsonPhotos {
        public List<JsonPhoto> photos = new ArrayList<>();

        public JsonPhotos(List<PhotoVO> photos) {
            for (PhotoVO photo : photos) {
                this.photos.add(new JsonPhoto(photo));
            }
        }
    }
}
