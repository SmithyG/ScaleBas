import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.collections.ObservableList;
import java.util.List;
import javafx.collections.FXCollections;

public class EditStockController
{

    private Stage stage;
    private MenuSceneController parent;

    @FXML   private TextField nameTextField;
    @FXML   private TextField priceTextField;
    @FXML   private ChoiceBox<Location> locationChoiceBox;
    @FXML   private Button saveButton;
    @FXML   private Button cancelButton;

    private StockInformation stockInformation;

    public EditStockController()
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
        {
            assert nameTextField != null : "Can't find nameTextField";
            assert priceTextField != null : "Can't find priceTextField";
            assert locationChoiceBox != null : "Can't find locationChoiceBox";
            assert saveButton != null : "Can't find saveButton";
            assert cancelButton != null : "Can't find cancelButton";
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }

        System.out.println("Populating scene with items from the database...");        

        List<Location> productList = locationChoiceBox.getItems();
        Location.readAll(productList);
        locationChoiceBox.getSelectionModel().selectFirst();

    }

    public void setParent(MenuSceneController parent)
    {
        this.parent = parent;
    }

    public void loadItem(int productID)
    {
        stockInformation = StockInformation.getByProductID(productID);
        nameTextField.setText(stockInformation.getProductName());
        priceTextField.setText(stockInformation.getProductPriceString());

        List<Location> productList = locationChoiceBox.getItems();

        for(Location l : productList)
        {
            if (l.id == stockInformation.getLocationID())
            {
                locationChoiceBox.getSelectionModel().select(l);
            }
        } 
    }

    @FXML   void saveButtonClicked()
    {
        System.out.println("Save button clicked!");   

        /* StockInformation stockInformation = new StockInformation(0,
        Double.parseDouble(priceTextField.getText()),        
        nameTextField.getText(),
        locationChoiceBox.getSelectionModel().getSelectedItem()
        ); */

        double price = Double.parseDouble(priceTextField.getText());

        price = Math.rint(price * 100) / 100;		

        if (stockInformation == null)
        {
            stockInformation = new StockInformation();
        }
        stockInformation.setProductName(nameTextField.getText());
        stockInformation.setProductPrice(price);
        Location selectedLocation = (Location) locationChoiceBox.getSelectionModel().getSelectedItem();
        stockInformation.setLocationID(selectedLocation.id);

        stockInformation.save(); 

        parent.refreshTable();

        stage.close();
    }

    @FXML   void cancelButtonClicked()
    {
        System.out.println("Cancel button clicked!");        
        stage.close();
    }

}

