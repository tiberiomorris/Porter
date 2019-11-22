package com.andrewfinberg;

import com.andrewfinberg.ImageDetection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by Andrew Finberg 2019/11/17
 * JavaFX App
 */
public class App extends Application {

    /*
    *** Do not change this code ***
    The methods below are used by JavaFX to launch the application
     */

    private static Scene scene;

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(); //This calls the JavaFX inherent launch method to start the JavaFX application
        
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

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();
    }

}
