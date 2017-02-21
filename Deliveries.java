import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
public class Deliveries
{
    private int deliveryID;
    private int productID;
    private int deliveryQuantity;
    private int deliveryStatus;
    private StringProperty deliveryDate;
    private StringProperty productName;

    public int getDeliveryID()
    {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID)
    {
        this.deliveryID = deliveryID;
    }

    public int getProductID()
    {
        return productID;
    }

    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    public int getDeliveryQuantity()
    {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity)
    {
        this.deliveryQuantity = deliveryQuantity;
    }

    public int getDeliveryStatus()
    {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus)
    {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryDate()
    {
        return deliveryDate.get();
    }

    public void setDeliveryDate(String deliveryDate)
    {
        this.deliveryDate = new SimpleStringProperty(deliveryDate);
    }

    public String getProductName()
    {
        return productName.get();
    }

    public void setProductName(String productName)
    {
        this.productName = new SimpleStringProperty(productName);
    }

    public Deliveries()
    {
    } 

    public Deliveries(int deliveryID, int productID, int deliveryQuantity, int deliveryStatus, String deliveryDate, String productName)
    {
        this.deliveryID = deliveryID;
        this.productID = productID;
        this.deliveryQuantity = deliveryQuantity;
        this.deliveryStatus = deliveryStatus;
        setDeliveryDate(deliveryDate);
        setProductName(productName);
    }

    public static void readAll(List<Deliveries> list)
    {
        list.clear();

        String sql = "SELECT Deliveries.DeliveryID, Deliveries.DeliveryDate, Deliveries.DeliveryStatus, ";
        sql += "DeliveryContent.DeliveryQuantity, ";
        sql += "StockInformation.ProductID, StockInformation.ProductName ";
        sql += "FROM Deliveries INNER JOIN DeliveryContent ON Deliveries.DeliveryID = DeliveryContent.DeliveryID ";
        sql += "INNER JOIN stockcatalog ON stockcatalog.StockID = DeliveryContent.StockID ";
        sql += "INNER JOIN stockinformation ON stockinformation.ProductID = stockcatalog.ProductID ";
        sql += "WHERE Deliveries.DeliveryStatus = 0"; //Only fields with a DeliveryStatus = 0 are displayed

        PreparedStatement statement = Application.database.newStatement(sql);

        if (statement !=null)
        {
            ResultSet results = Application.database.runQuery(statement);
            if (results !=null)
            {
                try {
                    while (results.next()){
                        list.add( new Deliveries(results.getInt("deliveryID"),
                                results.getInt("productID"),
                                results.getInt("deliveryQuantity"),
                                results.getInt("deliveryStatus"),
                                results.getString("deliveryDate"),
                                results.getString("productName")));
                    }
                }
                catch (SQLException resultsexception)
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage()); 
                }
            }

        }

    }

    public static Deliveries getByDeliveryID(int deliveryID)
    {
        Deliveries deliveries = null; //Object is emptied 

        String sql = "SELECT Deliveries.DeliveryID, Deliveries.DeliveryDate, "; //SELECT statement reads data from database
        sql += "DeliveryContent.DeliveryQuantity, ";
        sql += "StockInformation.ProductID, StockInformation.ProductName ";
        sql += "FROM Deliveries INNER JOIN DeliveryContent ON Deliveries.DeliveryID = DeliveryContent.DeliveryID "; //INNER JOIN selects only fields with matching DeliveryIDs
        sql += "INNER JOIN stockcatalog ON stockcatalog.StockID = DeliveryContent.StockID "; //INNER JOIN selects StockCatalog fields with matching StockIDs
        sql += "INNER JOIN stockinformation ON stockinformation.ProductID = stockcatalog.ProductID"; //INNER JOIN selects StockInformation fields with matching ProductIDs

        PreparedStatement statement = Application.database.newStatement(sql); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, deliveryID);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    deliveries = new Deliveries(results.getInt("deliveryID"), results.getInt("productID"), results.getInt("deliveryQuantity"), results.getInt("deliveryStatus"), results.getString("deliveryDate"), results.getString("productName"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

        return deliveries;
    }
    
    public static void processDeliveryQuantity(int deliveryID)
    {
        try 
        {
            String sql = "UPDATE StockCatalog SET ProductQuantity = ProductQuantity + ";
            sql += "(SELECT DeliveryQuantity FROM DeliveryContent WHERE DeliveryContent.StockID= StockCatalog.StockID) ";
            sql += "WHERE StockCatalog.StockID IN (SELECT DeliveryContent.StockID FROM DeliveryContent WHERE DeliveryContent.DeliveryID = ?)";
            PreparedStatement statement = Application.database.newStatement(sql);    
            statement.setInt(1, deliveryID);
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

    public static void processDeliveryStatus(int deliveryID)
    {

        try
        {
            String sql = "UPDATE Deliveries SET DeliveryStatus = 1 WHERE DeliveryID = ?";
            PreparedStatement statement = Application.database.newStatement(sql);  
            statement.setInt(1, deliveryID);
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
