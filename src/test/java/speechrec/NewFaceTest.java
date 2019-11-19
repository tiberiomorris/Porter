package speechrec;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

/**
 * This class is to call face++ api and send the images to compare and get the
 * result
 * 
 * @author: Saad Yousafi
 **/
public class NewFaceTest {

	private final static int CONNECT_TIME_OUT = 30000; // creating final variable and initializing
	private final static int READ_OUT_TIME = 50000; // creating final variable and initializing
	private static String boundaryString = getBoundary(); // creating private variable and initializing
	private static double response; // creating private variable and initializing

	/**
	 * This method will make a http request to face++ api, send the images to
	 * compare and get the result
	 * 
	 * @param receves
	 *            the address of the image to be compared with
	 **/
	public static void compare(String str1) throws Exception {

		// object of File class with the address path as parameter
		File file = new File("/home/tiberiomorris/Pictures/Porter/imageTaken.jpg");

		// object of File class with the variable as parameter
		File file1 = new File(str1);

		byte[] buff = getBytesFromFile(file);
		byte[] buff1 = getBytesFromFile(file1);

		// URL for making http request
		String url = "https://api-us.faceplusplus.com/facepp/v3/compare";
		// creating and initializing hashmap
		HashMap<String, String> map = new HashMap<>();
		// creating and initializing hashmap
		HashMap<String, byte[]> byteMap = new HashMap<>();
		// adding the key and value
		map.put("api_key", "jUMMcc9NHK3XfI3V3g9OmP8IwtWK2PY0");
		// adding the key and value
		map.put("api_secret", "oVv2mv8J9XqqqHI-vAuEaLy3Htj33YwQ");
		// adding the key and value
		byteMap.put("image_file1", buff);
		// adding the key and value
		byteMap.put("image_file2", buff1);

		try {
			// array of type byte storing the values from http request to bacd variable
			byte[] bacd = post(url, map, byteMap);
			// converting to string
			String str = new String(bacd);
			// System.out.println("this is the result " + str);

			// storing the value into the array of String with split method to extract the
			// value
			String[] words = str.split("[{}:\",]");
			// for loop to iterate words array
			for (int i = 0; i < words.length; i++) {
				// checking if element at i in array is equal to the expected value
				if (words[i].equalsIgnoreCase("confidence")) {
					// extracting the value and parcing it into double
					double d = Double.parseDouble(words[i + 2]);
					// setting the value of response equal to variable d
					response = d;
				}
			}

			// to print the extracted response
			// System.out.println(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap)
			throws Exception {
		HttpURLConnection conne;
		URL url1 = new URL(url);
		conne = (HttpURLConnection) url1.openConnection();
		conne.setDoOutput(true);
		conne.setUseCaches(false);
		conne.setRequestMethod("POST");
		conne.setConnectTimeout(CONNECT_TIME_OUT);
		conne.setReadTimeout(READ_OUT_TIME);
		conne.setRequestProperty("accept", "*/*");
		conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
		conne.setRequestProperty("connection", "Keep-Alive");
		conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
		DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry) iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			obos.writeBytes("--" + boundaryString + "\r\n");
			obos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
			obos.writeBytes("\r\n");
			obos.writeBytes(value + "\r\n");
		}
		if (fileMap != null && fileMap.size() > 0) {
			Iterator fileIter = fileMap.entrySet().iterator();
			while (fileIter.hasNext()) {
				Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
				obos.writeBytes("--" + boundaryString + "\r\n");
				obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey() + "\"; filename=\""
						+ encode(" ") + "\"\r\n");
				obos.writeBytes("\r\n");
				obos.write(fileEntry.getValue());
				obos.writeBytes("\r\n");
			}
		}
		obos.writeBytes("--" + boundaryString + "--" + "\r\n");
		obos.writeBytes("\r\n");
		obos.flush();
		obos.close();
		InputStream ins = null;
		int code = conne.getResponseCode();

		try {
			if (code == 200) {
				ins = conne.getInputStream();
			} else {
				ins = conne.getErrorStream();
			}
		} catch (SSLException e) {
			e.printStackTrace();
			return new byte[0];
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buff = new byte[4096];
		int len;
		while ((len = ins.read(buff)) != -1) {
			baos.write(buff, 0, len);
		}
		byte[] bytes = baos.toByteArray();
		ins.close();
		return bytes;
	}

	private static String getBoundary() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 32; ++i) {
			sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(
					random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
		}
		return sb.toString();
	}

	private static String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * this method will return true if match response of two images is more than 70
	 * else return false
	 * 
	 * @return true if response is greater than 70 else return false
	 */
	public boolean getResult() {
		// double variable res, setting it equal to response
		double res = response;
		// boolean variable istrue initializing to false
		boolean istrue = false;

		// to check if res value is greater or equal to 70 then set the istrue value to
		// true else fase
		if (res >= 70) {
			istrue = true;
		} else {
			istrue = false;
		}

		// returning the value of istrue variable
		return istrue;
	}

}
