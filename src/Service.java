import Suita.*;
import Users.*;

import java.util.*;
public class Service {
    private static Service single = null;
    private static UserDatabase udb = UserDatabase.getInstance();
    private static CourseDatabase cdb = CourseDatabase.getInstance();
    public static void populateCourses()
    {
        cdb.SQLReadCoursesFromDatabaseAndSaveLocally();
    }
    public static Service getInstance()
    {
        if (single == null)
            single = new Service();

        return single;
    }
    public User createUser()
    {
        return udb.createUserWithSubscription();
    }
    public void printUser(User u)
    {
        u.print();
    }
    public void showSubscriptionDetails(User u)
    {
        u.getSubscription().printSubscriptionDetails();
    }
    public void upgradeSubscription(User u)
    {
        Subscription stSub = u.getSubscription();
        if(stSub instanceof StandardSubscription)
        {
            PremiumSubscription premiumSubscription = PremiumSubscription.upgradeSubscription((StandardSubscription)stSub);
            u.setSubscription(premiumSubscription);
        }
        else
            System.out.println("Incompatible.\n");
    }

    public void enrollUserIntoCourse(User u, Course c)
    {
        c.addToUsersEnrolled(u);
    }
    public void completeTheCourse(User u, Course c)
    {
        c.addToUsersCompleted(u);
    }
    public User tryToLogIn(String username, String password)
    {
        List<User> lst = udb.getAllUsers();

        for(User u: lst)
        {
            if(u.getUsername().equals(username))
            {
                if(u.getHashedPassword() == password.hashCode())
                    return u;
                else
                    return null;
            }
        }
        return null;
    }
    public List<Course> findAllRemainingCourses(User u)
    {
        List<Course> com = u.getSubscription().getCoursesCompleted();
        List<Course> enr = u.getSubscription().getCoursesEnrolled();
        List<Course> allCourses = cdb.getAllCourses();
        List<Course> remainingCourses = new ArrayList<Course>();

        for(Course c: allCourses)
        {
            if( (!enr.contains(c)) && (!com.contains(c) ))
                remainingCourses.add(c);
        }
        Collections.sort(remainingCourses);
        return remainingCourses;
    }
}
