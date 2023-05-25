package Users;

import DB.DBConnection;

import java.sql.*;

public class SubscriptionDatabase {
    private Connection connection;
    private static SubscriptionDatabase instance = null;
    private SubscriptionDatabase() {
        this.connection = DBConnection.getDatabaseConnection();
    }
    static public SubscriptionDatabase getInstance(){
        if (instance == null)
        {
            instance = new SubscriptionDatabase();
        }
        return instance;
    }

    public Subscription SQLReadSubscriptionFromDatabase(int userId)
    {
        String insertSQL = "SELECT * FROM subscription where userId=?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                Integer id = resultSet.getInt(1);
                Date activationDate = resultSet.getDate(2);
                Integer downloadAccess = resultSet.getInt(3);
                Integer commentAccess = resultSet.getInt(4);
                Integer currentPoints = resultSet.getInt(5);
                Integer isPremium = resultSet.getInt(6);

                if(isPremium == 1)
                {
                    PremiumSubscription ps = new PremiumSubscription(
                            new StandardSubscription(id,activationDate,downloadAccess, commentAccess, currentPoints)
                    );
                    return ps;
                }
                else return new StandardSubscription(id,activationDate,downloadAccess, commentAccess, currentPoints);
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        return null;
    }

    public void SQLInjectSubscriptionIntoDatabase(Subscription subscriptionToInsert)
    {
        String insertSQL = "INSERT INTO subscription (userId, activationDate, downloadAccess, commentAccess, isPremium) VALUES (?,?,?,?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, subscriptionToInsert.getUserId());
            java.util.Date utilDate = subscriptionToInsert.getActivationDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setInt(3,subscriptionToInsert.getDownloadAccess());
            preparedStatement.setInt(4, subscriptionToInsert.getCommentAccess());
            if(subscriptionToInsert instanceof PremiumSubscription)
                preparedStatement.setInt(5, 1);
            else
                preparedStatement.setInt(5, 0);
            preparedStatement.executeUpdate();

        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    //StandardSubscription(id,activationDate,downloadAccess, commentAccess, currentPoints);
    public void SQLDeleteSubscriptionFromDatabase(int userId)
    {
        String updateSQL = "DELETE from subscription WHERE userId=?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1,userId);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public void SQLUpdateSubscriptionPoints(float currentPoints)
    {
        String updateSQL = "UPDATE subscription SET currentPoints=?";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, (int) currentPoints);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    public void SQLUpdateSubscriptionPrivileges()
    {
        String updateSQL = "UPDATE subscription SET isPremium=?";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }



}
