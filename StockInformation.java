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

    private StringProperty locationName;

    private double productPrice;
    private StringProperty productPriceString;

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

    public void setLocationID(int locationID)
    {
        this.locationID = locationID;
    }

    public String getProductPriceString() {

        String candidatePrice = Double.toString(getProductPrice());
        if (candidatePrice.charAt(candidatePrice.length() - 2) == '.') candidatePrice += "0";        
        return candidatePrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice)
    {
        this.productPrice = productPrice;
    }

    public int getProductID()
    {
        return productID;
    }

    public String getLocationName()
    {
        return locationName.get();
    }

    public void setLocationName(String locationName)
    {
        this.locationName = new SimpleStringProperty(locationName);
    }

    public StockInformation()
    {        
    }

    public StockInformation(int productID, double productPrice, String productName, int locationID, String locationName)
    {
        this.productID = productID;
        setProductPrice(productPrice);
        setProductName(productName);
        this.locationID = locationID;
        setLocationName(locationName);
    }

    public static void readAll(List<StockInformation> list)
    {
        list.clear();

        String sql = "SELECT StockInformation.ProductID, StockInformation.ProductName, StockInformation.ProductPrice, "; 
        sql += "WarehouseLocation.LocationID, WarehouseLocation.LocationName ";
        sql += "FROM StockInformation INNER JOIN WarehouseLocation ON StockInformation.LocationID = WarehouseLocation.LocationID";

        PreparedStatement statement = Application.database.newStatement(sql);

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
                                results.getInt("LocationID"),
                                results.getString("LocationName")));
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

        PreparedStatement statement = Application.database.newStatement("SELECT ProductID, ProductName, ProductPrice, LocationID FROM StockInformation WHERE ProductID = ?"); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, productID);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    stockInformation = new StockInformation(results.getInt("productID"), results.getDouble("productPrice"), results.getString("productName"), results.getInt("locationID"), "");
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

                statement = Application.database.newStatement("INSERT INTO StockInformation (ProductName, ProductPrice, LocationID) VALUES (?, ?, ?)");             
                statement.setString(1, getProductName());
                statement.setDouble(2, getProductPrice()); 
                statement.setInt(3, getLocationID());

            }
            else
            {
                statement = Application.database.newStatement("UPDATE StockInformation SET ProductName = ?, ProductPrice = ?, LocationID = ? WHERE ProductID = ?");             
                statement.setString(1, getProductName());
                statement.setDouble(2, getProductPrice());   
                statement.setInt(3, getLocationID());
                statement.setInt(4, getProductID()); //TY BASED ANDREAS
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