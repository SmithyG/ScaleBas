import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class DatabaseConnection {

    private Connection connection = null;

    public DatabaseConnection(String dbFile)
    {
        try
        {         
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            System.out.println("Database connection successfully established.");
        } 
        catch (ClassNotFoundException cnfex)
        {
            System.out.println("Class not found exception: " + cnfex.getMessage());
        }
        catch (SQLException exception)
        {                        
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    public PreparedStatement newStatement(String query)
    {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
        }
        catch (SQLException resultsexception) 
        {
            System.out.println("Database statement error: " + resultsexception.getMessage());
        }
        return statement;
    }

    public ResultSet runQuery(PreparedStatement statement)
    {               
        try {            
            return statement.executeQuery();           
        }
        catch (SQLException queryexception) 
        {
            System.out.println("Database query error: " + queryexception.getMessage());
            return null;
        }
    }
    
    public int lastNewID()
    {

        PreparedStatement statement = Application.database.newStatement("SELECT last_insert_rowid() As 'ID'"); 

        try 
        {
            if (statement != null)
            {
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    return (results.getInt("ID"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database new id retrieval error: " + resultsexception.getMessage());
        }

        return -1;

    }
    
     public void executeUpdate(PreparedStatement statement)
    {               
        try {            
            statement.executeUpdate();                       
        }
        catch (SQLException queryexception) 
        {
            System.out.println("Database update error: " + queryexception.getMessage());
        }
    }


    public void disconnect()
    {
        System.out.println("Disconnecting from database.");
        try {
            if (connection != null) connection.close();                        
        } 
        catch (SQLException finalexception) 
        {
            System.out.println("Database disconnection error: " + finalexception.getMessage());
        }        
    }

}