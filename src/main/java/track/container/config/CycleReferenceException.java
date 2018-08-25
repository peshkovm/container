package track.container.config;

public class CycleReferenceException extends Exception {
    public CycleReferenceException(String message) {
        super(message);
    }
}
