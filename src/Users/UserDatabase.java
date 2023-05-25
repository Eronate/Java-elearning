package Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static Users.User.read;
import Users.User;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

public final class UserDatabase {
    private Connection connection;
    private static UserDatabase instance = null;
    private List<User> allUsers = new ArrayList<User>();
    private static int userCount = 0;
    private UserDatabase(Connection c) {
        this.connection = c;
    }
    //First create the connection between the class and the real db
    public void createConnection(Connection connection)
    {
        if(instance == null)
            instance = new UserDatabase(connection);
        return;
    }
    static public UserDatabase getInstance(){
        return instance;
    }
    public User createUserWithSubscription()
    {
        User newUser = new User(++userCount);
        Scanner scanner = new Scanner(System.in);
        read(scanner, newUser);
        newUser.assignSubscription();
        allUsers.add(newUser);
        return newUser;
    }
    public User findUserById(int userId) {
        for (User user : allUsers)
            if (user.getUserId() == userId)
                return user;
        return null;
    }
    public void removeUser(User u)
    {
        allUsers.remove(u);
        return;
    }
    public void addExistingUser(Teacher u)
    {
        //Strictly for teachers
        userCount++;
        allUsers.add(u);
        return;
    }
    public List<User> getAllUsers() {
        return allUsers;
    }

    public void SQLReadUsersFromDatabaseAndSaveLocally()
    {
        String selectSQL = "SELECT * FROM user";
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while(resultSet.next())
            {
                Integer id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                Integer hashedPassword = resultSet.getInt(3);
                String email = resultSet.getString(4);
                Integer isTeacher = resultSet.getInt(5);

                if(isTeacher == 1)
                {
                    User user = new User(id, username, hashedPassword, email);
                    Teacher teacher = new Teacher(user);
                    allUsers.add(teacher);
                }
                else
                {
                    User user = new User(id, username, hashedPassword, email);
                    allUsers.add(user);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    public void SQLInjectUserIntoDatabase(User userToInsert)
    {
        String insertSQL = "INSERT INTO user (userId, username, hashedPassword, email, isTeacher) VALUES (?,?,?,?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, userToInsert.getUserId());
            preparedStatement.setString(2,userToInsert.getUsername());
            preparedStatement.setInt(3,userToInsert.getHashedPassword());
            preparedStatement.setString(4, userToInsert.getEmail());
            if(userToInsert instanceof Teacher)
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

    public void SQLUpdateUserToTeacher(int userId)
    {
        String updateSQL = "UPDATE user SET isTeacher=? WHERE userId=?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2,userId);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    public void SQLDeleteUser(int userId)
    {
        String updateSQL = "DELETE from user WHERE userId=?";
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
}
