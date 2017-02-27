import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import java.util.Iterator;

public class MenuSceneController
{  
    private static Stage stage;
    @FXML private Button refreshButton;
    @FXML private Button deliveriesButton;
    @FXML private TableView<StockInformation> inventoryTable;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    public MenuSceneController()
    {
        System.out.println("Initialising controllers...");

        if (stage != null) //Closes application if duplicate controller exists
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
            assert refreshButton != null : "Can't find warehouse visualisation button.";
            assert deliveriesButton != null : "Can't find deliveries button.";
            assert inventoryTable != null : "Can't find inventory table.";
            assert addButton != null : "Can't find add button.";
            assert editButton != null : "Can't find edit button.";
            assert deleteButton != null : "Can't find delete button."; //Assertions check to see if all interactive components have loaded
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate(); //If not, the application terminates
        }

        System.out.println("Populating scene with items from the database...");        
        ObservableList<StockInformation> productList = FXCollections.observableArrayList();
        StockInformation.readAll(productList);

        TableColumn<StockInformation, Integer> productIDColumn = new TableColumn<>("Product ID");
        productIDColumn.setCellValueFactory(new PropertyValueFactory<StockInformation, Integer>("ProductID"));
        productIDColumn.setMinWidth(25);
        inventoryTable.getColumns().add(productIDColumn);

        TableColumn<StockInformation, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<StockInformation, String>("ProductName"));
        productNameColumn.setMinWidth(25);
        inventoryTable.getColumns().add(productNameColumn);

        TableColumn<StockInformation, String> productPriceColumn = new TableColumn<>("Product Price");
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<StockInformation, String>("ProductPriceString"));
        productPriceColumn.setMinWidth(25);
        inventoryTable.getColumns().add(productPriceColumn);

        TableColumn<StockInformation, String> productLocationIDColumn = new TableColumn<>("Product Location ID");
        productLocationIDColumn.setCellValueFactory(new PropertyValueFactory<StockInformation, String>("LocationID"));
        productLocationIDColumn.setMinWidth(25);
        inventoryTable.getColumns().add(productLocationIDColumn);

        TableColumn<StockInformation, String> productLocationNameColumn = new TableColumn<>("Product Location Name");
        productLocationNameColumn.setCellValueFactory(new PropertyValueFactory<StockInformation, String>("LocationName"));
        productLocationNameColumn.setMinWidth(25);
        inventoryTable.getColumns().add(productLocationNameColumn);
        
        TableColumn<StockInformation, String> productQuantityColumn = new TableColumn<>("Product Quantity");
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<StockInformation, String>("ProductQuantityString"));
        productQuantityColumn.setMinWidth(25);
        inventoryTable.getColumns().add(productQuantityColumn); //Each column for the table view is created and populated

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

    public void refreshTable(){
        System.out.println("Populating scene with refreshed items from the database...");        
        ObservableList<StockInformation> productList = FXCollections.observableArrayList();
        StockInformation.readAll(productList); //The list is read again via the readAll method
        inventoryTable.setItems(productList); //The table is repopulated with updated information
    }

    @FXML   void tableViewClicked()
    {
        StockInformation selectedItem = (StockInformation) inventoryTable.getSelectionModel().getSelectedItem(); //The selected item ID is found

        if (selectedItem == null)
        {
            System.out.println("Nothing selected!");
        }
        else
        {
            System.out.println("(id: " + selectedItem.getProductID() + ") is selected.");
        }
    }

    @FXML void refreshClicked()
    {
        refreshTable(); //refreshTable method is called when the refresh button is clicked.
    }

    @FXML void deliveriesClicked()
    {
        openNewScene(-1); //Int -1 is passed to the openNewScene method when deliveries button is clicked
    }

    @FXML void addClicked()
    {
        openNewScene(0); //Int 0 is passed to the openNewScene method when add button is clicked
    }

    @FXML void editClicked()
    {
        System.out.println("Edit Clicked");
        StockInformation selectedItem = (StockInformation) inventoryTable.getSelectionModel().getSelectedItem(); //Selected Item ID is found
        if (selectedItem == null){
            System.out.println("Please select an item to be edited");
        }else
        openNewScene(selectedItem.getProductID()); //And is passed to the openNewScene method
    }

    @FXML void deleteClicked()
    {
        StockInformation selectedItem = (StockInformation) inventoryTable.getSelectionModel().getSelectedItem(); //Selected Item ID is found
        if (selectedItem !=null){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Item");
            alert.setContentText("Are you sure you want to delete this?");
            System.out.println("Delete was clicked!");
            Optional<ButtonType> result = alert.showAndWait(); //Alert is generated warning the user of their action
            if (result.get() == ButtonType.OK){
                StockInformation.deleteByProductID(selectedItem.getProductID()); //Delete by ID method in StockInformation is ran
                refreshTable(); //Table is refresh due to record being removed
                System.out.println("Item " + selectedItem.getProductName() +" deleted");
            }
        }
    }

    void openNewScene(int id)
    {

        
        if (id >= 0){
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Edit.fxml")); //If ID >= 0, the Edit scene is loaded
            try
            {
                Stage stage2 = new Stage();
                stage2.setTitle("Add/Edit");
                stage2.setScene(new Scene(loader.load()));
                stage2.show();           
                EditStockController controller2 = loader.getController();
                controller2.prepareStageEvents(stage2);

                controller2.setParent(this);
                if (id !=0) controller2.loadItem(id);

            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }

        }else if (id == -1){
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("Deliveries.fxml")); //If ID == -1, the Deliveries scene is loaded
            try
            {
                Stage stage3 = new Stage();
                stage3.setTitle("Deliveries");
                stage3.setScene(new Scene(loader.load()));
                stage3.show();           
                DeliveriesSceneController controller3 = loader.getController();
                controller3.prepareStageEvents(stage3);

                controller3.setParent(this);

            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
}

