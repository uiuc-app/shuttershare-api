package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.photo.PhotoVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Daniel
 * Date: 2/10/14
 * Time: 4:46 PM
 */
public class PhotoElements {
	@Data
    public static class JsonPhoto {
        public Long id;
        public Long user_id;
        public Long create_at;
        public Double latitude;
        public Double longitude;
	    public Long city_id;
        public Long[] group_ids;

        public JsonPhoto(PhotoVO photo, Long[] group_ids) {
            this.id = photo.getId();
            this.user_id = photo.getUserId();
            this.create_at = photo.getCreateAt();
            this.latitude = photo.getLatitude();
            this.longitude = photo.getLongitude();
            this.group_ids = group_ids;
	        this.city_id = photo.getCityId();
        }
    }

    public static class JsonPhotos {
        public List<JsonPhoto> photos = new ArrayList<>();

        public JsonPhotos(List<JsonPhoto> photos) {
            this.photos = photos;
        }
    }
}
