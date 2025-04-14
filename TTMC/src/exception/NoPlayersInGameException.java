package exception;

public class NoPlayersInGameException extends RuntimeException {
    public NoPlayersInGameException(String message) {
        super(message);
    }
}
