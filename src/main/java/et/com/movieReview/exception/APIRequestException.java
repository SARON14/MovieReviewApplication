package et.com.movieReview.exception;

public class APIRequestException extends RuntimeException {
    public APIRequestException(String message) {
        super(message);
    }
}
