package Suita;
import java.util.*;

import Users.StandardSubscription;
import Users.Subscription;
import Users.User;

import static java.lang.Math.min;

public class Quiz {
    public List<User> usersCompleted = new ArrayList<>();
    private int courseId;
    private Map<String, String> questionsAndAnswers = new HashMap<>();
    private float pointsWorth;
    public Quiz() {
        this.pointsWorth = 200;
    }

    public void setCourseId(Integer courseId)
    {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }
    public void setQuestionAndAnswer(String q, String a)
    {
        questionsAndAnswers.put(q,a);
        return;
    }
    public static Quiz createQuiz(List<String> questions,
                                  List<String> answers)
    {
        Quiz q = new Quiz();
        for(int i = 0; i<min(questions.size(), answers.size()); i++)
            q.setQuestionAndAnswer(questions.get(i), answers.get(i));
        return q;
    }
    public void TakeQuiz(User u)
    {
        System.out.println("Questions incoming: \n");
        Integer correctCount = 0, totalQuestions = 0;
        for (Map.Entry<String, String> entry : questionsAndAnswers.entrySet()) {
            totalQuestions++;
            String question = entry.getKey();
            String answer = entry.getValue();
            System.out.println(question);
            System.out.print("Your answer: ");
            Scanner scanner = new Scanner(System.in);
            String yourAnswer = scanner.nextLine();

            if(yourAnswer.equals(answer))
            {
                correctCount++;
                System.out.println("You've done it! Next question: \n");
            }
            else
            {
                System.out.println("Wrong answer. Continuing with next question: \n");
            }
        }
        float pointsEarned = ((float)correctCount / totalQuestions) * pointsWorth;
        System.out.println("You've earned " + pointsEarned  + " points! \n");
        Subscription currentUserSubscription = u.getSubscription();
        if (currentUserSubscription instanceof StandardSubscription)
            ((StandardSubscription) currentUserSubscription).
                    setCurrentPoints(((StandardSubscription) currentUserSubscription).
                            getCurrentPoints() + pointsEarned);
        usersCompleted.add(u);
        return;
    }

    public List<User> getUsersCompleted() {
        return usersCompleted;
    }
}
