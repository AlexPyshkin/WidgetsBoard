package pyshkin.alexandr.board.repository;

import pyshkin.alexandr.board.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pyshkin.alexandr.board.model.Widget;

import java.util.Collection;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static pyshkin.alexandr.board.datasource.WidgetDataSource.WIDGET_COMPARATOR;

@Repository
public class WidgetH2Repository implements IWidgetRepository {
    @Autowired
    private IWidgetH2Repository widgetH2Repository;

    @Override
    public Widget saveWidget(Widget widget) {
        return widgetH2Repository.save(widget);
    }

    @Override
    public void removeWidget(Long id) {
        Widget widget = findById(id);
        if (widget != null)
            widgetH2Repository.delete(widget);
    }

    @Override
    public Widget findById(Long id) {
        return widgetH2Repository.findById(id).orElse(null);
    }

    @Override
    public Widget findByZIndex(Long zIndex) {
        return widgetH2Repository.findByzIndex(zIndex);
    }

    @Override
    public Collection<Widget> findAllWidgets() {
        Collection<Widget> result = new TreeSet<>(WIDGET_COMPARATOR);
        result.addAll(StreamSupport.stream(widgetH2Repository.findAll().spliterator(), false)
                .collect(Collectors.toSet()));
        return result;
    }

    @Override
    public Long getNextZIndex() {
        Long currentZIndex = widgetH2Repository.findMaxZIndex();
        if (currentZIndex == null)
            return Long.MIN_VALUE;
        if (Objects.equals(currentZIndex, Long.MAX_VALUE))
            throw new InvalidValueException("Невозможно сгенерировать очередное значение Z-индекса");
        return currentZIndex + 1;
    }

    @Override
    public Collection<Widget> findByRectangle(Long minX, Long minY, Long maxX, Long maxY) {
        return widgetH2Repository.findByRectangle(minX, minY, maxX, maxY);
    }
}
