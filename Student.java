/**
 * Student
 *
 * This is a class for Student objects that will be created in LMSQuizClient/Server.
 *
 * @author ArchitVarunSahu, JakobWachter
 * @version 1.0, (2022-04-22)
 */

public class Student {
    // NOTE: The student will always have a typeOfUser value of 2.

    private String username;
    private String password;

    public Student(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
