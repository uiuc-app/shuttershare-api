package com.apollo.shuttershare.core.facedetect;

// FaceDetection.java
// Andrew Davison, July 2013, ad@fivedots.psu.ac.th

/* Use JavaCV to detect faces in an image, and save a marked-faces
   version of the image to OUT_FILE.

   JavaCV location: http://code.google.com/p/javacv/

   Usage:
     run FaceDetection lena.jpg
*/

import com.googlecode.javacv.cpp.*;
import com.googlecode.javacpp.Loader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;


@Slf4j
public class FaceDetector {
	private static final int IM_SCALE = 4;
	private static final int FACE_WIDTH = 125;
	private static final int FACE_HEIGHT = 150;
	private static final int SCALE = 2;
	// scaling factor to reduce size of input image

	// cascade definition for face detection
//	private static final String CASCADE_FILE = "src/main/resources/haarcascade_frontalface_alt.xml";
	private static final String CASCADE_FILE = "/usr/share/haarcascade_frontalface_alt.xml";// Unfortunately I couldn't get it to work in Tomcat without using absolute path

	static {
		Loader.load(opencv_objdetect.class);
	}

	public static java.util.List<DetectedFace> detectFacesAndSave(File src, String destPath) {
		log.debug("Detecting image {}, and save cropped faces images to {}", src, destPath);

		// load an image
		IplImage origImg = cvLoadImage(src.getAbsolutePath());

		// convert to grayscale
		IplImage grayImg = cvCreateImage(cvGetSize(origImg), IPL_DEPTH_8U, 1);
		cvCvtColor(origImg, grayImg, CV_BGR2GRAY);

		// scale the grayscale (to speed up face detection)
		IplImage smallImg = IplImage.create(grayImg.width()/SCALE,
				grayImg.height()/SCALE, IPL_DEPTH_8U, 1);
		cvResize(grayImg, smallImg, CV_INTER_LINEAR);

		// equalize the small grayscale
		cvEqualizeHist(smallImg, smallImg);

		// create temp storage, used during object detection
		CvMemStorage storage = CvMemStorage.create();

		// instantiate a classifier cascade for face detection
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
				cvLoad(CASCADE_FILE));
		log.debug("Detecting faces...");
		CvSeq faces = cvHaarDetectObjects(smallImg, cascade, storage, 1.1, 3,
				CV_HAAR_DO_CANNY_PRUNING);
		// CV_HAAR_DO_ROUGH_SEARCH);
		// 0);
		cvClearMemStorage(storage);

		ArrayList<DetectedFace> result = new ArrayList<>();

