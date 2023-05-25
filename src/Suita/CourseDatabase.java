package Suita;
import java.util.*;
import Users.UserDatabase;
import Suita.PremiumCourse;
public class CourseDatabase {
    private static final CourseDatabase instance = new CourseDatabase();
    private static int courseCount = 0;
    List<Course> allCourses = new ArrayList<Course>();
    private CourseDatabase() {}
    static public CourseDatabase getInstance(){
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

}
