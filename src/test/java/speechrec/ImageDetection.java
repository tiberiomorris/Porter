package speechrec;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

/**
 * This class is created to access the webcam, detect the face and then take the
 * image and store it in the project level folder
 *
 * @author Saad Yousafi
 */
public class ImageDetection {

	// address for the folder where image will be stored
	private String directory = "/home/tiberiomorris/Pictures/Porter/";

	// initialization of CascadeClassifier class
	private CascadeClassifier cascadeClassifier;

	// initialization of MatOfRect class
	private MatOfRect detectedFaces;

	// initialization of Mat class with coloredImage variable
	private Mat coloredImage;

	// initialization of Mat class with greyImage variable
	private Mat greyImage;

	// initialization of BufferedImage class with image variable
	private BufferedImage image;

	// int variable to store 1 if camera detects the face
	private int isDetected;

	// boolean value isTrue to use as accumulator
	private boolean isTrue = true;

	// constructor with initialization of classes in the body
	public ImageDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.detectedFaces = new MatOfRect();
		this.coloredImage = new Mat();
		this.greyImage = new Mat();
		this.cascadeClassifier = new CascadeClassifier(
				"/home/tiberiomorris/Vision/opencv/data/haarcascades/haarcascade_frontalface_alt.xml");
	}

	/**
	 * This method will access the camera and set the value in isDetected variable
	 * upon detecting face
	 */
	public void DetectFace() {

		// creating object of Mat class with webcameImage variable
		Mat webcamImage = new Mat();

		// creating object of VideoCapture class with videoCapture variable
		VideoCapture videoCapture = new VideoCapture(0);

		// logical condition to check if vedioCapture is open
		if (videoCapture.isOpened()) {

			// while loop to iterate till it detects the face
			while (isTrue) {

				videoCapture.read(webcamImage);

				// logical condition to check if webcamImage is not empty
				if (!webcamImage.empty()) {

					webcamImage.copyTo(coloredImage); // copying coloredImage reference variable to webcamImage
					webcamImage.copyTo(greyImage); // copying greyImage reference variable to webcameImage

					Imgproc.cvtColor(coloredImage, greyImage, Imgproc.COLOR_BGR2GRAY);
					Imgproc.equalizeHist(greyImage, greyImage);

					// calling detectMultiScale method of cascadeClassifier class to detect face
					cascadeClassifier.detectMultiScale(greyImage, detectedFaces);

					// this creates rectangle around detected faces
					int width = webcamImage.width(), height = webcamImage.height(), channels = webcamImage.channels();
					byte[] sourcePixels = new byte[width * height * channels];
					webcamImage.get(0, 0, sourcePixels);

					image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
					final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
					System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

					// detectedFAces.toArray().length with store the number of faces detected in
					// accumulator variable
					int accumulator = detectedFaces.toArray().length;

					// this will set the value of isDetected variable to 1 if it detected faces
					if (accumulator > 0) {
						this.isDetected = 1;
						isTrue = false;
					} else {
						this.isDetected = 0;
					}

					// this will print 1 in the conlose if face is detected and 0 if not
					System.out.println("Detected faces: " + this.isDetected);

				} else {
					System.out.println("Problem");
					break;
				}
				videoCapture.release();
			}
		}
	}

	/**
	 * this method will return true or false based on the value of isDetected
	 * variable
	 *
	 * @return true if isDetected = 1 else false
	 */
	public boolean isFaceDetected() {

		// creating boolean variable and initializating it to false
		boolean faceNumber = false;

		// checking if isDetected variable's value is 0 or 1
		if (this.isDetected == 0) {
			faceNumber = false;
		} else if (this.isDetected >= 1) {
			faceNumber = true;
		}

		// returning true if isDetected is equal to 1 else returning false
		return faceNumber;
	}

	/**
	 * this method is to capture and store the image
	 */
	public void takeImage() {

		// creating object of Mat class with webcamImage variable
		Mat webcamImage = new Mat();

		// creating object of VideoCapute class with videoCapture variable
		VideoCapture videoCapture = new VideoCapture(0);

		// checking if camera is open
		if (videoCapture.isOpened()) {

			// checking if camera is reading images
			if (videoCapture.read(webcamImage)) {
				BufferedImage image = new BufferedImage(webcamImage.width(), webcamImage.height(),
						BufferedImage.TYPE_3BYTE_BGR);

				WritableRaster raster = image.getRaster();
				DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
				byte[] data = dataBuffer.getData();
				webcamImage.get(0, 0, data);
				WritableImage writeImage = SwingFXUtils.toFXImage(image, null);

				String file = directory + "imageTaken.jpg";
				Imgcodecs.imwrite(file, webcamImage);

				videoCapture.release();
			}
		}
	}

	/**
	 * this method will return the address of taken image.
	 *
	 * @return address of taken image
	 */
	public File showTakenImage() {
		File file = new File(directory + "imageTaken.jpg");
		return file;
	}

}
