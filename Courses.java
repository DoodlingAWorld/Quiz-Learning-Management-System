/**
 * Courses
 *
 * This is a class for Course objects that will be created in LMSQuizClient/LMSQuizServer.
 *
 * @author ArchitVarunSahu, JakobWachter
 * @version 1.0, (2022-04-26)
 */


public class Courses {
    public String courseName;
    public Quiz[] quizzes;

    public Courses(Quiz[] quizzes, String courseName) {
        this.quizzes = quizzes;
        this.courseName = courseName;
    }

    public Quiz[] getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Quiz[] quizzes) {
        this.quizzes = quizzes;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
