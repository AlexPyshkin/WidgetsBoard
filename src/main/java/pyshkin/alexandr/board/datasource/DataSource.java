package pyshkin.alexandr.board.datasource;

import java.util.Set;

/**
 * Интерфейс для кастомного источника данных
 */
public interface DataSource<T> {
    Set<T> getData();

    boolean add(T widget);

    boolean delete(T widget);
}