		// iterate over the faces and draw yellow rectangles around them
		int total = faces.total();
		log.debug("Found {} faces", total);
		for (int i = 0; i < total; i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			cvRectangle(origImg, cvPoint( r.x()*SCALE, r.y()*SCALE ),    // undo the scaling
					cvPoint( (r.x() + r.width())*SCALE, (r.y() + r.height())*SCALE ),
					CvScalar.YELLOW, 6, CV_AA, 0);
			clipSaveFace(origImg, r, destPath, i + ".png");
			result.add(new DetectedFace(i, r.x() * SCALE, r.y() * SCALE, r.width() * SCALE, r.height() * SCALE));
		}
		return result;
	}

	/**
	 * clip the image using the current face rectangle, and save it into fnm
	 * The use of faceRect is in a synchronized block since it may be being
	 * updated or used for drawing at the same time in other threads.
	 * @param img
	 * @param cvRect
	 * @param absPath
	 * @param fileName
	 */
	private static void clipSaveFace(IplImage img, CvRect cvRect, String absPath, String fileName) {
		Rectangle faceRect = getRectangle(cvRect);
		BufferedImage clipIm = null;
		if (faceRect.width == 0) {
			log.debug("No face selected");
			return;
		}
		BufferedImage im = img.getBufferedImage();
		try {
			clipIm = im.getSubimage(faceRect.x, faceRect.y, faceRect.width, faceRect.height);
		} catch(RasterFormatException e) {
			throw new RuntimeException("Cannot clip the image", e);
		}
		if (clipIm != null)
			saveClip(clipIm, absPath, fileName);
	}

	/**
	 * resizes to at least a standard size, converts to grayscale,
	 * clips to an exact size, then saves in a standard location */
	private static void saveClip(BufferedImage clipIm, String absPath, String fileName) {
		long startTime = System.currentTimeMillis();

		log.debug("Saving clip for {}...", fileName);
		BufferedImage grayIm = resizeImage(clipIm);
		BufferedImage faceIm = clipToFace(grayIm);
		saveImage(faceIm, absPath, fileName);

		log.debug("Save time for {}: {}ms", fileName, (System.currentTimeMillis() - startTime));
	}

	private static BufferedImage resizeImage(BufferedImage im)
	// resize to at least a standard size, then convert to grayscale
	{
		// resize the image so *at least* FACE_WIDTH*FACE_HEIGHT size
		int imWidth = im.getWidth();
		int imHeight = im.getHeight();
		log.debug("Original (w,h): (" + imWidth + ", " + imHeight + ")");

		double widthScale = FACE_WIDTH / ((double) imWidth);
		double heightScale = FACE_HEIGHT / ((double) imHeight);
		double scale = (widthScale > heightScale) ? widthScale : heightScale;

		int nWidth = (int)Math.round(imWidth* scale);
		int nHeight = (int)Math.round(imHeight* scale);

		// convert to grayscale while resizing
		BufferedImage grayIm = new BufferedImage(nWidth, nHeight,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2 = grayIm.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(im, 0, 0, nWidth, nHeight,  0, 0, imWidth, imHeight, null);
		g2.dispose();

		log.debug("Scaled gray (w,h): (" + nWidth + ", " + nHeight + ")");
		return grayIm;
	}  // end of resizeImage()




	private static BufferedImage clipToFace(BufferedImage im)
	// clip image to FACE_WIDTH*FACE_HEIGHT size
	// I assume the input image is face size or bigger
	{
		int xOffset = (im.getWidth() - FACE_WIDTH)/2;
		int yOffset = (im.getHeight() - FACE_HEIGHT)/2;
		BufferedImage faceIm = null;
		try {
			faceIm = im.getSubimage(xOffset, yOffset, FACE_WIDTH, FACE_HEIGHT);
			log.debug("Clipped image to face dimensions: ({}, {})", FACE_WIDTH, FACE_HEIGHT);
		}
		catch(RasterFormatException e) {
			throw new RuntimeException("Could not clip the image", e);
		}
		return faceIm;
	}  // end of clipToFace()


	private static void saveImage(BufferedImage im, String absPath, String fileName)
	// save image in fnm
	{
		try {
			File file = new File(absPath, fileName);
			file.getParentFile().mkdirs();
			ImageIO.write(im, "png", file);
			log.debug("Saved image to " + fileName);
		}
		catch (IOException e) {
			throw new RuntimeException("Could not save image to " + fileName, e);
		}
	}  // end of saveImage()


	private static Rectangle getRectangle(CvRect r) {
		Rectangle rectangle = new Rectangle(
				r.x() * SCALE,
				r.y() * SCALE,
				r.width() * SCALE,
				r.height() * SCALE);
		return rectangle;
	}

	@Data
	@AllArgsConstructor
	public static class DetectedFace {
		private int index;
		private int x;
		private int y;
		private int width;
		private int height;
	}



	public static void main(String[] args)
	{
		if (args.length != 1) {
			System.out.println("Usage: run FaceDetection <input-file>");
			return;
		}

		// preload the opencv_objdetect module to work around a known bug
		Loader.load(opencv_objdetect.class);

		// load an image
		//System.out.println("Loading image from " + args[0]);
//		IplImage origImg = cvLoadImage(args[0]);

		IplImage origImg = cvLoadImage("/Users/Daniel/Desktop/group.jpg");

		// convert to grayscale
		IplImage grayImg = cvCreateImage(cvGetSize(origImg), IPL_DEPTH_8U, 1);
		cvCvtColor(origImg, grayImg, CV_BGR2GRAY);

		// scale the grayscale (to speed up face detection)
		IplImage smallImg = IplImage.create(grayImg.width()/SCALE,
				grayImg.height()/SCALE, IPL_DEPTH_8U, 1);
		cvResize(grayImg, smallImg, CV_INTER_LINEAR);

		// equalize the small grayscale
		cvEqualizeHist(smallImg, smallImg);

		// create temp storage, used during object detection
		CvMemStorage storage = CvMemStorage.create();

		// instantiate a classifier cascade for face detection
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
				cvLoad(CASCADE_FILE));
		System.out.println("Detecting faces...");
		CvSeq faces = cvHaarDetectObjects(smallImg, cascade, storage, 1.1, 3,
				CV_HAAR_DO_CANNY_PRUNING);
		// CV_HAAR_DO_ROUGH_SEARCH);
		// 0);
		cvClearMemStorage(storage);

		// iterate over the faces and draw yellow rectangles around them
		int total = faces.total();
		System.out.println("Found " + total + " face(s)");
		for (int i = 0; i < total; i++) {
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			cvRectangle(origImg, cvPoint( r.x()*SCALE, r.y()*SCALE ),    // undo the scaling
					cvPoint( (r.x() + r.width())*SCALE, (r.y() + r.height())*SCALE ),
					CvScalar.YELLOW, 6, CV_AA, 0);
			clipSaveFace(origImg, r, "/tmp/", "" + i + ".png");
		}

		final String OUT_FILE = "markedFaces.jpg";
//		if (total > 0) {
//			System.out.println("Saving marked-faces version of " + args[0] + " in " + OUT_FILE);
//			cvSaveImage(OUT_FILE, origImg);
//		}
	}  // end of main()
}
