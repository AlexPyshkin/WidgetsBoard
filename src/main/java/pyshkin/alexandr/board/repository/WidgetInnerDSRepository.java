package pyshkin.alexandr.board.repository;

import pyshkin.alexandr.board.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pyshkin.alexandr.board.datasource.WidgetDataSource;
import pyshkin.alexandr.board.model.Widget;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

import static pyshkin.alexandr.board.datasource.WidgetDataSource.WIDGET_COMPARATOR;

@Repository
public class WidgetInnerDSRepository implements IWidgetRepository {

    private WidgetDataSource widgetDataSource;

    @Autowired
    public WidgetInnerDSRepository(WidgetDataSource widgetDataSource) {
        this.widgetDataSource = widgetDataSource;
    }

    @Override
    public Widget saveWidget(Widget widget) {
        widgetDataSource.add(widget);
        return widget;
    }

    @Override
    public void removeWidget(Long id) {
        Optional<Widget> widgetInDataSource = findByIdOptional(id);
        widgetInDataSource.ifPresent(widget -> widgetDataSource.delete(widget));
    }

    @Override
    public Widget findById(Long id) {
        return findByIdOptional(id).orElse(null);
    }

    @Override
    public Widget findByZIndex(Long zIndex) {
        return widgetDataSource.getData().stream().filter(widget -> widget.getzIndex().equals(zIndex)).findFirst().orElse(null);
    }

    @Override
    public Collection<Widget> findAllWidgets() {
        return widgetDataSource.getData();
    }

    @Override
    public Long getNextZIndex() {
        Collection<Widget> data = widgetDataSource.getData();
        if (data.isEmpty())
            return Long.MIN_VALUE;

        long currentZIndex = widgetDataSource.getData().last().getzIndex();
        if (Objects.equals(currentZIndex, Long.MAX_VALUE))
            throw new InvalidValueException("Невозможно сгенерировать очередное значение Z-индекса");
        return currentZIndex + 1;
    }

    @Override
    public Collection<Widget> findByRectangle(Long minX, Long minY, Long maxX, Long maxY) {
        /*
        Вариант рабочий, но при исходных данных, близких к критичным значениям виджетов
        (minX близок к самой маленькой координате Х в системе, maxY - к самой большой координате Y)
        последний этап обработки коллекции приведет к перебору объектов, что близко к O(n)
        (хотя это и частный случай, а значит, решение могу считать допустимым)

        return widgetDataSource.getGroupedData().subMap(minX, maxX).values().stream() // виджеты с posX = [minX, maxX)
                        .flatMap(m -> m.subMap(minY, maxY).values().stream()) // виджеты с posY = [minY, maxY)
                        .flatMap(m -> m.subMap(1L, maxX - minX + 1).values().stream()) // виджеты, чья ширина не превышает разницу входящих координат X
                        .flatMap(m -> m.subMap(1L, maxY - minY + 1).values().stream()) // виджеты, чья высота не превышает разницу входящих координат Y
                        // могут остаться виджеты с (posX > minX), но с (width == maxX-minX). Они не должны возвращаться
                        // поэтому далее придется немного поперебирать
                        .flatMap(Set::stream).filter(w -> w.getPosX() + w.getWidth() <= maxX && w.getPosY() + w.getHeight() <= maxY)
                        .collect(Collectors.toSet());
        */

        Collection<Widget> result = new TreeSet<>(WIDGET_COMPARATOR);
        // виджеты с posX = [minX, maxX)
        widgetDataSource.getGroupedData().subMap(minX, maxX).forEach((posX, xGroupedMap) -> {
            // виджеты с posY = [minY, maxY)
            xGroupedMap.subMap(minY, maxY).forEach((posY, yGroupedMap) -> {
                // виджеты, чья ширина не превышает (maxX-posX). Уже здесь отсеиваются виджеты,
                // которые могут не влезть в заданный диапазон по X
                yGroupedMap.subMap(1L, maxX - posX + 1).forEach((width, widthGroupedMap) -> {
                    // виджеты, чья высота не превышает (maxY-posY). Уже здесь отсеиваются виджеты,
                    // которые могут не влезть в заданный диапазон по Y
                    widthGroupedMap.subMap(1L, maxY - posY + 1).forEach((height, heightGroupedSet) -> {
                        result.addAll(heightGroupedSet);
                    });
                });
            });
        });

        return result;
    }

    private Optional<Widget> findByIdOptional(Long id) {
        return widgetDataSource.getData().stream().filter(widget -> widget.getId().equals(id)).findFirst();
    }
}
