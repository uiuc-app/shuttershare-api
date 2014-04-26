package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.user.DeviceService;
import com.apollo.shuttershare.core.user.DeviceVO;
import com.apollo.shuttershare.core.user.UserService;
import com.apollo.shuttershare.core.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/")
@Transactional
@Slf4j
public class UserController {
    @Autowired
	PhotoService photoService;

	@Autowired
	UserService userService;

	@Autowired
	DeviceService deviceService;

	@ResponseBody
	@RequestMapping(value = "/users/{targetUserId}/photos", method = RequestMethod.GET)
	public PhotoElements.JsonPhotos getUserPhotosList(
			UserVO viewer,
			@PathVariable Long targetUserId,
			@RequestParam(defaultValue = "20") Integer limit,
			@RequestParam(defaultValue = Long.MAX_VALUE + "") Long before,
			@RequestParam(defaultValue = "-1") Long after) {
		log.debug("Get the list of photos uploaded by user {} viewable by {}", targetUserId, viewer);

		List<PhotoElements.JsonPhoto> result = photoService.getViewableUserJsonPhotosList(
				userService.getUser(targetUserId), viewer, limit, before, after);
		return new PhotoElements.JsonPhotos(result);
	}

	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public UserElements.JsonUser signUpUser(@RequestParam String name,
	                                        @RequestParam("device_type") String deviceType,
	                                        @RequestParam String udid) {
		log.debug("Create a new user with name {}, deviceType {}, udid {}", name, deviceType, udid);
		UserVO user = userService.createNewUser(name);
		DeviceVO device = deviceService.createNewDevice(deviceType, udid, user);

		return new UserElements.JsonUser(user, new ArrayList<>(Arrays.asList(new DeviceVO[]{device})));
	}

	@ResponseBody
	@RequestMapping(value = "/devices", method = RequestMethod.POST)
	public UserElements.JsonDevice addDevice(UserVO user,
	                                        @RequestParam("device_type") String deviceType,
	                                        @RequestParam String udid) {
		log.debug("Create a a new device for user {}, deviceType {}, udid {}", user, deviceType, udid);
		DeviceVO device = deviceService.createNewDevice(deviceType, udid, user);

		return new UserElements.JsonDevice(device);
	}

}
