package View;

import Server.Configurations;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class WonController {
    public javafx.scene.image.ImageView Char1;
    public javafx.scene.control.Button New;
    public javafx.scene.control.Button Exit;
    public javafx.scene.control.Button Again;
    public javafx.scene.control.Label lbl;
    public String Action="None";

    /**
     * this function won - handle when the user have won the game
     */
    public void won(){
        File file = new File("resources/Images/won.jpg");
        String temp = Configurations.getProperty("Player");
        if (temp != null) {
            if (temp.equals("Ash")) {
                file = new File("resources/Images/won.jpg");
            }
        }
        Image player = new Image(file.toURI().toString());
        lbl.setText("You Won!");
        Char1.setImage(player);
    }


    /**
     * these functions handle what the user chose after he win
     */
    public void Exit(){
        Action= "Exit";
        Stage stage = (Stage)lbl.getScene().getWindow();
        stage.close();
        return;
    }

    public void New(){
        Action="New";
        Stage stage = (Stage)lbl.getScene().getWindow();
        stage.close();
    }

    public void Again(){
        Action="Again";
        Stage stage = (Stage)lbl.getScene().getWindow();
        stage.close();
    }
}
