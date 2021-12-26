package pyshkin.alexandr.board.repository;

import pyshkin.alexandr.board.exception.InvalidValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pyshkin.alexandr.board.model.Widget;
import pyshkin.alexandr.board.model.WidgetFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static pyshkin.alexandr.board.datasource.WidgetDataSource.WIDGET_COMPARATOR;

@SpringBootTest
class WidgetRepositoryTest {

    @Autowired
    private IWidgetRepository widgetRepository;
    @Autowired
    private WidgetFactory widgetFactory;

    @BeforeEach
    void setUp() {
        widgetRepository.saveWidget(widgetFactory.createWidget(0L, 0L, 0L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(100L, 100L, 1L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(200L, 200L, 2L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(-100L, 100L, 3L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(100L, -100L, 7L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(-200L, 200L, 8L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(200L, -200L, 9L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(900L, 905L, -5L, 222L, 222L));
    }


    @Test
    void zIndexGeneration() {
        Collection<Widget> all = widgetRepository.findAllWidgets();
        assertFalse(all == null || all.isEmpty());
        // зафиксировали исходное количество элементов
        int size = all.size();
        // Наибольший zIndex после инициализации данных, в дальнейших проверках ориентироваться на него,
        // чтобы последующие изменения в @BeforeEach не оказали влияния на результаты теста
        Long max = all.stream().map(Widget::getzIndex).max(Long::compareTo).orElse(null);
        assertNotNull(max);

        assertEquals(max + 1, widgetRepository.getNextZIndex());

        // Если добавлен виджет с Z-индексом, бОльшим, чем существует в системе, функция возвращает следующее за ним значение
        widgetRepository.saveWidget(widgetFactory.createWidget(33L, 55L, max + 10, 100L, 100L));
        assertEquals(max + 11, widgetRepository.getNextZIndex());

        widgetRepository.saveWidget(widgetFactory.createWidget(33L, 55L, Long.MAX_VALUE, 100L, 100L));
        //Если добавлен виджет с максимально допустимым значением Z-индекса, последующее значение приведет к переполнению памяти,
        // старший бит отбросится и получим наименьшее допустимое значение для используемого типа. Это грубое нарушение бизнес логики
        // потому ожидаем исключение
        Throwable thrown = catchThrowable(() -> {
            widgetRepository.getNextZIndex();
        });
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
    }

    @Test
    void findByRectangle() {
        Set<Widget> expected = new HashSet<>();
        expected.add(widgetRepository.saveWidget(widgetFactory.createWidget(500L, 500L, 200L, 100L, 100L)));
        expected.add(widgetRepository.saveWidget(widgetFactory.createWidget(500L, 550L, 201L, 100L, 100L)));
        expected.add(widgetRepository.saveWidget(widgetFactory.createWidget(500L, 500L, 202L, 100L, 150L)));
        // и виджеты, которые не попадают в результат поиска
        widgetRepository.saveWidget(widgetFactory.createWidget(550L, 550L, 203L, 100L, 100L));
        widgetRepository.saveWidget(widgetFactory.createWidget(500L, 500L, 204L, 101L, 150L));
        widgetRepository.saveWidget(widgetFactory.createWidget(500L, 500L, 205L, 100L, 151L));
        widgetRepository.saveWidget(widgetFactory.createWidget(501L, 500L, 206L, 100L, 150L));
        widgetRepository.saveWidget(widgetFactory.createWidget(500L, 501L, 207L, 100L, 150L));

        Collection<Widget> actual = widgetRepository.findByRectangle(500L, 500L, 600L, 650L);
        assertArrayEquals(expected.stream().sorted(WIDGET_COMPARATOR).toArray()
                , actual.stream().sorted(WIDGET_COMPARATOR).toArray());
    }
}