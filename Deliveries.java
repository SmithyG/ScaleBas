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

    public Deliveries(int deliveryID, int productID, String deliveryDate, String productName)
    {
        this.deliveryID = deliveryID;
        this.productID = productID;
        setDeliveryDate(deliveryDate);
        setProductName(productName);
    }

    public static void readAll(List<Deliveries> list)
    {
        list.clear();

        String sql = "SELECT Deliveries.DeliveryID, Deliveries.DeliveryDate, ";
        sql += "StockInformation.ProductID, StockInformation.ProductName ";
        sql += "FROM Deliveries INNER JOIN DeliveryContent ON Deliveries.DeliveryID = DeliveryContent.DeliveryID ";
        sql += "INNER JOIN stockcatalog ON stockcatalog.StockID = DeliveryContent.StockID ";
        sql += "INNER JOIN stockinformation ON stockinformation.ProductID = stockcatalog.ProductID";

        PreparedStatement statement = Application.database.newStatement(sql);

        if (statement !=null)
        {
            ResultSet results = Application.database.runQuery(statement);

            if (results !=null)
            {
                try {
                    while (results.next()){
                        list.add( new Deliveries(results.getInt("DeliveryID"),
                                results.getInt("LocationID"),
                                results.getString("ProductName"),
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

}
