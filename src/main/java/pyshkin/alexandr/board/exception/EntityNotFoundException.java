package pyshkin.alexandr.board.exception;

/**
 * Бросать в случае неудачной попытке поиска экземпляра сущности в источнике данных
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Class clazz, Long id) {
        this(String.format("Сущность %s с идентификатором %d не найдена", clazz.getSimpleName(), id));
    }
}
