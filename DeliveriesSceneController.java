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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import java.util.Optional;

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

        TableColumn<Deliveries, Integer> deliveryQuantityColumn = new TableColumn<>("Delivery Quantity");
        deliveryQuantityColumn.setCellValueFactory(new PropertyValueFactory<Deliveries, Integer>("DeliveryQuantity"));
        deliveryQuantityColumn.setMinWidth(25);
        deliveriesTable.getColumns().add(deliveryQuantityColumn);

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

    public void refreshTable()
    {
        System.out.println("Populating scene with refreshed items from the database...");        
        ObservableList<Deliveries> deliveriesList = FXCollections.observableArrayList();
        Deliveries.readAll(deliveriesList);
        deliveriesTable.setItems(deliveriesList);
    }

    @FXML void processClicked()
    {
        System.out.println("Process button clicked!");   
        Deliveries selectedItem = (Deliveries) deliveriesTable.getSelectionModel().getSelectedItem();
        if (selectedItem !=null){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Process Delivery");
            alert.setContentText("Are you sure you want to process " + selectedItem.getDeliveryID());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Deliveries.processDeliveryQuantity(selectedItem.getDeliveryID());
                Deliveries.processDeliveryStatus(selectedItem.getDeliveryID());
                refreshTable();
                parent.refreshTable();
                System.out.println("Delivery " + selectedItem.getDeliveryID() + " processed");
            }
        }
    } 

    @FXML void cancelClicked()
    {
        System.out.println("Cancel clicked");
        stage.close();
    }

    @FXML   void tableViewClicked()
    {
        Deliveries selectedItem = (Deliveries) deliveriesTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            System.out.println("Nothing selected!");
        }
        else
        {
            System.out.println("(id: " + selectedItem.getDeliveryID() + ") is selected.");
        }
    }

    public void setParent(MenuSceneController parent)
    {
        this.parent = parent;
    }
}
