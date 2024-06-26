package View;

import Server.Configurations;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;



public class ChooseController {
    public javafx.scene.image.ImageView Char1;
    public javafx.scene.image.ImageView Char2;
    public javafx.scene.control.Button Brook;
    public javafx.scene.control.Button Ash;

    /**
     * this function Choose - set the images of the players
     */
    public void choose(){
        File file = new File("resources/Images/player.png");
        Image player = new Image(file.toURI().toString());
        Char1.setImage(player);
        file = new File("resources/Images/player1.png");
        player = new Image(file.toURI().toString());
        Char2.setImage(player);
    }

    public void PlayAsh(){
        Configurations.setProperty("Player","Ash"); //update the config file
        Stage stage = (Stage) Ash.getScene().getWindow();
        stage.close(); //close the stage
    }


    public void PlayBrook(){
        Configurations.setProperty("Player","Brook"); //update the config file
        Stage stage = (Stage) Brook.getScene().getWindow();
        stage.close(); //close the stage
    }


}
