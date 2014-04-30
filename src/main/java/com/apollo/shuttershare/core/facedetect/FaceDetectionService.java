package com.apollo.shuttershare.core.facedetect;

import com.apollo.shuttershare.core.photo.PhotoQuality;
import com.apollo.shuttershare.core.photo.PhotoService;
import com.apollo.shuttershare.core.photo.PhotoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * Date: 4/29/14
 */
@Service
@Slf4j
@Transactional
public class FaceDetectionService {
	private static final String basePath = "/tmp/faces";

	private static final int DAEMON_PROCESS_LIMIT = 10;

	@Autowired
	PhotoService photoService;

	@Autowired
	FaceMapper faceMapper;

	@Autowired
	FaceDetectionLogMapper faceDetectionLogMapper;

	synchronized public void detectFacesForUnprocessedPhotos() {
		log.debug("Detecting faces for unprocessed photos");
		long startProcess = System.currentTimeMillis();
		List<PhotoVO> photos = faceDetectionLogMapper.getUnprocessedPhotos(DAEMON_PROCESS_LIMIT);
		for (PhotoVO photo : photos) {
			detectFacesAndSaveFaces(photo);
		}
		log.debug("Processed {} photos in {}ms", photos.size(), System.currentTimeMillis() - startProcess);
	}

	public void detectFacesAndSaveFaces(PhotoVO photo) {
		log.debug("Detecting faces for PhotoVO {}", photo);
		File srcFile = photoService.getFileForPhotoWithQuality(photo, PhotoQuality.full);

		List<FaceDetector.DetectedFace> detectedFaces = FaceDetector.detectFacesAndSave(srcFile, getPermanentFileDirectoryPath(photo));

		for (FaceDetector.DetectedFace detectedFace : detectedFaces) {
			FaceVO face = new FaceVO();
			face.setFaceIndex(detectedFace.getIndex());
			face.setX(detectedFace.getX());
			face.setY(detectedFace.getY());
			face.setWidth(detectedFace.getWidth());
			face.setHeight(detectedFace.getHeight());
			face.setPhotoId(photo.getId());
			faceMapper.save(face);
		}
		FaceDetectionLogVO faceDetectionLog = new FaceDetectionLogVO();
		faceDetectionLog.setPhotoId(photo.getId());
		faceDetectionLog.setCreateAt(System.currentTimeMillis());
		faceDetectionLog.setNumFaces(detectedFaces.size());
		faceDetectionLogMapper.save(faceDetectionLog);
	}

	private String getPermanentFileDirectoryPath(PhotoVO photo) {
		log.debug("Generating directory path for detected faces for PhotoVO {} with its id.", photo);

		StringBuilder builder = new StringBuilder(basePath);

		builder.append("/").
				append(photo.getId() / 10000).
				append("/").
				append(photo.getId());
		return builder.toString();
	}
}
