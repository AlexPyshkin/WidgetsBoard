package pyshkin.alexandr.board.exception;

/**
 * Бросать при ошибке валидации параметров
 */
public class InvalidValueException extends RuntimeException{
    public InvalidValueException(String message) {
        super(message);
    }
}
