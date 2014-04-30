package com.apollo.shuttershare.core.facedetect;

import lombok.Data;

/**
 * Date: 4/29/14
 */
@Data
public class FaceVO {
	private Long id;
	private Long photoId;
	private Integer faceIndex;
	private Integer x;
	private Integer y;
	private Integer width;
	private Integer height;
}
