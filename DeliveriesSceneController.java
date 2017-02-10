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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

public class DeliveriesSceneController
{
    private Stage stage;
    private MenuSceneController parent;

    @FXML private TableView<Deliveries> deliveriesTable;
    @FXML private Button processButton;
    @FXML private Button cancelButton;

    private Deliveries deliveries;

    public DeliveriesSceneController()
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

        System.out.println("Populating scene with items from the database...");        
        ObservableList<Deliveries> deliveriesList = FXCollections.observableArrayList();
        Deliveries.readAll(deliveriesList);

        TableColumn<Deliveries, Integer> deliveryIDColumn = new TableColumn<>("Delivery ID");
        deliveryIDColumn.setCellValueFactory(new PropertyValueFactory<Deliveries, Integer>("DeliveryID"));
        deliveryIDColumn.setMinWidth(25);
        deliveriesTable.getColumns().add(deliveryIDColumn);

        TableColumn<Deliveries, Integer> productIDColumn = new TableColumn<>("Product ID");
        productIDColumn.setCellValueFactory(new PropertyValueFactory<Deliveries, Integer>("ProductID"));
        productIDColumn.setMinWidth(25);
        deliveriesTable.getColumns().add(productIDColumn);

        TableColumn<Deliveries, String> deliveryDateColumn = new TableColumn<>("Delivery Date");
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<Deliveries, String>("DeliveryDate"));
        deliveryDateColumn.setMinWidth(25);
        deliveriesTable.getColumns().add(deliveryDateColumn);

        TableColumn<Deliveries, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Deliveries, String>("ProductName"));
        productNameColumn.setMinWidth(25);
        deliveriesTable.getColumns().add(productNameColumn);

        deliveriesTable.setItems(deliveriesList);

    }
    
    @FXML void processClicked()
    {
        System.out.println("Process clicked");
    }
    
    @FXML void cancelClicked()
    {
        System.out.println("Cancel clicked");
        stage.close();
    }
    
        public void setParent(MenuSceneController parent)
    {
        this.parent = parent;
    }
}
