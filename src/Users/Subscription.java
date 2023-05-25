package Users;
import Suita.Course;
import java.util.*;

public abstract class Subscription{
    private int userId;
    private Date activationDate;
    private Integer downloadAccess = 0;
    private Integer commentAccess = 0;
    public Subscription(int userId){
        this.userId = userId;
    }
    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }
    public Date getActivationDate() {
        return activationDate;
    }
    abstract public void updatePrivileges();
    public abstract List<Course> getCoursesEnrolled();
    public abstract List<Course> getCoursesCompleted();
    public void setCommentAccess(Integer commentAccess) {
        this.commentAccess = commentAccess;
    }
    public void setDownloadAccess(Integer downloadAccess) {
        this.downloadAccess = downloadAccess;
    }

    public int getCommentAccess() {
        return commentAccess;
    }
    public int getDownloadAccess() {
        return downloadAccess;
    }
    public abstract void printSubscriptionDetails();
    public int getUserId() {
        return userId;
    }

}
