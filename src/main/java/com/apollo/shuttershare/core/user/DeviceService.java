package com.apollo.shuttershare.core.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 4/26/14
 */
@Service
@Slf4j
@Transactional
public class DeviceService {
	@Autowired
	DeviceMapper deviceMapper;

	public DeviceVO createNewDevice(String deviceType, String udid, UserVO user) {
		log.debug("Create a new device with deviceType {}, udid {}, for user {}", deviceType, udid, user);
		DeviceVO device = new DeviceVO();
		device.setDeviceType(deviceType);
		device.setUdid(udid);
		device.setUserId(user.getId());
		deviceMapper.save(device);
		return device;
	}

	public List<DeviceVO> getDevicesForUser(UserVO user) {
		log.debug("Getting list of devices for user {}", user);
		return deviceMapper.getListForUser(user.getId());
	}
}
