import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.control.SplitPane;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.util.List;
/**
 * This class controls interactivity with JavaFX
 * 
 * @George Smith
 * @Alpha
 */
public class SceneController
{  
    private static Stage stage;

    @FXML private SplitPane mainPane;
    @FXML private Button goButton;
    @FXML private Button addremoveButton;
    @FXML private Button editButton;
    @FXML private ScrollBar libraryBar;
    @FXML private ListView listView;

    public SceneController()
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
        assert mainPane != null : "Can't find main pane.";
        assert goButton != null : "Can't find go button.";
        assert addremoveButton != null : "Can't find add/remove button.";
        assert listView != null : "Can't find table.";
        assert libraryBar !=null : "Can't find scrollbar.";
        
        System.out.println("Populating scene with items from the database...");        
        @SuppressWarnings("unchecked")
        List<Games> targetList = listView.getItems();
        Games.readAll(targetList);
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

    @FXML void goClicked()
    {
        System.out.println("Go clicked!");
    }

    @FXML void addremoveClicked()
    {
        System.out.println("Add/Remove clicked");
    }
    
    @FXML void editClicked()
    {
        System.out.println("Edit clicked");
    }

    @FXML void listViewClicked()
    {
        Games selectedItem = (Games) listView.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            System.out.println("Nothing selected!");
        }
        else
        {
            System.out.println(selectedItem + " (id: " + selectedItem.id + ") is selected.");
        }
    }

}
