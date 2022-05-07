import java.io.Serializable;
import java.util.Arrays;

/**
 * RequestObj
 *
 * This is a class that the Client uses to communicate with the server. It contains a check value and a String[]
 * array of objects to be sent between the server and client.
 *
 * @author ArchitVarunSahu
 * @version 1.0, (2022-04-22)
 */

public class RequestObj implements Serializable {
    public String checkString; //  Variable that tells server what kind of request is being sent
    public String[] data; //  Array that holds parameters that will be used in Server operation

    public RequestObj(String checkString, String[] data) {
        this.checkString = checkString;
        this.data = data;
    }

    public String getCheckString() {
        return checkString;
    }

    public void setCheckString(String checkString) {
        this.checkString = checkString;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestObj{" +
                "checkString='" + checkString + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
