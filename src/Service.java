import Suita.*;
import Users.*;

import java.util.*;
public class Service {
    private static Service single = null;
    private static UserDatabase udb = UserDatabase.getInstance();
    private static CourseDatabase cdb = CourseDatabase.getInstance();
    public static void populateCourses()
    {
        String Title0 = "Game development";
        String s1 = "In a quest to understand how video games themselves are implemented, " +
                "you'll explore the design of such childhood games as: Super Mario Bros., Pong, Flappy Bird, Breakout, Match 3, Legend of Zelda, Angry Birds, " +
                "Pokémon, 3D Helicopter Game, Dreadhalls, and Portal." +
                "By class’ end, you'll have programmed several of your own games and gained a thorough understanding of the basics of game design and development.";

        cdb.createStandardCourseAndAddToDb(s1, Title0);

        String Title1 = "Databases";
        String s2 = "Whether you are a beginning programmer with an interest in Data Science, a" +
                " data scientist working closely with content experts, or a software developer seeking to learn about the database layer of the stack this specialization is for you! We focus on the relational database which is the most widely used type of database.  " +
                "Relational databases have dominated the database software marketplace for nearly four decades and form a core, foundational part of software development.";

        cdb.createStandardCourseAndAddToDb(s2, Title1);

        String Title2 = "ASP.NET";
        String s3 = "A practical example of how to build an application with ASP.NET Core API and Angular from start to finish." +
                "Learn how to build a web application from start to publishing using ASPNET Core (v2.1), Entity Framework Core and Angular (v6)";

        cdb.createStandardCourseAndAddToDb(s3, Title2);

        String Title3 = "React Front-end";
        String s4 = "React is an open-source JavaScript library used for building user interfaces (UIs). It was developed by Facebook and has gained popularity due to its efficiency, flexibility, and reusability. React follows a component-based architecture, where the UI is broken down into reusable and independent components. " +
                "Each component encapsulates its own logic and rendering.";
        List<String> pc1q = new ArrayList<>(Arrays.asList("What is React?", "What is JSX?", "Can React components have their own internal data that can change?"));
        List<String> pc1a = new ArrayList<>(Arrays.asList("javascript", "syntax", "yes"));
        Quiz q = Quiz.createQuiz(pc1q, pc1a);
        PremiumCourse pc1 = cdb.createPremiumCourseAndAddToDbWithQuiz(s4,Title3, q);
    }
    public static Service getInstance()
    {
        if (single == null)
            single = new Service();

        return single;
    }
    public void createUser()
    {
        udb.createUserWithSubscription();
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
