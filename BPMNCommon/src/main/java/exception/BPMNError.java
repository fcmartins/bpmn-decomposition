package exception;

public class BPMNError extends Exception {
    public BPMNError() {
    }

    public BPMNError(String message) {
        super(message);
    }

    public BPMNError(String message, Throwable cause) {
        super(message, cause);
    }

    public BPMNError(Throwable cause) {
        super(cause);
    }
}
