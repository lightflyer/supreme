package exception;

public class BotException extends Exception {



    public BotException() {
        super();
    }


    public BotException(String message) {
        super(message);
    }


    public BotException(String message, Throwable cause) {
        super(message, cause);
    }


    public BotException(Throwable cause) {
        super(cause);
    }

}
