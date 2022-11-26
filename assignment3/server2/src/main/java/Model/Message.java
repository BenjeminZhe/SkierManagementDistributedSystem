package Model;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 01:31
 **/
public class Message {
    private String message = "";

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void changeMessage(String str) {
        this.message = str;
    }
}
