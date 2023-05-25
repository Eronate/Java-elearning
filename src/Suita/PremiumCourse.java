package Suita;

import Users.PremiumSubscription;
import Users.Teacher;
import Users.User;
import Users.UserDatabase;
public class PremiumCourse extends Course{
    private Quiz courseQuiz;
    private int teacherId;
    public PremiumCourse(Course c)
    {
        super(c);
        this.teacherId = -1;
    }

    public int getTeacherId() {
        return this.teacherId;
    }
    public void setTeacherId(int tid)
    {
        this.teacherId = tid;
    }

    public void setCourseQuiz(Quiz q)
    {
        this.courseQuiz = q;
    }
    public Quiz getCourseQuiz()
    {
        return courseQuiz;
    }
}
