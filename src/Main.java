import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    MyViewController view;

    @Override
    public void start(Stage primaryStage) throws Exception{

        MyModel myModel = new MyModel();
        MyViewModel myViewModel = new MyViewModel(myModel);
        myModel.addObserver(myViewModel);

        primaryStage.setTitle("My Application!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View/MyView.fxml").openStream());
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("View/View.css").toExternalForm());
        primaryStage.setScene(scene);

        view = fxmlLoader.getController();
        view.setViewModel(myViewModel);
        view.setMainStage(primaryStage);
        myViewModel.addObserver(view);
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }
    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                view.exit();
                windowEvent.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
