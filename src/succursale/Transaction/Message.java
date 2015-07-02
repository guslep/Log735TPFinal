package succursale.Transaction;

import java.io.Serializable;

/**
 * Created by Gus on 6/11/2015.
 */
public  abstract class Message  {

    String type;

    public String getType() {
        return type;
    }

    public Message(String type) {
        this.type = type;
    }

    public Message() {

    }
}
