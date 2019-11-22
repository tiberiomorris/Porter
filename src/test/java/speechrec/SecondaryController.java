package com.speechrec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * Created by Andrew Finberg 2019/11/17
 * This is the class for the Action Screen
 * This class extends PrimaryController for reuse of common methods
 */
public class SecondaryController extends PrimaryController {

    @FXML
    public ImageView photoViewer; //Creating an ImageViewer for the action screen

    @FXML
    public TextArea textArea; //Creating a TextArea FXML item that shows the question details

    @FXML
    private ListView<Question> questionView; //Creating a ListView variable to hold the list of questions

    /**
     * This method initializes the ListView with questions
     */
    public void initialize() {
        Question question1 = new Question("Name", "Visitor name");
        Question question2 = new Question("Purpose", "Reason for visit");

        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);

        questionView.getItems().setAll(questions);
        questionView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * This method is called when the user clicks on a question in the ListView
     * The method should display the details in the TextArea
     */
    @FXML
    public void handleClickListView() {
        Question selectedQuestion = questionView.getSelectionModel().getSelectedItem();
        StringBuilder sb = new StringBuilder(selectedQuestion.getDetails());
        sb.append("\n\n");
        sb.append("Hi, everyone!");
        textArea.setText(sb.toString());
    }

    /**
     * This method should change the current screen to the Welcome Screen
     *
     * @throws IOException throw exception
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
