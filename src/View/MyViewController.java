package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Observer, IView, Initializable {

    @FXML
    private MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public ChooseController chooseController;
    public WonController wonController;
    public PropertiesController propertiesController;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.MenuItem mi_Save;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.ChoiceBox Level;
    public javafx.scene.control.Button btn_hint;
    public javafx.scene.layout.Pane MazePane;
    public javafx.scene.control.Button btn_music;
    public MediaPlayer mediaPlayer;
    public MediaPlayer finishMusic;
    public javafx.scene.layout.BorderPane borderPane;
    public javafx.scene.control.ScrollPane scroll;
    public static Stage PrimaryStage;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void setMainStage (Stage stage) {this.PrimaryStage = stage;}
    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
            displaySol(viewModel.getSol());
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Media music = new Media(new File("resources/music/Song.mp3").toURI().toString());
        mediaPlayer= new MediaPlayer(music);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.setAutoPlay(true);
        adjustDisplaySize();
    }

    private void adjustDisplaySize() {
        borderPane.widthProperty().addListener(e -> {
            MazePane.setMinWidth(borderPane.getWidth()*0.75);
                mazeDisplayer.redraw();
        });
        borderPane.heightProperty().addListener(e -> {
            MazePane.setMinHeight(borderPane.getHeight()*0.75);
                mazeDisplayer.redraw();
        });
        borderPane.widthProperty().addListener(e -> {
            mazeDisplayer.setWidth(borderPane.getWidth()*0.75);
            scroll.setPrefWidth(borderPane.getWidth()*0.78);
                mazeDisplayer.redraw();
        });
        borderPane.heightProperty().addListener(e -> {
            mazeDisplayer.setHeight(borderPane.getHeight()*0.75);
            scroll.setPrefHeight(borderPane.getHeight()*0.78);
                mazeDisplayer.redraw();
        });
    }

    /**
     * this function displayMaze
     * @param maze -the maze we want to display
     */
    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze); //set the maze
        int characterPositionRow = viewModel.getCharacterPositionRow(); //update the info from the new maze
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);

    }

    /**
     * this function displaysol
     * @param sol -the solution we want to display
     */
    public  void  displaySol(Solution sol){
        mazeDisplayer.setSol(sol);  //set the sol
    }

    /**
     * this function generate maze
     */
    public void generateMaze() {
        int heigth=0,width=0;
        try {
            heigth = Integer.parseInt(txtfld_rowsNum.getText()); //get the info(heigth and row) from the user
            width = Integer.parseInt(txtfld_columnsNum.getText());
            if (heigth >= 2 && heigth <= 500 && width >= 2 && width <= 500) { //check that the info is ok
                ChooseChar(); //send him to choose is character
                Setbtn(); //change the visible of some buttons
                viewModel.generateMaze(width, heigth,Level.getValue().toString()); //generate the maze at the model
                mi_Save.setDisable(false); //now the user can save the maze
            } else {
                showAlert("The maze size have to be between 2*2 to 500*500");
            }

        }
        catch (NumberFormatException E1) {
            showAlert("Height and Width have to be numbers");
        }
    }
    /**
     * this function Setbtn- change the setting of the buttons to maze displayer buttons
     */
    public void Setbtn(){
        btn_generateMaze.setVisible(false);
        txtfld_columnsNum.setVisible(false);
        txtfld_rowsNum.setVisible(false);
        lbl_columnsNum.setVisible(false);
        lbl_rowsNum.setVisible(false);
        btn_solveMaze.setVisible(true);
        mazeDisplayer.setVisible(true);
        Level.setVisible(false);
        btn_hint.setVisible(true);
    }

    /**
     * this function ChooseChar- open new stage- the user choose hes character
     */
    public void ChooseChar() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Choose Character");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("ChooseChar.fxml").openStream());
            chooseController=fxmlLoader.getController();
            Scene scene = new Scene(root, 400, 350);
            scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            chooseController.choose();
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("error 5");
        }
    }

    /**
     * this function solveMaze
     */
    public void solveMaze() {
        if(btn_solveMaze.getText().equals("Solution")) //if the solution is hidden
        {
            mazeDisplayer.setShowSOl(true);
            btn_solveMaze.setText("No Solution");
        }
        else //hide the solution
        {
            mazeDisplayer.setShowSOl(false);
            btn_solveMaze.setText("Solution");
        }
        viewModel.solveMaze(viewModel.getMaze()); //send to ViewModel to solve the maze
    }

    /**
     * this function hint - give only a hint from the solution
     */
    public void hint() {
        if(btn_hint.getText().equals("Hint")) //if the hint is hidden
            mazeDisplayer.setShowhint(true);
        viewModel.solveMaze(viewModel.getMaze()); //send to ViewModel to solve the maze
        mazeDisplayer.setShowhint(false);
    }

    /**
     * this function ShowAlert - open new alert
     *
     */

    /**
     * this function ShowAlert - open new alert
     * @param alertMessage- the message on the alert
     */
    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    /**
     * this function KeyPressed - handle the key event
     * @param keyEvent
     */
    public void KeyPressed(KeyEvent keyEvent) throws IOException {
        if(!(btn_generateMaze.isVisible())) {
            viewModel.moveCharacter(keyEvent.getCode()); //sent to ViewModel to move the character acorrding
            if (btn_solveMaze.getText().equals("No Solution")) { //if sol is show update
                viewModel.solveMaze(viewModel.getMaze());
            }
            keyEvent.consume();
            //handle lose and win
            if (viewModel.getCharacterPositionRow() == viewModel.getMaze().getGoalPosition().getRowIndex() + 1 && viewModel.getCharacterPositionColumn() ==
                    viewModel.getMaze().getGoalPosition().getColumnIndex() + 1)
            {
                Media Temp = mediaPlayer.getMedia();
                try {
                    mediaPlayer.pause(); //stop the main music
                    Stage stage = new Stage();
                    stage.setTitle("Finish!");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Parent root = fxmlLoader.load(getClass().getResource("Won.fxml").openStream());
                    wonController = fxmlLoader.getController();
                    Scene scene = new Scene(root, 400, 350);
                    scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                    Media music = new Media(new File("resources/music/WinSong.mp3").toURI().toString());
                    if (viewModel.getCharacterPositionRow() == viewModel.getMaze().getGoalPosition().getRowIndex() + 1 && viewModel.getCharacterPositionColumn() == viewModel.getMaze().getGoalPosition().getColumnIndex() + 1) {
                        wonController.won();
                   }
                    finishMusic = new MediaPlayer(music);
                    finishMusic.setAutoPlay(true);
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("error 6");
                }
                if (btn_music.getText().equals("No Music")) //if the music is on keep play
                    mediaPlayer.play();
                if (wonController.Action.equals("New"))
                    generateMaze();
                if (wonController.Action.equals("Again"))
                    Again();
            }
        }
    }

    /**
     * this function zoomIn - handle the scroll event
     * @param scrollevent
     */
    public void zoomIn(ScrollEvent scrollevent) {
        if(scrollevent.isControlDown() && !(btn_generateMaze.isVisible())) {
            double zoomFactor = 1.05;
            double deltaY = scrollevent.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 1/zoomFactor;
            }
            if(mazeDisplayer.getWidth()*zoomFactor>4000 || mazeDisplayer.getHeight()*zoomFactor>4000){
                mazeDisplayer.setHeight(4000);
                mazeDisplayer.setWidth(4000);
                mazeDisplayer.redraw();
                scrollevent.consume();
                return;
            }
            mazeDisplayer.setWidth(mazeDisplayer.getWidth()*zoomFactor);
            mazeDisplayer.setHeight(mazeDisplayer.getHeight()*zoomFactor);
            mazeDisplayer.redraw();
            scrollevent.consume();
        }
    }

    /**
     * this function Music - handle the btn of play/close music
     */
    public void Music(){
        if(btn_music.getText().equals("No Music")) //close music
        {
            mediaPlayer.pause();
            btn_music.setText("Music");
        }
        else //open music
        {
            mediaPlayer.play();
            btn_music.setText("No Music");
        }
    }

    /**
     * this function Again - open again the same maze
     */
    public void Again(){
        viewModel.SameMaze(); //send to viewModel to open the same maze
        if(btn_solveMaze.getText().equals("No Solution")) { //if solution is shown
            mazeDisplayer.setShowSOl(false); //close the option to see the sol
            btn_solveMaze.setText("Solution");
        }
    }

    /**
     * this function New - open new scene
     */
    public void New()throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene2 = new Scene(root,800,700);
        scene2.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
        PrimaryStage.setScene(scene2);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        view.setResizeEvent(scene2);
        viewModel.addObserver(view);
        mediaPlayer.stop(); //stop the music from this scene
        PrimaryStage.show();

    }

    
    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
            }
        });
    }
    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }


    //handle the menu buttons
    /**
     * this function NewMaze - handle new from the menu
     * @param  actionEvent
     */
    public void NewMaze(ActionEvent actionEvent) throws IOException {
        New(); //send to function that open new scene
    }

    /**
     * this function About - handle about from the menu
     * @param actionEvent
     */
    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("AboutController");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            scene.getStylesheets().add(getClass().getResource("Prop.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            System.out.println("error 7");
        }

    }

    /**
     * this function SaveMaze - handle save from the menu
     * @param actionEvent
     */
    public void SaveMaze(ActionEvent actionEvent){
        //open the dialog to choose file
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(PrimaryStage);
        boolean ok=viewModel.SaveMaze(file); //send to viewModel to save the maze
        if(ok==false) //status ok if the maze is saved
        {
            showAlert("can't save this maze");
        }
    }


    public void exitProgram(ActionEvent actionEvent){
        exit();
    }

    public void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            // Close program
            alert.close();
            PrimaryStage.close();
            Platform.exit();
        } else {
            alert.close();
        }
    }

    /**
     * this function OpenMaze - handle load from the menu
     * @param actionEvent
     */
    public void OpenMaze(ActionEvent actionEvent){
        //open the dialog to choose file
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(PrimaryStage);
        boolean ok=viewModel.OpenMaze(file);
        if(ok==false) //status ok if can open the maze
        {
            showAlert("You didn't choose maze");
        }
        else {
            Setbtn(); //change the buttons to maze displayer buttons
            mi_Save.setDisable(false); //open the option to save the maze
            if(btn_solveMaze.getText().equals("No Solution"))
                btn_solveMaze.setText("Solution");
        }
    }

    /**
     * this function ShowProperties - handle properties from the menu
     * @param actionEvent
     */
    public void ShowProperties(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            scene.getStylesheets().add(getClass().getResource("Prop.css").toExternalForm());
            propertiesController=fxmlLoader.getController();
            propertiesController.setViewModel(viewModel);
            propertiesController.Prop();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();

        } catch (Exception e) {
            System.out.println("error 8");
        }
    }

    /**
     * this function help - handle help from the menu
     * @param actionEvent
     */
    public void help(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            scene.getStylesheets().add(getClass().getResource("Prop.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }
}
