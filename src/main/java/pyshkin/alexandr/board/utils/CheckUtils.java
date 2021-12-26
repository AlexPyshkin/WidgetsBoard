package pyshkin.alexandr.board.utils;

import pyshkin.alexandr.board.exception.InvalidValueException;

import java.util.Objects;

/**
 * Набор утилит для валидации объектов
 */
public class CheckUtils {
    /**
     * Бросает исключение если проверяемый объект - NULL
     * @param o - проверяемый объект
     * @param errorMessage - сообщение об ошибке
     * @return
     */
    public static boolean checkNotNull(Object o, String errorMessage) {
        if (Objects.isNull(o))
            throw new InvalidValueException(errorMessage);
        return true;
    };

    /**
     * Бросает исключение если проверяемое значение не положительное
     * @param value - проверяемое значение
     * @param errorMessage - сообщение об ошибке
     * @return
     */
    public static boolean checkPositive(Long value, String errorMessage) {
        if (value == null || value <= 0)
            throw new InvalidValueException(errorMessage);
        return true;
    };
}
