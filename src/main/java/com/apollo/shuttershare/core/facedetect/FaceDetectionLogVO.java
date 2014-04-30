package com.apollo.shuttershare.core.facedetect;

import lombok.Data;

/**
 * Date: 4/29/14
 */
@Data
public class FaceDetectionLogVO {
	private Long id;
	private Long photoId;
	private Long createAt;
	private Integer numFaces;
}
