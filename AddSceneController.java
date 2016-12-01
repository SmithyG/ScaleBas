import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class AddSceneController
{

    private Stage stage;
    private MenuSceneController parent;

    @FXML   private TextField nameTextField;
    @FXML   private TextField priceTextField;
    @FXML   private TextField locationTextField;
    @FXML   private Button saveButton;
    @FXML   private Button cancelButton;

    private StockInformation StockInformation;

    public AddSceneController()
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
            assert priceTextField != null : "Can't find nameTextField";
            assert locationTextField != null : "Can't find nameTextField";
            assert saveButton != null : "Can't find saveButton";
            assert cancelButton != null : "Can't find cancelButton";
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }

        System.out.println("Populating scene with items from the database...");        
ObservableList<StockInformation> productList = FXCollections.observableArrayList();
        StockInformation.readAll(productList); 

    }

    public void setParent(MenuSceneController parent)
    {
        this.parent = parent;
    }

    @FXML   void saveButtonClicked()
    {
        System.out.println("Save button clicked!");        

        StockInformation.setProductName(nameTextField.getText());
        StockInformation.setProductPrice(Double.parseDouble(priceTextField.getText()));
        StockInformation.setProductLocation(locationTextField.getText());

        //StockInformation.save();

        parent.initialize();

        stage.close();
    }

    @FXML   void cancelButtonClicked()
    {
        System.out.println("Cancel button clicked!");        
        stage.close();
    }

}

