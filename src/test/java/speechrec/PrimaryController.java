package com.speechrec;

import java.io.IOException;

import javafx.fxml.FXML;

/**
 * Created by Andrew Finberg 2019/11/17
 * This is the class for the Welcome screen
 */
public class PrimaryController extends ImageDetection {

    /**
     * This method should call the DetectFace method
     * and switch to the secondary scene
     */
    @FXML
    private void ringBell() throws IOException {
        DetectFace();
        switchToSecondary();

    /**
     * The quitProgram method should quit the program
     * with status code '0'
     */
    public void quitProgram() {
        System.exit(0);
    }
}
