package Users;
import Suita.Course;
import java.util.*;

import Suita.CourseDatabase;
import Users.UserDatabase;
public class StandardSubscription extends Subscription{
    static private float maxPoints = 500;
    private float currentPoints;
    public StandardSubscription(int userId, Date actDate)
    {
        super(userId);
        this.currentPoints = 0;
        this.setActivationDate(actDate);
    }
    public StandardSubscription(int userId, Date actDate, Integer downloadAccess,
                                Integer commentAccess, Integer currentPoints)
    {
        super(userId);
        this.currentPoints = 0;
        this.setActivationDate(actDate);
        this.setDownloadAccess(downloadAccess);
        this.setCommentAccess(commentAccess);
        this.currentPoints = currentPoints;
    }
//    public void addCourseToEnrolled(Course c)
//    {
//        if(this.getCoursesEnrolled().size() > maxEnrolledCourses)
//        {
//            System.out.println("Too many courses. As a standard subscriber you can only learn 3 courses at once.");
//            return;
//        }
//        else
//        {
//            List<Course> currentCourses = this.getCoursesEnrolled();
//        }
//    }
    public User getSubscriptionUser()
    {
        UserDatabase context = UserDatabase.getInstance();
        List<User> users = context.getAllUsers();
        User searched = null;
        for(User u : users)
        {
            if(u.getUserId() == this.getUserId())
            {
                searched = u;
                break;
            }
        }
        return searched;
    }
//    public void addCourseToCompleted(Course c)
//    {
//        List<User> listEnrolled = c.getUsersEnrolled();
//        User searched = this.getSubscriptionUser();
//        for(User u: listEnrolled)
//            if (u == searched)
//            {
//                listEnrolled.remove(u);
//                break;
//            }
//        c.getUsersCompleted().add(searched);
//        this.updatePrivileges();
//    }
    public List<Course> getCoursesCompleted()
    {
    CourseDatabase context = CourseDatabase.getInstance();

    List<Course> allCourses = context.getAllCourses(),
                 coursesCompleted = new ArrayList<Course>();

    User current = this.getSubscriptionUser();
        for(Course course : allCourses) {
//           if(course.getUsersEnrolled().contains(current))
//                coursesEnrolled.add(course);
            List<User> ls = course.getUsersCompleted();
            for (User u : ls)
                if(u.getUserId() == this.getUserId())
                    coursesCompleted.add(course);
        }
    return coursesCompleted;
    }

    public List<Course> getCoursesEnrolled()
    {
        CourseDatabase context = CourseDatabase.getInstance();

        List<Course> allCourses = context.getAllCourses(),
                coursesEnrolled = new ArrayList<Course>();

        User current = this.getSubscriptionUser();
        for(Course course : allCourses) {
//           if(course.getUsersEnrolled().contains(current))
//                coursesEnrolled.add(course);
            List<User> ls = course.getUsersEnrolled();
            for (User u : ls)
                if(u.getUserId() == this.getUserId())
                    coursesEnrolled.add(course);
        }
        return coursesEnrolled;
    }
    public void updatePrivileges() {
        if (this.getCoursesCompleted().size() > 10) {
            this.setDownloadAccess(1);
            this.setCommentAccess(1);
        }
    }

    @Override
    public void printSubscriptionDetails() {
        User u = getSubscriptionUser();
        System.out.println("User " + u.getUsername()
        + " has a standard subscription created on " + this.getActivationDate() +
        " and his access to commenting is: " + this.getCommentAccess() + " and his download access is " +
        this.getDownloadAccess() + '\n');
    }

    public static float getMaxPoints() {
        return maxPoints;
    }
    public static void setMaxPoints(float maxPoints) {
        StandardSubscription.maxPoints = maxPoints;
    }
    public float getCurrentPoints() {
        return currentPoints;
    }
    public void setCurrentPoints(float currentPoints) {
        this.currentPoints = currentPoints;
    }

}

