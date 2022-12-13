package com.example.fileseeker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.IOException;

public class Controller {
    private Stage stage;
    private Scene scene;
    private boolean folderChosen = false;
    private boolean correctExt = false;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    public TextField extTextField;
    @FXML
    public TextArea resultTextArea;
    public void switchToResultScene(ActionEvent event) throws IOException, InterruptedException {
        if (!folderChosen){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please choose folder");
            alert.showAndWait();
            return;
        }
        String ext = extTextField.getText();
        if (ext.startsWith(".")) correctExt = true;
        if (!correctExt){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please type correct ext");
            alert.showAndWait();
            return;
        }
        FileSeeker fs = new FileSeeker(FileSeeker.folder.getPath(), ext);

        Thread thread = new Thread(fs);
        thread.start();
        thread.join();

        Parent root = FXMLLoader.load(getClass().getResource("Result.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void chooseFolder(){
        FileSeeker.folder = directoryChooser.showDialog(new Stage());
        if (FileSeeker.folder != null) folderChosen = true;
    }

    public void setData(){
        String fieldContent = "";
        for (String key : FileSeeker.searchResult.keySet()){
            fieldContent += "\n" + key + "\n";
            for (String val : FileSeeker.searchResult.get(key)){
                fieldContent += val + "\n";
            }
        }
        resultTextArea.setText(fieldContent);
    }
}
