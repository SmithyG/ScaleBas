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
    private CharSequence searchFieldContents;
    @FXML private Button warehouseButton;
    @FXML private Button deliveriesButton;
    @FXML private Button chartsButton;
    @FXML private TableView<StockInformation> inventoryTable;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

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
            assert searchField != null : "Can't find search field.";
            assert addButton != null : "Can't find add button.";
            assert editButton != null : "Can't find edit button.";
            assert deleteButton != null : "Can't find delete button.";
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
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
        System.out.println("Populating scene with items from the database...");        
        ObservableList<StockInformation> productList = FXCollections.observableArrayList();
        StockInformation.readAll(productList);
        inventoryTable.setItems(productList);
    }

    @FXML   void tableViewClicked()
    {
        StockInformation selectedItem = (StockInformation) inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            System.out.println("Nothing selected!");
        }
        else
        {
            System.out.println("(id: " + selectedItem.getProductID() + ") is selected.");
        }
    }

    @FXML void warehouseClicked()
    {
        System.out.println("Warehouse Clicked");
    }

    @FXML void deliveriesClicked()
    {
        openNewScene(0);
    }

    @FXML void chartsClicked()
    {
        System.out.println("Charts Clicked");
    }

    @FXML void addClicked()
    {
        openNewScene(0);
    }

    @FXML void editClicked()
    {
        System.out.println("Edit Clicked");
        StockInformation selectedItem = (StockInformation) inventoryTable.getSelectionModel().getSelectedItem();
        openNewScene(selectedItem.getProductID());
    }

    @FXML void deleteClicked()
    {
        StockInformation selectedItem = (StockInformation) inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedItem !=null){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Item");
            alert.setContentText("Are you sure you want to delete this?");
            System.out.println("Delete was clicked!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                StockInformation.deleteByProductID(selectedItem.getProductID());
                refreshTable();
                System.out.println("Item " + selectedItem.getProductName() +" deleted");
            }
        }
    }

    @FXML void textEntered()
    {
        searchFieldContents = searchField.getCharacters();
        System.out.println(searchFieldContents.toString());
    }

    void openNewScene(int id)
    {

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("Edit.fxml"));

        try
        {
            Stage stage2 = new Stage();
            stage2.setTitle("Add");
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

    }
}

