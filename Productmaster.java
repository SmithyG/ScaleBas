import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
public  class Productmaster{
    private StringProperty productName;
    private int productID;
    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName)
    {
        this.productName = new SimpleStringProperty(productName);
    }

    public int getproductID() {
        return productID;
    }

    public Productmaster(int productID, String productName)
    {
        this.productID = productID;
        setProductName(productName);
    }

    public static void readAll(List<Productmaster> list)
    {
        list.clear();
        PreparedStatement statement = Application.database.newStatement("SELECT ProductID, ProductName FROM StockInformation");

        if (statement !=null)
        {
            ResultSet results = Application.database.runQuery(statement);

            if (results !=null)
            {
                try {
                    while (results.next()){
                        list.add( new Productmaster(results.getInt("ProductID"),
                                results.getString("Product Name")));
                    }
                }
                catch (SQLException resultsexception)
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage()); 
                }
            }
        }
    }
}