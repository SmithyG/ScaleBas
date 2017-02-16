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
    
    private int productQuantity;
    private StringProperty productQuantityString;
    
    private int stockID;

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
    
    public int getStockID()
    {
        return stockID;
    }
    
    public void setStockID(int stockID)
    {
        this.stockID = stockID;
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
    
    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    public String getLocationName()
    {
        return locationName.get();
    }

    public void setLocationName(String locationName)
    {
        this.locationName = new SimpleStringProperty(locationName);
    }

    public int getProductQuantity()
    {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity)
    {
        this.productQuantity = productQuantity;
    }

    public String getProductQuantityString() {
        String candidateQuantity = Integer.toString(getProductQuantity());  
        return candidateQuantity;
    }

    
    public StockInformation()
    {        
    }

    public StockInformation(int productID, double productPrice, String productName, int locationID, String locationName, int productQuantity)
    {
        this.productID = productID;
        setProductPrice(productPrice);
        setProductName(productName);
        this.locationID = locationID;
        setLocationName(locationName);
        this.productQuantity = productQuantity;
    }

    public static void readAll(List<StockInformation> list)
    {
        list.clear();

        String sql = "SELECT StockInformation.ProductID, StockInformation.ProductName, StockInformation.ProductPrice, "; 
        sql += "WarehouseLocation.LocationID, WarehouseLocation.LocationName, ";
        sql += "StockCatalog.ProductQuantity ";
        sql += "FROM StockInformation INNER JOIN WarehouseLocation ON StockInformation.LocationID = WarehouseLocation.LocationID ";
        sql += "INNER JOIN StockCatalog ON StockInformation.ProductID = StockCatalog.ProductID";

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
                                results.getString("LocationName"),
                                results.getInt("ProductQuantity")));
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

        PreparedStatement statement = Application.database.newStatement("SELECT StockInformation.ProductID, StockInformation.ProductName, StockInformation.ProductPrice, StockInformation.LocationID, StockCatalog.ProductQuantity FROM StockInformation INNER JOIN StockCatalog ON StockInformation.ProductID = StockCatalog.ProductID WHERE StockInformation.ProductID = ?"); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, productID);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    stockInformation = new StockInformation(results.getInt("productID"), results.getDouble("productPrice"), results.getString("productName"), results.getInt("locationID"), "", results.getInt("productQuantity"));
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
        saveStockInformation();
        if (getProductID() == 0) setProductID(Application.database.lastNewID());
        saveStockCatalog();
    }

    public void saveStockInformation()    
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
    
    public void saveStockCatalog()
    {
        PreparedStatement statement;
        
        try 
        {

            if (productID >= 0)
            {                

                statement = Application.database.newStatement("INSERT INTO StockCatalog (ProductQuantity,ProductID) VALUES (?,?)");             
                statement.setInt(1, getProductQuantity());
                statement.setInt(2, getProductID());

            }
            else
            {
                statement = Application.database.newStatement("UPDATE StockCatalog SET ProductQuantity = ? WHERE ProductID = ?");             
                statement.setInt(1, getProductQuantity());
                statement.setInt(2, getProductID()); //TY BASED ANDREAS
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