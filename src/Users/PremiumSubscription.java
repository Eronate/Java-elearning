package Users;
import Suita.Course;
import Suita.PremiumCourse;

import java.util.Date;
public class PremiumSubscription extends StandardSubscription{
    public PremiumSubscription(int userId, Date actDate)
    {
        super(userId, actDate);
        updatePrivileges();
    }
    public PremiumSubscription(StandardSubscription subscription)
    {
        super(subscription.getUserId(), subscription.getActivationDate());
        this.setCurrentPoints(subscription.getCurrentPoints());
        updatePrivileges();
    }
    public void updatePrivileges() {

        this.setDownloadAccess(1);
        this.setCommentAccess(1);
        this.setMaxPoints(Integer.MAX_VALUE);
    }
    public static PremiumSubscription upgradeSubscription(StandardSubscription s)
    {
        PremiumSubscription premium = new PremiumSubscription(s.getUserId(), s.getActivationDate());
        return premium;
    }
    @Override
    public void printSubscriptionDetails() {
        User u = getSubscriptionUser();
        System.out.println("User " + u.getUsername()
        + " has a premium subscription created on " + this.getActivationDate() +
        " and his access to commenting is: " + this.getCommentAccess() + " and his download access is " +
        this.getDownloadAccess() + '\n');
    }
}
