import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
/**
 * This class controls interactivity with JavaFX
 * 
 * @George Smith
 * @Alpha
 */
public class MenuSceneController
{  
    private static Stage stage;

    @FXML private Button warehouseButton;
    @FXML private Button deliveriesButton;
    @FXML private Button chartsButton;
    @FXML private TableView inventoryTable;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn quantityColumn;
 

    public MenuSceneController()
    {
        System.out.println("Initialising controllers...");

        if (stage != null)
        {
            System.out.println("Error, duplicate controller - terminating application!");
            System.exit(-1);
        }
    }

    @FXML   void initialize()
    {
        System.out.println("Asserting controls...");
        assert warehouseButton != null : "Can't find warehouse visualisation button.";
        assert deliveriesButton != null : "Can't find deliveries button.";
        assert chartsButton != null : "Can't find charts button.";
        assert inventoryTable != null : "Can't find inventory table.";
        assert idColumn != null : "Can't find ID column.";
        assert nameColumn != null : "Can't find name column.";
        assert quantityColumn != null : "Can't find quantity column.";
    }

    public void prepareStageEvents(Stage stage)
    {
        System.out.println("Preparing stage events...");

        this.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Close button was clicked!");
                    Application.terminate();
                }
            });
    }

    @FXML void warehouseClicked()
    {
        System.out.println("Warehouse Clicked!");
    }

    @FXML void deliveriesClicked()
    {
        System.out.println("Pending Deliveries Clicked");
    }
    
    @FXML void chartsClicked()
    {
        System.out.println("Charts clicked");
    }

}
