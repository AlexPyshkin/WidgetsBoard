package pyshkin.alexandr.board.service;

import pyshkin.alexandr.board.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pyshkin.alexandr.board.model.Widget;
import pyshkin.alexandr.board.model.WidgetFactory;
import pyshkin.alexandr.board.repository.IWidgetRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import static pyshkin.alexandr.board.utils.CheckUtils.checkNotNull;
import static pyshkin.alexandr.board.utils.CheckUtils.checkPositive;

@Service
public class WidgetService implements IWidgetService {

    final private IWidgetRepository widgetRepository;
    final private WidgetFactory widgetFactory;

    @Autowired
    public WidgetService(IWidgetRepository widgetRepository, WidgetFactory widgetFactory) {
        this.widgetRepository = widgetRepository;
        this.widgetFactory = widgetFactory;
    }

    @Override
    @Transactional
    public Widget createWidget(Widget widget) {
        return createOrUpdate(null, widget);
    }

    @Override
    @Transactional
    public Widget updateWidget(Long id, Widget updatingWidget) {
        checkNotNull(id, "Идентификатор виджета не может быть NULL");

        return createOrUpdate(id, updatingWidget);
    }

    @Override
    @Transactional
    public void removeWidget(Long id) {
        widgetRepository.removeWidget(id);
    }

    @Override
    public Widget getWidget(Long id) {
        return widgetRepository.findById(id);
    }

    @Override
    public Collection<Widget> getAllWidget() {
        return widgetRepository.findAllWidgets();
    }

    @Override
    public Collection<Widget> findByRectangle(Long minX, Long minY, Long maxX, Long maxY) {
        return widgetRepository.findByRectangle(minX, minY, maxX, maxY);
    }


    /**
     * Создание нового виджета или обновление существующего
     * @param id идентификатор редактируемого виджета. Если не задан - создается новый виджет
     * @param updatingWidget - объект, хранящий новое состояние виджета
     * @return
     */
    private Widget createOrUpdate(Long id, Widget updatingWidget) {
        checkNotNull(updatingWidget.getPosX(), "Не задана координата X");
        checkNotNull(updatingWidget.getPosY(), "Не задана координата Y");
        checkPositive(updatingWidget.getHeight(), "Высота виджета должна быть больше 0");
        checkPositive(updatingWidget.getWidth(), "Ширина виджета должна быть больше 0");

        Widget returningWidget;
        Long zIndex;
        if (Objects.isNull(updatingWidget.getzIndex()))
            zIndex = getNextZIndex();
        else {
            zIndex = updatingWidget.getzIndex();
            shiftExistingZIndexes(zIndex);
        }

        if (id == null) {
            returningWidget = widgetFactory.createWidget(updatingWidget.getPosX(), updatingWidget.getPosY(), zIndex, updatingWidget.getWidth(), updatingWidget.getHeight());
        } else {
            updatingWidget.setId(id);
            returningWidget = widgetRepository.findById(updatingWidget.getId());
            // Согласно спецификации, PUT метод должен создавать ресурс при его отсутствии, однако:
            // 1. Сервис не зависит от контроллера, и типа HTTP запроса, на который будет вызываться
            // 2. Указание идентификатора нового объекта вручную может противоречить стратегии генерации идентификаторов
            // 3. Если идентификатор указан неверно - пользователь не получит желаемого результата. Вместо обновления виджета с корректным id
            // мы просто получим новый объект
            // Поэтому при отсутствии объекта с целевым идентификатором кидаем ошибку
            if (Objects.isNull(returningWidget))
                throw new EntityNotFoundException(Widget.class, id);

            returningWidget.setPosX(updatingWidget.getPosX());
            returningWidget.setPosY(updatingWidget.getPosY());
            returningWidget.setzIndex(zIndex);
            returningWidget.setWidth(updatingWidget.getWidth());
            returningWidget.setHeight(updatingWidget.getHeight());
            returningWidget.setChangeDate(new Date());
        }
        returningWidget = widgetRepository.saveWidget(returningWidget);

        return returningWidget;
    }

    private Long getNextZIndex() {
        return widgetRepository.getNextZIndex();
    }

    // TODO переписать реализацию без риска StackOverFlowError
    /**
     * Сдвигает Z-индекс виджета (и всех последующих при необходимости) "вверх"
     */
    private void shiftExistingZIndexes(Long zIndex){
        // если виджет с таким zIndex - находим его и увеличиваем его zIndex
        Widget widget = widgetRepository.findByZIndex(zIndex);
        Long next = zIndex+1;
        if (Objects.nonNull(widget)) {
            shiftExistingZIndexes(next);
            widget.setzIndex(next);
            widgetRepository.saveWidget(widget);
        }
    }
}
