package com.apollo.shuttershare.core.city;

import lombok.Data;

/**
 * Date: 4/21/14
 */
@Data
public class CityVO {
	private Long id;
	private String name;
	private Double latitude;
	private Double longitude;
	private String county;
	private String stateAbbreviation;
	private String state;
}
