package pyshkin.alexandr.board.repository;

import pyshkin.alexandr.board.model.Widget;

import java.util.Collection;

public interface IWidgetRepository {
    /**
     * Сохранение виджета в источнике данных
     *
     * @param widget
     * @return
     */
    Widget saveWidget(Widget widget);

    /**
     * Удаление виджета из источника данных
     *
     * @param id идентификатор удаляемого виджета
     */
    void removeWidget(Long id);

    /**
     * Поиск виджета в источнике данных по идентификатору
     *
     * @param id идентификатор виджета
     * @return null если виджет не найден
     */
    Widget findById(Long id);

    /**
     * Поиск виджета в источнике данных по значению Z-индекса
     *
     * @param zIndex идентификатор виджета
     * @return null если виджет не найден
     */
    Widget findByZIndex(Long zIndex);

    /**
     * Возвращает все виджеты из источника данных
     */
    Collection<Widget> findAllWidgets();


    /**
     * Поиск очередного значения Z-индекса
     *
     * @return наибольший не занятый виджетом Z-индекс
     */
    Long getNextZIndex();

    /**
     * Поиск виджетов, полностью лежащих в заданном прямоугольнике (в том числе и на его границах)
     *
     * @param minX координата X левого нижнего угла заданного прямоугольника
     * @param minY координата Y левого нижнего угла заданного прямоугольника
     * @param maxX координата X правого верхнего угла заданного прямоугольника
     * @param maxY координата Y правого верхнего угла заданного прямоугольника
     * @return
     */
    Collection<Widget> findByRectangle(Long minX, Long minY, Long maxX, Long maxY);

}
