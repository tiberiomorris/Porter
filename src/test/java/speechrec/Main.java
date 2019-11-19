package speechrec;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
        
        /*speechrec.TextToSpeech speechText = new speechrec.TextToSpeech();
        speechText.setFilePath();
        String greeting = speechText.greeting();
        speechText.run(greeting);
        ComputerSpeech player = new ComputerSpeech();
        player.play(speechText.returnFilePath());

        speechrec.Record recorder = new speechrec.Record();
        recorder.beginRecord(recorder);
        Translate test = new Translate();
        test.translation();
        String name = test.convertToString();
        System.out.println(name);

        String response = speechText.visitReason(name);
        speechText.run(response);
        ComputerSpeech player2 = new ComputerSpeech();
        player2.play(speechText.returnFilePath());*/
		
		ImageDetection imgDet = new ImageDetection();

		imgDet.DetectFace();
		System.out.println(imgDet.isFaceDetected());
		if (imgDet.isFaceDetected()) {
			imgDet.takeImage();

			NewFaceTest nft = new NewFaceTest();
			try {
				String str = "/home/tiberiomorris/Pictures/Porter2/";
				File file = new File(str);
				File[] files = file.listFiles();
				for (File f : files) {
					NewFaceTest.compare(str + f.getName());
					if (nft.getResult() == true) {
						System.out.println(f.getName());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
