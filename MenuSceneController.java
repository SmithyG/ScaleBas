import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * This class controls interactivity with JavaFX
 * 
 * @George Smith
 * @Alpha
 */
public class MenuSceneController
{  
    private static Stage stage;

    @FXML private HBox backgroundHBox;
    @FXML private VBox tableVBox;
    @FXML private VBox backgroundVBox;
    @FXML private Button warehouseButton;
    @FXML private Button deliveriesButton;
    @FXML private Button chartsButton;
    @FXML private TableView<Productmaster> inventoryTable;

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
        try
        {
            assert warehouseButton != null : "Can't find warehouse visualisation button.";
            assert deliveriesButton != null : "Can't find deliveries button.";
            assert chartsButton != null : "Can't find charts button.";
            assert inventoryTable != null : "Can't find inventory table.";
            assert backgroundHBox != null : "Can't find backgroundHBox.";
            assert tableVBox != null : "Can't find tableVBox.";
            assert backgroundVBox != null : "Can't find backgroundVBox.";
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }
        
        System.out.println("Populating scene with items from the database...");        

        ObservableList<Productmaster> productList = FXCollections.observableArrayList();
       Productmaster.readAll(productList);             
        
       TableColumn<Productmaster, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Productmaster, String>("ProductName"));
        productNameColumn.setMinWidth(150);
        inventoryTable.getColumns().add(productNameColumn);
        
       TableColumn<Productmaster, Double> productPriceColumn = new TableColumn<>("Product Price");
       productPriceColumn.setCellValueFactory(new PropertyValueFactory<Productmaster, Double>("ProductPrice"));
       productPriceColumn.setMinWidth(150);
       inventoryTable.getColumns().add(productPriceColumn);

        
        inventoryTable.setItems(productList);

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
