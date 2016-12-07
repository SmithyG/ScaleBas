import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;
public class Location
{
    public int id;
    public String locationName;

    public Location(int id, String locationName)
    {
        this.id = id;
        this.locationName = locationName;
    }

    @Override public String toString()
    {
        return locationName;
    }

    public static void readAll (List<Location> list)
    {
        list.clear();

        PreparedStatement statement = Application.database.newStatement("SELECT LocationID, LocationName FROM WarehouseLocation ORDER BY LocationID");

        if (statement !=null)
        {
            ResultSet results = Application.database.runQuery(statement);

            if (results !=null)
            {
                try{
                    while (results.next()) {
                        list.add( new Location(results.getInt("LocationID"), results.getString("LocationName")));
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
