/**
 * Quiz
 *
 * This is a class for Quiz objects that will be created in LMSQuizClient/Server.
 *
 * @author ArchitVarunSahu, JakobWachter
 * @version 1.0, (2022-04-26)
 */

public class Quiz {
    private String quizName;
    private Questions[] quizQuestions;

    public Quiz(String quizName, Questions[] quizQuestions) {
        this.quizName = quizName;
        this.quizQuestions = quizQuestions;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public void setQuizQuestions(Questions[] quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    public String getQuizName() {
        return quizName;
    }

    public Questions[] getQuizQuestions() {
        return quizQuestions;
    }
}
