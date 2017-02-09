import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.FXCollections;

public class DeliveriesSceneController
{
    private Stage stage;
    private MenuSceneController parent;

    @FXML private TableView<Deliveries> deliveriesTable;
    @FXML private Button processButton;
    @FXML private Button cancelButton;

    private Deliveries deliveries;

    public DeliveriesStockController()
    {
        System.out.println("Initialising controllers...");
    }

    public void prepareStageEvents(Stage stage)
    {
        System.out.println("Preparing stage events...");

        this.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Close button was clicked!");
                    stage.close();
                }
            });
    }

    @FXML   void initialize()
    {
        System.out.println("Asserting controls...");
        try
        {   assert processButton != null : "Can't find process button.";
            assert cancelButton != null : "Can't find cancel button.";
            assert deliveriesTable != null : "Can't find deliveries table.";
        }
         catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }

    }
