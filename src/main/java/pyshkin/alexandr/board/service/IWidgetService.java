package pyshkin.alexandr.board.service;

import pyshkin.alexandr.board.model.Widget;

import java.util.Collection;

public interface IWidgetService {
    /**
     * Создание виджета
     * @param widget
     * @return
     */
    Widget createWidget(Widget widget);

    /**
     * Обновление существующего виджета
     * @param id идентификатор обновляемого виджета
     * @param widget новое состояние виджета
     * @return
     */
    Widget updateWidget(Long id, Widget widget);


    /**
     * Удаление виджета
     * @param id идентификатор удаляемого виджета
     */
    void removeWidget(Long id);

    /**
     * Поиск виджета по идентификатору
     * @param id идентификатор виджета
     * @return null если виджет не найден
     */
    Widget getWidget(Long id);

    /**
     * Возвращает все виджеты
     */
    Collection<Widget> getAllWidget();

    /**
     * Поиск виджетов, полностью лежащих в заданном прямоугольнике (в том числе и на его границах)
     * @param minX координата X левого нижнего угла заданного прямоугольника
     * @param minY координата Y левого нижнего угла заданного прямоугольника
     * @param maxX координата X правого верхнего угла заданного прямоугольника
     * @param maxY координата Y правого верхнего угла заданного прямоугольника
     * @return
     */
    Collection<Widget> findByRectangle(Long minX, Long minY, Long maxX, Long maxY);
}
