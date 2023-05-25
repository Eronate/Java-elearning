import Users.UserDatabase;
import Suita.*;
import Users.*;
import oracle.sql.BOOLEAN;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        User currentUser = null;
        Service.populateCourses();
        UserDatabase udb = UserDatabase.getInstance();
        CourseDatabase cdb = CourseDatabase.getInstance();

        while(Boolean.TRUE)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("");
            System.out.println("Choose your option:");
            System.out.println("1. Create an account.");
            System.out.println("2. Log into an account.");
            System.out.println("3. Show me all the remaining courses. ");
            System.out.println("4. Show me all the courses I'm enrolled in. ");
            System.out.println("5. Show me all the courses I've completed.");
            System.out.println("6. Show me my subscription. ");
            System.out.println("7. Upgrade my subscription.");
            System.out.println("8. Show me how many points I have.");
            System.out.println("9. Make me a teacher.");
            System.out.println("");
            int choice;
            if(scanner.hasNextInt())
                choice = scanner.nextInt();
            else
            {
                System.out.println("Invalid input.");
                continue;
            }
            switch(choice){
                case 1:
                    service.createUser();
                    break;
                case 2:
                {
                    System.out.println("Enter an username:");
                    Scanner scanner2 = new Scanner(System.in);
                    String uname = scanner2.nextLine();
                    System.out.println("Enter a passsword:");
                    String password = scanner2.nextLine();
                    User u = service.tryToLogIn(uname,password);
                    if(u != null) {
                        System.out.println("You have logged in.");
                        currentUser = u;
                    }
                    else
                        System.out.println("Wrong credentials.");
                    break;
                }
                case 3:
                {
                    if(currentUser != null)
                    {
                        List<Course> remC = service.findAllRemainingCourses(currentUser);
                        for(Course c: remC)
                        {
                            System.out.print(c.getId() + " ");
                            System.out.print(c.getTitle() + " ");
                            if(c instanceof PremiumCourse)
                                System.out.print("PREMIUM\n");
                            else System.out.println("");
                        }

                        Scanner scanner3 = new Scanner(System.in);
                        System.out.println("Choose the course you want to enroll in: ");
                        int sth;
                        if (scanner3.hasNextInt())
                            sth = scanner3.nextInt();
                        else
                        {
                            System.out.println("Invalid input.");
                            break;
                        }
                        Course courseSought = cdb.getCourseById(sth);
                        if(courseSought == null)
                        {
                            System.out.println("Course doesn't exist");
                            break;
                        }

                        Boolean hasEnrolled = courseSought.getUsersEnrolled().contains(currentUser);
                        Boolean isPremium = (courseSought instanceof PremiumCourse);

                        if(isPremium && !(currentUser.getSubscription() instanceof PremiumSubscription))
                        {
                            System.out.println("The course is premium and you cannot access it because you do not have a premium subscription \n.");
                            break;
                        }

                        if(courseSought != null && !hasEnrolled)
                        {
                            service.enrollUserIntoCourse(currentUser, courseSought);
                            System.out.println("You have been enrolled in the course.");
                            courseSought.print();
                            System.out.println("Do you want to set the course as completed? 1 (Yes) / 2 (No).");
                            int sth2;
                            if(scanner3.hasNextInt())
                            {
                                sth2 = scanner3.nextInt();
                            }
                            else
                            {
                                System.out.println("Invalid input.");
                                break;
                            }
                            if(sth2 == 1)
                                service.completeTheCourse(currentUser, courseSought);
                            else
                            {
                                System.out.println("Okay.");
                                break;
                            }
                        }
                        else if(hasEnrolled == Boolean.TRUE)
                            System.out.println("You are already enrolled in that course!");

                        else
                            System.out.println("The course with that id doesn't exist.");

                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    break;
                }
                case 4: {
                    if (currentUser != null) {

                        if(currentUser.getSubscription().getCoursesEnrolled().size() == 0)
                        {
                            System.out.println("You haven't enrolled in any courses yet.");
                            break;
                        }

                        List<Course> enr = currentUser.getSubscription().getCoursesEnrolled();
                        for (Course c : enr) {
                            System.out.print(c.getId() + " ");
                            System.out.print(c.getTitle() + " ");
                            if (c instanceof PremiumCourse)
                                System.out.print("PREMIUM\n");
                            else System.out.println("");
                        }



                        System.out.println("Press the index of the course you want to select or any other input to exit: \n");
                        Scanner scanner4 = new Scanner(System.in);

                        int sth;
                        if (scanner4.hasNextInt())
                            sth = scanner4.nextInt();
                        else {
                            break;
                        }
                        Course courseSought = cdb.getCourseById(sth);

                        Boolean hasCompleted = courseSought.getUsersCompleted().contains(currentUser);
                        Boolean hasEnrolled = courseSought.getUsersEnrolled().contains(currentUser);
                        Boolean isPremium = (courseSought instanceof PremiumCourse);
                        Boolean isTeacher = (currentUser instanceof Teacher);
                        //The user could type in an id of a course that he isn't even enrolled in.
                        if (courseSought != null && hasEnrolled) {
                            courseSought.print();
                            if(isTeacher && isPremium)
                            {
                                System.out.println("Do you want to be the coordinator of the selected course? 1. Yes, 2. No");

                                if (scanner4.hasNextInt())
                                    sth = scanner4.nextInt();
                                else {
                                    break;
                                }
                                if(sth == 1)
                                {
                                    if(((PremiumCourse)courseSought).getTeacherId() == currentUser.getUserId())
                                    {
                                        System.out.println("You are already the coordinator of the picked course!");

                                        break;
                                    }
                                    else
                                    {
                                        ((PremiumCourse) courseSought).setTeacherId(currentUser.getUserId());
                                        System.out.println("You have become the coordinator of the course!");
                                        courseSought.setTitle(courseSought.getTitle() + " (Coordinator: " + currentUser.getEmail() + ")");
                                        break;
                                    }
                                }
                                else
                                    break;

                            }
                            if (hasCompleted) {
                                System.out.println("You have already completed this course.");
                            } else {
                                System.out.println("Do you want to set the course as completed? 1 (Yes) / 2 (No).");
                                int sth2;
                                if (scanner4.hasNextInt()) {
                                    sth2 = scanner4.nextInt();
                                } else {
                                    System.out.println("Invalid input.");
                                    break;
                                }
                                if (sth2 == 1)
                                    service.completeTheCourse(currentUser, courseSought);
                                else {
                                    System.out.println("Okay.");
                                }
                            }
                        } else if (!hasEnrolled)
                            System.out.println("You haven't enrolled in the course with that id");
                        else if(courseSought == null)
                        {
                            System.out.println("Course doesn't exist");
                        }
                        break;
                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                }
                case 5:
                {
                    if(currentUser != null) {
                        List<Course> compl = currentUser.getSubscription().getCoursesCompleted();

                        for (Course c : compl) {
                            System.out.print(c.getId() + " ");
                            System.out.print(c.getTitle() + " ");
                            if (c instanceof PremiumCourse)
                                System.out.print("PREMIUM\n");
                            else
                                System.out.println("");
                        }

                        if(currentUser.getSubscription().getCoursesCompleted().size() == 0)
                        {
                            System.out.println("You haven't completed any courses yet.");
                            break;
                        }

                        System.out.println("Choose a PREMIUM course to take a quiz in: \n");
                        Scanner scanner4 = new Scanner(System.in);
                        int sth;
                        if (scanner4.hasNextInt())
                            sth = scanner4.nextInt();
                        else {
                            break;
                        }
                        Course courseSought = cdb.getCourseById(sth);
                        if(courseSought == null)
                            System.out.println("The course doesn't exist.");
                        Boolean isPremium = (courseSought instanceof PremiumCourse);

                        if (!courseSought.getUsersCompleted().contains(currentUser))
                        {
                            System.out.println("You haven't completed that course.");
                            break;
                        }

                        if (isPremium) {
                            if (((PremiumCourse) courseSought).getCourseQuiz().getUsersCompleted().contains(currentUser)) {
                                System.out.println("You have already taken the quiz for this course.\n");
                                break;
                            }
                            ((PremiumCourse) courseSought).getCourseQuiz().TakeQuiz(currentUser);
                            break;
                        } else {
                            System.out.println("The selected course isn't premium and doesn't have a quiz.");
                            break;
                        }
                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                }
                case 6:
                {
                    if(currentUser != null)
                    {
                        currentUser.getSubscription().printSubscriptionDetails();
                        break;
                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                }
                case 7:
                {
                    if(currentUser != null) {
                        currentUser.setSubscription(PremiumSubscription.upgradeSubscription((StandardSubscription) currentUser.getSubscription()));
                        System.out.println("Your subscription has been upgraded.");
                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                    break;
                }
                case 8:
                {
                    if(currentUser != null)
                    {
                        System.out.println("You have " + ((StandardSubscription)currentUser.getSubscription()).getCurrentPoints() + " points!") ;
                        break;
                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                }
                case 9:
                {
                    if(currentUser != null)
                    {
                        Teacher teacher = new Teacher(currentUser);
                        udb.removeUser(currentUser);
                        udb.addExistingUser(teacher);
                        currentUser = teacher;
                        System.out.println("You have just become a teacher! Now you can assign yourself to a course to be the coordinator of the respective course.");
                        break;
                    }
                    else
                    {
                        System.out.println("You aren't logged in.");
                        break;
                    }
                }
            }
        }
    }
}