package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.city.CityService;
import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/")
@Transactional
@Slf4j
public class CityController {
    @Autowired
    PhotoService photoService;

	@Autowired
	CityService cityService;

	@ResponseBody
	@RequestMapping(value = "/cities", method = RequestMethod.GET)
	public CityElements.JsonCities getClosestCitiesList(
			UserVO user,
			@RequestParam Double latitude,
			@RequestParam Double longitude) {
		log.debug("Getting the list of closest cities with photos for user {}", user);

		return cityService.getClosestCitiesWithPhotosForUser(user, latitude, longitude);
	}

    @ResponseBody
    @RequestMapping(value = "/cities/{id}/photos", method = RequestMethod.GET)
    public PhotoElements.JsonPhotos getGroupPhotosList(
                              UserVO user,
                              @PathVariable Long id,
                              @RequestParam(defaultValue = "20") Integer limit,
                              @RequestParam(defaultValue = Long.MAX_VALUE + "") Long before,
                              @RequestParam(defaultValue = "-1") Long after) {
        log.debug("Get the list of photos in city with id {}", id);

        List<PhotoElements.JsonPhoto> result = photoService.getCityJsonPhotosList(id, limit, before, after);
        return new PhotoElements.JsonPhotos(result);
    }
}
