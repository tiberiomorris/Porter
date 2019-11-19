package speechrec;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class ImageDetectionTest {
	private String directory = "C:\\Users\\Saad\\eclipse-workspace\\CollegeJava\\takenImage\\";
	private BufferedImage image;
	private int isDetected;

	@Test
	public void detectFaceTest() {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		CascadeClassifier cascadeClassifier = new CascadeClassifier(
				"C:\\Users\\Saad\\Documents\\Downloads\\facedetect\\haarcascade_frontalface_alt.xml");
		MatOfRect detectedFaces = new MatOfRect();
		Mat coloredImage = new Mat();
		Mat greyImage = new Mat();
		boolean isTrue = true;
		Mat webcamImage = new Mat();

		VideoCapture videoCapture = new VideoCapture(0);

		if (videoCapture.isOpened()) {

			while (isTrue) {

				videoCapture.read(webcamImage);

				if (!webcamImage.empty()) {

					webcamImage.copyTo(coloredImage);
					webcamImage.copyTo(greyImage);

					Imgproc.cvtColor(coloredImage, greyImage, Imgproc.COLOR_BGR2GRAY);
					Imgproc.equalizeHist(greyImage, greyImage);

					cascadeClassifier.detectMultiScale(greyImage, detectedFaces);

					int width = webcamImage.width(), height = webcamImage.height(), channels = webcamImage.channels();
					byte[] sourcePixels = new byte[width * height * channels];
					webcamImage.get(0, 0, sourcePixels);

					image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
					final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
					System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

					int accumulator = detectedFaces.toArray().length;

					if (accumulator > 0) {
						this.isDetected = 1;
						isTrue = false;
					} else {
						this.isDetected = 0;
					}
					System.out.println(this.isDetected);
					assertEquals(1, this.isDetected, "it should be 1 after detecting the face.");

				} else {
					System.out.println("Problem");
					break;
				}
				videoCapture.release();

			}
		}
	}

	@Test
	public void isFaceDetectedTest() {
		int isDetected = 1;
		boolean faceNumber = false;
		if(isDetected == 0) {
			faceNumber = false;
		}else if(isDetected >=1) {
			faceNumber = true;
		}

		assertEquals(true, faceNumber, "it should return true if face is detected.");
	}

	@Test
	public void takeImageTest() {
		Mat webcamImage = new Mat();
		VideoCapture videoCapture = new VideoCapture(0);

		if (videoCapture.isOpened()) {

			if (videoCapture.read(webcamImage)) {
				BufferedImage image = new BufferedImage(webcamImage.width(), webcamImage.height(),
						BufferedImage.TYPE_3BYTE_BGR);

				WritableRaster raster = image.getRaster();
				DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
				byte[] data = dataBuffer.getData();
				webcamImage.get(0, 0, data);
				WritableImage writeImage = SwingFXUtils.toFXImage(image, null);

				String file = directory+"imageTaken.jpg";
				Imgcodecs.imwrite(file, webcamImage);

				videoCapture.release();
				File file1 = new File(directory);
				File[] fArray = file1.listFiles();
				assertEquals(1, fArray.length, "after image capturing and storing, fArray should have the length of 1");
			}
		}
	}

	@Test
	public void showTakenImageTest() {
		File file = new File(directory+"imageTaken.jpg");
		File file1 = new File(directory);
		File[] fArray = file1.listFiles();
		String name = fArray[0].getName();
		assertEquals(directory+"imageTaken.jpg", name , "stored image address should match");
	}
}
