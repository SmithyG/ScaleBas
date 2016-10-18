import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application
{
    public static DatabaseConnection database;

    public static void main(String args[])
    {       
        JFXPanel panel = new JFXPanel();        
        Platform.runLater(() -> start());               
    }

    private static void start() 
    {
        try
        {         
            database = new DatabaseConnection("ScaleBaseDataBase.db");
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Menu.fxml"));
            Stage stage = new Stage();
            stage.setTitle("ScaleBase");
            stage.setScene(new Scene(loader.load()));
            stage.show();           
            MenuSceneController controller = loader.getController();
            controller.prepareStageEvents(stage);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            terminate();
        }
    }

    public static void terminate()
    {
        System.out.println("Closing database connection and terminating application...");                                

        if (database != null) database.disconnect();
        System.exit(0);
    }

}