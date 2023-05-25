package Suita;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import Users.User;
public class Course implements Comparable<Course> {
    private int id = 0;
    private String informationToBeLearned;
    private List<User> usersEnrolled = new ArrayList<User>();
    private List<User> usersCompleted = new ArrayList<User>();
    private String title;

    public Course() {

    }
    public Course(int idCourse, String s, String title) {
        this.id = idCourse;
        this.informationToBeLearned = s;
        this.title = title;
    }
    public Course(int cid) {
        this.id = cid;
    }

    public Course(Course c) {
        this.id = c.getId();
        this.informationToBeLearned = c.getInformationToBeLearned();
        this.usersCompleted = c.getUsersCompleted();
        this.usersEnrolled = c.getUsersEnrolled();
        this.title = c.getTitle();
    }

    public void addToUsersCompleted(User u) {
        for (User current : usersCompleted)
            if (current == u) {
                return;
            }
        this.usersCompleted.add(u);
    }

    public void addToUsersEnrolled(User u) {
        for (User current : usersEnrolled)
            if (current == u) {
                return;
            }
        this.usersEnrolled.add(u);
    }

    public void print() {
        System.out.println(informationToBeLearned);
        return;
    }

    public List<User> getUsersCompleted() {
        return usersCompleted;
    }

    public List<User> getUsersEnrolled() {
        return usersEnrolled;
    }

    public int getId() {
        return id;
    }

    public void setInformationToBeLearned(String informationToBeLearned) {
        this.informationToBeLearned = informationToBeLearned;
    }

    public String getInformationToBeLearned() {
        return informationToBeLearned;
    }

    public void setUsersEnrolled(List<User> usersEnrolled) {
        this.usersEnrolled = usersEnrolled;
    }

    public void setUsersCompleted(List<User> usersCompleted) {
        this.usersCompleted = usersCompleted;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Course course) {
        if (this.getId() < course.getId()) {
            return -1;
        } else if (this.getId() > course.getId()) {
            return 1;
        } else {
            return 0;
        }
    }
}
