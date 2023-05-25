package Suita;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import DB.DBConnection;
import Users.*;
import Suita.PremiumCourse;
import oracle.jdbc.proxy.annotation.Pre;

public class CourseDatabase {
    private static CourseDatabase instance = null;
    private Connection connection;
    private static int courseCount = 0;
    List<Course> allCourses = new ArrayList<Course>();
    private CourseDatabase() {
        this.connection = DBConnection.getDatabaseConnection();
    }
    static public CourseDatabase getInstance(){
        if(instance == null)
            instance = new CourseDatabase();
        return instance;
    }
    public List<Course> getAllCourses() {
        return allCourses;
    }
    public Course getCourseById(int id)
    {
        for(Course course: allCourses)
            if(course.getId() == id)
                return course;
        return null;
    }
    private static Course CreateCourse(String information, String title)
    {
        courseCount++;
        Course course = new Course(courseCount);
        course.setInformationToBeLearned(information);
        course.setTitle(title);
        return course;
    }
    public Course createStandardCourseAndAddToDb(String information, String title){
        Course c = CreateCourse(information, title);
        allCourses.add(c);
        return c;
    }
    public PremiumCourse createPremiumCourseAndAddToDb(String information,
                                                       String title)
    {
        PremiumCourse pc = new PremiumCourse( CreateCourse (information,title) );
        allCourses.add(pc);
        return pc;
    }
    public PremiumCourse createPremiumCourseAndAddToDbWithQuiz(String information, String title, Quiz q)
    {
        PremiumCourse pc = createPremiumCourseAndAddToDb(information, title);
        pc.setCourseQuiz(q);
        return pc;
    }
    public void SQLReadCoursesFromDatabaseAndSaveLocally()
    {
        String selectSQL = "SELECT * FROM course";
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while(resultSet.next())
            {
                Integer idCourse = resultSet.getInt(1);
                String informationToBeLearned = resultSet.getString(2);
                String title = resultSet.getString(3);
                Integer isPremium = resultSet.getInt(4);
                Integer idQuiz = resultSet.getInt(5);
                Integer idTeacher = resultSet.getInt(6);
                if(isPremium == 1)
                {

                    PremiumCourse premiumCourse = new PremiumCourse(new Course(idCourse, informationToBeLearned, title));
                    premiumCourse.setTeacherId(idTeacher);
                    allCourses.add(premiumCourse);
                }
                else
                {
                    Course course = new Course(idCourse, informationToBeLearned, title);
                    allCourses.add(course);
                }
            }
            courseCount = allCourses.size();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public void SQLInjectCourseIntoDatabase(Course courseToInsert)
    {
        String insertSQL = "INSERT INTO course (idCourse, informationToBeLearned, title, isPremium) VALUES (?,?,?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, courseToInsert.getId() );
            preparedStatement.setString(2, courseToInsert.getInformationToBeLearned() );
            preparedStatement.setString(3,courseToInsert.getTitle());
            if (courseToInsert instanceof PremiumCourse)
                preparedStatement.setInt(4,1);
            else
                preparedStatement.setInt(4,0);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public void SQLSetTeacherId(int courseId, int idTeacher)
    {
        String insertSQL = "UPDATE course SET idTeacher=? where idCourse=?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, idTeacher);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public void SQLSetQuizId(int courseId, int idQuiz)
    {
        String updateSQL = "UPDATE course SET idQuiz=? where idCourse=?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setInt(1, idQuiz);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    //must be used after courses are populated
    public void PopulateEnrolledAndCompleted()
    {
        CourseDatabase cdb = CourseDatabase.getInstance();
        UserDatabase udb = UserDatabase.getInstance();
        String selectSQL = "SELECT * FROM coursesenrolled";
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while(resultSet.next())
            {
                Integer idUser = resultSet.getInt(1);
                Integer idCourse = resultSet.getInt(2);
                Course c = cdb.getCourseById(idCourse);
                User u = udb.findUserById(idUser);
                c.addToUsersEnrolled(u);
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        String selectSQL2 = "SELECT * FROM coursescompleted";
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL2);
            while(resultSet.next())
            {
                Integer idUser = resultSet.getInt(1);
                Integer idCourse = resultSet.getInt(2);
                Course c = cdb.getCourseById(idCourse);
                User u = udb.findUserById(idUser);
                c.addToUsersCompleted(u);
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        return;
    }
    public void SQLInjectEnrolled(int userId, int courseId)
    {
        String insertSQL = "INSERT INTO coursesenrolled (idUser, idCourse) VALUES (?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    public void SQLInjectCompleted(int userId, int courseId)
    {
        String insertSQL = "INSERT INTO coursescompleted (idUser, idCourse) VALUES (?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
