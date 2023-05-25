package Users;

import Suita.PremiumCourse;

public class Teacher extends User {
    public Teacher(User u)
    {
        super(u);
        //Teacher gets a premium subscription
        if(u.getSubscription() instanceof StandardSubscription)
            this.setSubscription(PremiumSubscription.upgradeSubscription((StandardSubscription)this.getSubscription()));
    }
    public void assignTeacherSelfToCourse(PremiumCourse c)
    {
        c.setTeacherId(this.getUserId());
    }
}
