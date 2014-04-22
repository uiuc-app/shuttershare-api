package com.apollo.shuttershare.core.city;

import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.user.UserVO;
import com.apollo.shuttershare.web.CityElements;
import com.apollo.shuttershare.web.PhotoElements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 4/21/14
 */
@Service
@Slf4j
public class CityService {
	@Autowired
	CityMapper cityMapper;

	@Autowired
	PhotoService photoService;

	private static final Double HEURISTIC_THRESHOLD = 1.0;

	public CityVO getClosestCityFromCoordinate(final Double latitude, final Double longitude) {
		log.debug("Retrieving closest city at latitude:{}, longitude:{}", latitude, longitude);
		List<CityVO> cities = cityMapper.getClosestListHeuristic(latitude, longitude, HEURISTIC_THRESHOLD);

		double minDistance = Double.MAX_VALUE;
		CityVO closestCity = null;
		for (CityVO city : cities) {
			double distance = cartesianDistance(latitude, longitude, city.getLatitude(), city.getLongitude());
			if (minDistance > distance) {
				closestCity = city;
				minDistance = distance;
			}
		}
		log.debug("closest city is {}", closestCity);
		return closestCity;
	}

	private static Double cartesianDistance(Double lat1, Double long1, Double lat2, Double long2) {
		return Math.sqrt((lat1 - lat2) * (lat1 - lat2) + (long1 - long2) * (long1 - long2));
	}

	/**
	 * Get at most 10 cities along with city's representative photo's id.
	 * @param user
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public CityElements.JsonCities getClosestCitiesWithPhotosForUser(UserVO user,
	                                                                   final Double latitude,
	                                                                   final Double longitude) {
		log.debug("Getting closest cities and photo list for user {} near lat:{}, long:{}", user, latitude, longitude);

		List<CityVO> cities = cityMapper.getClosestCitiesWithPhotosForUser(user.getId(), latitude, longitude);
		List<CityElements.JsonCity> jsonCities = new ArrayList<>();
		for (CityVO city : cities) {
			List<PhotoElements.JsonPhoto> jsonPhotos = photoService.getCityJsonPhotosList(city.getId(), 1, Long.MAX_VALUE, -1l);
			if (!jsonPhotos.isEmpty()) {
				jsonCities.add(new CityElements.JsonCity(city, jsonPhotos.get(0).id));
			}
		}

		return new CityElements.JsonCities(jsonCities);
	}
}
