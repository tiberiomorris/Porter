package speechrec;

//Imports the Google Cloud client library
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Translate {

	public Object translation() throws Exception {

		SpeechRecognitionAlternative alternative = null;
		// Instantiates a client
		try (SpeechClient speechClient = SpeechClient.create()) {

			// The path to the audio file to transcribe
			String fileName = "/home/tiberiomorris/Templates/here.wav";

			// Reads the audio file into memory
			Path path = Paths.get(fileName);
			byte[] data = Files.readAllBytes(path);
			ByteString audioBytes = ByteString.copyFrom(data);

			// Builds the sync recognize request
			RecognitionConfig config = RecognitionConfig.newBuilder()
					.setEncoding(AudioEncoding.LINEAR16)
					.setSampleRateHertz(16000)
					.setLanguageCode("en-US")
					.build();
			RecognitionAudio audio = RecognitionAudio.newBuilder()
					.setContent(audioBytes)
					.build();

			// Performs speech recognition on the audio file
			RecognizeResponse response = speechClient.recognize(config, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();

			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given chunk of speech. Just use the
				// first (most likely) one here.
				alternative = result.getAlternativesList().get(0);
			}
		}
		return alternative.getTranscript();
	}

	public String convertToString() throws Exception {
		String convertedToString = String.valueOf(translation());
		return convertedToString;
	}
}