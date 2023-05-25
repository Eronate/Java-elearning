package Users;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.* ;
import Suita.*;
public class User {
    private int userId;
    private String username;
    private int hashedPassword;
    private String email;
    private Subscription subscription;
    public void setHashedPassword(String password){
        this.hashedPassword = password.hashCode();
    }
    public int getHashedPassword() {
        return hashedPassword;
    }
    public User(int id)
    {
        this.userId = id;
    }
    public User(int userId, String username, String password, String email)
    {
    this.userId = userId;
    this.username = username;
    this.setHashedPassword(password);
    this.email = email;
    this.assignSubscription();
    }

    public User(int userId, String username, Integer hashedPassword, String email)
    {
        this.userId = userId;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.assignSubscription();
    }

    public User(User u)
    {
        this.userId = u.getUserId();
        this.username = u.getUsername();
        this.hashedPassword = u.getHashedPassword();
        this.email = u.getEmail();
        this.setSubscription(u.getSubscription());
    }
    static public void read(Scanner in, User u)
    {
        System.out.println("Username: ");
        u.username = in.nextLine();
        System.out.println("Password: ");
        u.setHashedPassword(in.nextLine());
        System.out.println("Email: ");
        u.email = in.nextLine();
    }
    public void print()
    {
        System.out.println("Username: " + this.username + "\n");
        System.out.println("Email: " + this.email + "\n");
    }
    public void assignSubscription()
    {
        Calendar c1 = Calendar.getInstance();
        Date now = c1.getTime();
        StandardSubscription s = new StandardSubscription(this.getUserId(), now);
        this.subscription = s;
    }
    public Subscription getSubscription() {
        return subscription;
    }
    public void setSubscription(Subscription subscription) {
        if(subscription instanceof PremiumSubscription || subscription instanceof StandardSubscription)
            this.subscription = subscription;
        return;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}
