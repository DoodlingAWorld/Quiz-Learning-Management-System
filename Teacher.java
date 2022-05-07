/**
 * Teacher
 *
 * This is a class for Teacher objects that will be created in LMSQuizClient/Server.
 *
 * @author ArchitVarunSahu, JakobWachter
 * @version 1.0, (2022-04-22)
 */

public class Teacher {
    // NOTE: The teacher will always have a typeOfUser value of 1.

    private String username;
    private String password;

    public Teacher(String username, String password) {
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
