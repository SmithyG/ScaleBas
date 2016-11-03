import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
public  class Productmaster{
    private final SimpleStringProperty productName;
    private final SimpleIntegerProperty productID;
   public String getProductName() {
      return productName.get();
   }

   public Integer getProductID() {
      return productID.get();
   }
}