package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.user.DeviceVO;
import com.apollo.shuttershare.core.user.UserVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 4/26/14
 */
public class UserElements {
	public static class JsonUser {
		public Long id;
		public String name;
		public List<JsonDevice> devices;
		public String api_key;

		public JsonUser(UserVO user, List<DeviceVO> devices) {
			this.id = user.getId();
			this.name = user.getName();
			this.devices = new ArrayList<>();
			for (DeviceVO device : devices) {
				this.devices.add(new JsonDevice(device));
			}
			this.api_key = user.getApiKey();
		}
	}

	public static class JsonDevice {
		public JsonDevice(DeviceVO device) {
			this.device_type = device.getDeviceType();
			this.udid = device.getUdid();
			this.user_id = device.getUserId();
		}

		public String device_type;
		public String udid;
		public Long user_id;
	}
}
