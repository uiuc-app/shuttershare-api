package com.apollo.shuttershare.core.facedetect;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Date: 4/29/14
 */
@Slf4j
public class FaceDetectionRunner extends QuartzJobBean {
	FaceDetectionService faceDetectionService;
	public void setFaceDetectionService(FaceDetectionService faceDetectionService) {
		this.faceDetectionService = faceDetectionService;
	}

	@Override
	protected void executeInternal(org.quartz.JobExecutionContext context) throws JobExecutionException {
		try {
			log.debug("Running a FaceDetection Daemon");
//			faceDetectionService.detectFacesForUnprocessedPhotos();
		} catch (Exception e) {
			log.error("Error while running Face Detection Daemon {}", e);
		}
	}
}
