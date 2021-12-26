package pyshkin.alexandr.board.datasource;

import pyshkin.alexandr.board.model.Widget;

import java.util.Map;
import java.util.Set;

/**
 * Расширение интерфейса DataSource.
 * Добавляется специфика виджетов
 */
public interface IWidgetDataSource extends DataSource<Widget>{

    Map<Long, ? extends Map<Long, ? extends Map<Long, ? extends Map<Long, ? extends Set<Widget>>>>> getGroupedData();
}
