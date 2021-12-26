package pyshkin.alexandr.board.datasource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pyshkin.alexandr.board.model.Widget;

import java.util.*;

@Component
@Scope(value = "singleton")
public class WidgetDataSource extends TreeSet<Widget> implements IWidgetDataSource {
    public static final Comparator<Widget> WIDGET_COMPARATOR = Comparator.comparing(Widget::getzIndex);

    TreeMap<Long, TreeMap<Long, TreeMap<Long, TreeMap<Long, TreeSet<Widget>>>>> groupedData;

    public WidgetDataSource() {
        super(WIDGET_COMPARATOR);
        groupedData = new TreeMap<>(Long::compareTo);
    }


    @Override
    public TreeSet<Widget> getData() {
        return this;
    }

    @Override
    public TreeMap<Long, TreeMap<Long, TreeMap<Long, TreeMap<Long, TreeSet<Widget>>>>> getGroupedData() {
        return groupedData;
    }

    @Override
    public boolean add(Widget widget) {
        boolean result = super.add(widget);
        refreshGroupedData();
        return result;
    }

    @Override
    public boolean delete(Widget o) {
        return remove(o);
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        refreshGroupedData();
        return result;
    }

    /**
     * Обновление сгруппированных данных.
     * При условии, что операции вставки выполняются не часто,
     * можем позволить осуществить перестройку это составной коллекции
     */
    private void refreshGroupedData() {
        groupedData = new TreeMap<>(Long::compareTo);

        for (Widget widget : this) {
            // 1й уровень группировки - по координате posX. Если в коллекции нет ключа widget.posX, добавляем.
            // в качестве value выступает проинициализированная Мапа, в которой будет происходить 2й уровень группировки
            groupedData.computeIfAbsent(widget.getPosX(), k -> new TreeMap<>(Long::compareTo));
            // 2й уровень группировки - по координате posY. Если в коллекции нет ключа widget.posY, добавляем.
            // в качестве value выступает проинициализированная Мапа, в которой будет происходить 3й уровень группировки
            groupedData.get(widget.getPosX())
                    .computeIfAbsent(widget.getPosY(), k -> new TreeMap<>(Long::compareTo));

            // 3й уровень группировки - по ширине виджета. Если в коллекции нет ключа widget.width, добавляем.
            // в качестве value выступает проинициализированная Мапа, в которой будет происходить 4й уровень группировки
            groupedData.get(widget.getPosX())
                    .get(widget.getPosY())
                    .computeIfAbsent(widget.getWidth(), k -> new TreeMap<>(Long::compareTo));

            // 4й уровень группировки - по высоте виджета. Если в коллекции нет ключа widget.height, добавляем.
            // в качестве value выступает коллекция подходящих виджетов
            groupedData.get(widget.getPosX())
                    .get(widget.getPosY())
                    .get(widget.getWidth())
                    .computeIfAbsent(widget.getHeight(), k -> new TreeSet<>(WIDGET_COMPARATOR));

            groupedData.get(widget.getPosX())
                    .get(widget.getPosY())
                    .get(widget.getWidth())
                    .get(widget.getHeight())
                    .add(widget);
        }
    }
}
