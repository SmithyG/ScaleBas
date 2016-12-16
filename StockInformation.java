import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
public  class StockInformation{
    private int productID;
    private StringProperty productName;
    private int locationID;
    private DoubleProperty productPrice;
    public String getProductName() 
    {
        return productName.get();
    }

    public void setProductName(String productName)
    {
        this.productName = new SimpleStringProperty(productName);
    }

    public int getLocationID()
    {
        return locationID;
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

    public StockInformation(int productID, double productPrice, String productName, int locationID)
    {
        this.productID = productID;
        setProductPrice(productPrice);
        setProductName(productName);
        this.locationID = locationID;
    }

    public static void readAll(List<StockInformation> list)
    {
        list.clear();
        PreparedStatement statement = Application.database.newStatement("SELECT ProductID, ProductName, ProductPrice, LocationID FROM StockInformation");

        if (statement !=null)
        {
            ResultSet results = Application.database.runQuery(statement);

            if (results !=null)
            {
                try {
                    while (results.next()){
                        list.add( new StockInformation(results.getInt("ProductID"),
                                results.getDouble("ProductPrice"),
                                results.getString("ProductName"),
                                results.getInt("LocationID")));
                    }
                }
                catch (SQLException resultsexception)
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage()); 
                }
            }
        }
    }

    public static StockInformation getByProductID(int productID)
    {
        StockInformation stockInformation = null;

        PreparedStatement statement = Application.database.newStatement("SELECT ProductID, ProductName, ProductPrice, LocationID FROM StockInformation WHERE id = ?"); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, productID);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    stockInformation = new StockInformation(results.getInt("productID"), results.getDouble("productPrice"), results.getString("productName"), results.getInt("locationID"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

        return stockInformation;
    }

    public static void deleteByProductID(int productID)
    {
    try 
    {

    PreparedStatement statement = Application.database.newStatement("DELETE FROM StockInformation WHERE ProductID = ?");             
    statement.setInt(1, productID);

    if (statement != null)
    {
    Application.database.executeUpdate(statement);
    }
    }
    catch (SQLException resultsexception)
    {
    System.out.println("Database result processing error: " + resultsexception.getMessage());
    }

    } 

    public void save()    
    {
        PreparedStatement statement;

        try 
        {

            if (productID == 0)
            {

                statement = Application.database.newStatement("SELECT ProductID FROM StockInformation ORDER BY ProductID DESC");             

                if (statement != null)	
                {
                    ResultSet results = Application.database.runQuery(statement);
                    if (results != null)
                    {
                        productID = results.getInt("ProductID") + 1;
                    }
                }

                statement = Application.database.newStatement("INSERT INTO StockInformation (ProductID, ProductName, ProductPrice, LocationID) VALUES (?, ?, ?, ?)");             
                statement.setInt(1, getProductID());
                statement.setString(2, getProductName());
                statement.setDouble(3, getProductPrice()); 
                statement.setInt(4, getLocationID());

            }
            else
            {
                statement = Application.database.newStatement("UPDATE StockInformation SET ProductName = ?, ProductPrice = ?, LocationID = ? WHERE ProductID = ?");             
                statement.setString(1, getProductName());
                statement.setDouble(2, getProductPrice());   
                statement.setInt(3, getLocationID());
            }

            if (statement != null)
            {
                Application.database.executeUpdate(statement);
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

    }
}