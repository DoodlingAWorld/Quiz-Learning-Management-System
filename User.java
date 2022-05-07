/**
 * User
 *
 * This is a parent class for all users of the program. It will be extended by Teachers and Students.
 *
 * @author ArchitVarunSahu
 *
 * @version 1.0, (2022-04-22)
 */

public class User {
    private String username;
    private String password;
    private int typeOfUser;  //  Teacher is 1, Student is 2

    public User(String username, String password, int typeOfUser) {
        this.username = username;
        this.password = password;
        this.typeOfUser = typeOfUser;
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

    public int getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(int typeOfUser) {
        this.typeOfUser = typeOfUser;
    }
}
