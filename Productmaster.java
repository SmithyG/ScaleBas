import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
public  class Productmaster{
    private int productID;
    private StringProperty productName;
    private DoubleProperty productPrice;
    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName)
    {
        this.productName = new SimpleStringProperty(productName);
    }

    public double getProductPrice() {
        return productPrice.get();
    }

    public void setProductPrice(double productPrice)
    {
        this.productPrice = new SimpleDoubleProperty(productPrice);
    }

    public int getProductID()
    {
        return productID;
    }

    public Productmaster(int productID,double productPrice, String productName)
    {
        this.productID = productID;
        setProductPrice(productPrice);
        setProductName(productName);
    }

    public static void readAll(List<Productmaster> list)
    {
        list.clear();
        PreparedStatement statement = Application.database.newStatement("SELECT ProductName, ProductPrice FROM StockInformation");

        if (statement !=null)
        {
            ResultSet results = Application.database.runQuery(statement);

            if (results !=null)
            {
                try {
                    while (results.next()){
                        list.add( new Productmaster(results.getInt("ProductID"),
                        results.getDouble("ProductPrice"),
                        results.getString("ProductName")));
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