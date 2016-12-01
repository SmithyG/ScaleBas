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
    private StringProperty productLocation;
    private DoubleProperty productPrice;
    public String getProductName() 
    {
        return productName.get();
    }

    public void setProductName(String productName)
    {
        this.productName = new SimpleStringProperty(productName);
    }
    
    public String getProductLocation()
    {
        return productLocation.get();
    }
    
    public void setProductLocation (String productLocation)
    {
        this.productLocation = new SimpleStringProperty(productLocation);
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

    public StockInformation(int productID,double productPrice, String productName, String productLocation)
    {
        this.productID = productID;
        setProductPrice(productPrice);
        setProductName(productName);
        setProductLocation(productLocation);
    }

    public static void readAll(List<StockInformation> list)
    {
        list.clear();
        PreparedStatement statement = Application.database.newStatement("SELECT ProductID, ProductName, ProductPrice FROM StockInformation");

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
                        results.getString("ProductLocation")));
                    }
                }
                catch (SQLException resultsexception)
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage()); 
                }
            }
        }
    }
    
        public static StockInformation getById(int id)
    {
        StockInformation stockInformation = null;

        PreparedStatement statement = Application.database.newStatement("SELECT id, name, categoryId FROM StockInformations WHERE id = ?"); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, id);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    stockInformation = new StockInformation(results.getInt("productID"), results.getDouble("productPrice"), results.getString("productName"), results.getString("productLocation"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

        return StockInformation;
    }

    public static void deleteById(int id)
    {
        try 
        {

            PreparedStatement statement = Application.database.newStatement("DELETE FROM StockInformations WHERE id = ?");             
            statement.setInt(1, id);

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

            if (id == 0)
            {

                statement = Application.database.newStatement("SELECT id FROM StockInformations ORDER BY id DESC");             

                if (statement != null)	
                {
                    ResultSet results = Application.database.runQuery(statement);
                    if (results != null)
                    {
                        id = results.getInt("id") + 1;
                    }
                }

                statement = Application.database.newStatement("INSERT INTO StockInformations (id, name, categoryId) VALUES (?, ?, ?)");             
                statement.setInt(1, id);
                statement.setString(2, name);
                statement.setInt(3, categoryId);         

            }
            else
            {
                statement = Application.database.newStatement("UPDATE StockInformations SET name = ?, categoryId = ? WHERE id = ?");             
                statement.setString(1, name);
                statement.setInt(2, categoryId);   
                statement.setInt(3, id);
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