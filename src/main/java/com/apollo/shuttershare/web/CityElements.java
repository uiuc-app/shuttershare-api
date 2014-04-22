package com.apollo.shuttershare.web;

import com.apollo.shuttershare.core.city.CityVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 4/22/14
 */
public class CityElements {
	public static class JsonCity {
		public JsonCity(CityVO city, Long photoId) {
			this.id = city.getId();
			this.name = city.getName();
			this.latitude = city.getLatitude();
			this.longitude = city.getLongitude();
			this.county = city.getCounty();
			this.stateAbbreviation = city.getStateAbbreviation();
			this.state = city.getState();
			this.backgroundPhotoId = photoId;
		}

		public Long id;
		public String name;
		public Double latitude;
		public Double longitude;
		public String county;
		public String stateAbbreviation;
		public String state;
		public Long backgroundPhotoId;
	}

	public static class JsonCities {
		public List<JsonCity> cities = new ArrayList<>();

		public JsonCities(List<JsonCity> cities) {
			this.cities = cities;
		}
	}
}
