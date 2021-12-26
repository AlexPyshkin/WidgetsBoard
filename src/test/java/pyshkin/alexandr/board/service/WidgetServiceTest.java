package pyshkin.alexandr.board.service;

import pyshkin.alexandr.board.exception.InvalidValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pyshkin.alexandr.board.model.Widget;
import pyshkin.alexandr.board.model.WidgetFactory;
import pyshkin.alexandr.board.repository.IWidgetRepository;

import java.util.Collection;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WidgetServiceTest {

    @Autowired
    private IWidgetService widgetService;
    @Autowired
    private IWidgetRepository widgetRepository;
    @Autowired
    private WidgetFactory widgetFactory;

    @BeforeEach
    void setUp() {
        widgetService.createWidget(widgetFactory.createWidget(0L, 0L, 0L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(100L, 100L, 1L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(200L, 200L, 2L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(-100L, 100L, 3L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(100L, -100L, 7L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(-200L, 200L, 8L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(200L, -200L, 9L, 100L, 100L));
        widgetService.createWidget(widgetFactory.createWidget(900L, 905L, -5L, 777L,  777L));
    }


    @Test
    void createWidget() {
        // Проверить заполненность обязательных полей
        Widget widget = widgetService.createWidget(widgetFactory.createWidget(1L, 2L, 0L, 100L, 200L));
        Assertions.assertNotNull(widget.getId());
        assertEquals(1L, widget.getPosX());
        assertEquals(2L, widget.getPosY());
        assertEquals(0L, widget.getzIndex());
        assertEquals(100L, widget.getWidth());
        assertEquals(200L, widget.getHeight());

        // Наличие запрета ввода недопустимых значений
        Throwable thrown = catchThrowable(() -> {widgetService.createWidget(widgetFactory.createWidget(null, 2L, 99L, 100L, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.createWidget(widgetFactory.createWidget(1L, null, 100L, 100L, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.createWidget(widgetFactory.createWidget(1L, 2L, 101L, null, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.createWidget(widgetFactory.createWidget(1L, 2L, 102L, 100L, null));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.createWidget(widgetFactory.createWidget(1L, 2L, 103L, -100L, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.createWidget(widgetFactory.createWidget(1L, 2L, 104L, 100L, -200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
    }

    @Test
    void updateWidget() {
        // Проверить заполненность обязательных полей
        Widget widget = widgetService.createWidget(widgetFactory.createWidget(1L, 2L, 0L, 100L, 200L));
        Long updatingId = widget.getId();
        widget = widgetService.updateWidget(updatingId, widgetFactory.createWidget(3L, 4L, -5L, 300L, 400L));

        assertEquals(3L, widget.getPosX());
        assertEquals(4L, widget.getPosY());
        assertEquals(-5L, widget.getzIndex());
        assertEquals(300L, widget.getWidth());
        assertEquals(400L, widget.getHeight());
        // Проверить, что идентификатор объекта не меняется
        assertEquals(updatingId, widget.getId());

        // Наличие запрета ввода недопустимых значений
        Throwable thrown = catchThrowable(() -> {widgetService.updateWidget(updatingId, widgetFactory.createWidget(null, 2L, 99L, 100L, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.updateWidget(updatingId, widgetFactory.createWidget(1L, null, 100L, 100L, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.updateWidget(updatingId, widgetFactory.createWidget(1L, 2L, 101L, null, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.updateWidget(updatingId, widgetFactory.createWidget(1L, 2L, 102L, 100L, null));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.updateWidget(updatingId, widgetFactory.createWidget(1L, 2L, 103L, -100L, 200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
        thrown = catchThrowable(() -> {widgetService.updateWidget(updatingId, widgetFactory.createWidget(1L, 2L, 104L, 100L, -200L));});
        assertThat(thrown).isInstanceOf(InvalidValueException.class);
    }

    @Test
    void getWidget() {
        Widget created = widgetService.createWidget(widgetFactory.createWidget(1L, 2L, null, 100L, 200L));

        Widget found = widgetService.getWidget(created.getId());

        //виджет возвращается со всеми обязательными полями
        assertNotNull(found.getPosX());
        assertNotNull(found.getPosY());
        assertNotNull(found.getzIndex());
        assertThat(found.getHeight()!=null && found.getHeight() > 0).isTrue();
        assertThat(found.getWidth()!=null && found.getWidth() > 0).isTrue();
    }

    @Test
    void getAllWidget() {
        int size = widgetService.getAllWidget().size();
        widgetService.createWidget(widgetFactory.createWidget(1L, 2L, null, 100L, 200L));
        // Убедиться что виджеты добавляются
        assertThat(widgetService.getAllWidget().size() == size + 1).isTrue();
    }

    @Test
    void removeWidget() {
        Collection<Widget> all = widgetService.getAllWidget();
        assertFalse(all == null || all.isEmpty());
        // зафиксировали исходное количество элементов
        int size = all.size();
        Long delId = all.stream().findFirst().get().getId();

        widgetService.removeWidget(delId);

        // После удаления виджет не должен существовать
        assertNull(widgetService.getWidget(delId));
        // проверить количество виджетов до и после удаления
        assertThat(widgetService.getAllWidget().size() == size - 1).isTrue();

        // Удаление несуществующего виджета не создает ошибку
        widgetService.removeWidget(delId);
        assertThat(widgetService.getAllWidget().size() == size - 1).isTrue();
    }

    @Test
    void shiftExistingZIndexes() {
        LinkedList<Widget> widgetsBeforeInsert = new LinkedList<>();
        widgetsBeforeInsert.add(widgetRepository.findByZIndex(1L));
        widgetsBeforeInsert.add(widgetRepository.findByZIndex(2L));
        widgetsBeforeInsert.add(widgetRepository.findByZIndex(3L));

        widgetService.createWidget(widgetFactory.createWidget(300L, 300L, 2L, 300L, 300L));

        LinkedList<Widget> widgetsAfterInsert = new LinkedList<>();
        widgetsAfterInsert.add(widgetRepository.findByZIndex(1L));
        widgetsAfterInsert.add(widgetRepository.findByZIndex(2L));
        widgetsAfterInsert.add(widgetRepository.findByZIndex(3L));
        widgetsAfterInsert.add(widgetRepository.findByZIndex(4L));

        assertEquals(widgetsBeforeInsert.get(0), widgetsAfterInsert.get(0));
        assertNotEquals(widgetsBeforeInsert.get(1), widgetsAfterInsert.get(1));
        assertNotEquals(widgetsBeforeInsert.get(2), widgetsAfterInsert.get(2));
        assertEquals(widgetsBeforeInsert.get(1), widgetsAfterInsert.get(2));
        assertEquals(widgetsBeforeInsert.get(2), widgetsAfterInsert.get(3));
    }
}