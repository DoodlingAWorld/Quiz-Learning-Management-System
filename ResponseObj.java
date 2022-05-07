import java.io.Serializable;
import java.util.ArrayList;

/**
 * ResponseObj
 *
 * This is response class. The Server sends back the required information to the Client using objects of this class.
 * Objects are created using overloaded constructors.
 *
 * @author ArchitVarunSahu, JakobWachter
 * @version 1.0, (2022-04-22)
 */

public class ResponseObj implements Serializable {
    public boolean success; //  Declaring a variable that tells whether operation requested was a success or not
    public String username; //  Declaring a variable to hold the username of the current user
    public String[] quizScore; //  Declaring a variable to hold quiz score
    public String[] quiz; //  Declaring a variable to hold quiz questions
    public String quizScoreStr; //  Declaring a variable to hold quiz score
    public String[] userNames; //  Declaring a variable to hold userNames
    public ArrayList<String> courseList; //  Declaring an array list to hold course names

    public ResponseObj(boolean success) { //  Constructor to return modified value of success variable
        this.success = success;
        this.username = null;
    }

    public ResponseObj(boolean success, String username, String[] quizScore) { //  Constructor to return quiz score
        this.success = success;
        this.username = username;
        this.quizScore = quizScore;
    }

    public ResponseObj(boolean success, String[] quiz) { //  Constructor to return quiz questions
        this.success = success;
        this.username = null;
        this.quiz = quiz;
    }

    public ResponseObj(boolean success, String quizScoreStr) { //  Constructor to send quiz score string
        this.success = success;
        this.quizScoreStr = quizScoreStr;
    }

    public ResponseObj(boolean success, ArrayList<String> courseList) { //  Constructor to return a list of courses
        this.success = success;
        this.courseList = courseList;
    }

    public ArrayList<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<String> courseList) {
        this.courseList = courseList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(String[] quizScore) {
        this.quizScore = quizScore;
    }

    public String[] getQuiz() {
        return quiz;
    }

    public void setQuiz(String[] quiz) {
        this.quiz = quiz;
    }

    public String getQuizScoreStr() {
        return quizScoreStr;
    }

    public void setQuizScoreStr(String quizScoreStr) {
        this.quizScoreStr = quizScoreStr;
    }

    public String[] getUserNames() {
        return userNames;
    }

    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }

    @Override
    public String toString() {
        return "ResponseObj{" +
                "success=" + success +
                ", username='" + username + '\'' +
                ", quizScore=" + quizScore +
                '}';
    }
}