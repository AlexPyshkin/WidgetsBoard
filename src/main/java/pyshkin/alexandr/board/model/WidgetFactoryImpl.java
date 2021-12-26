package pyshkin.alexandr.board.model;

import org.springframework.stereotype.Component;

@Component
public class WidgetFactoryImpl implements WidgetFactory {
    // Идентификатор для следующего значения
    private static Long sequence = 1L;

    @Override
    public Widget createWidget(Long posX, Long posY, Long zIndex, Long width, Long height) {
        Widget widget = new Widget(posX, posY, zIndex, width, height);
        widget.setId(sequence++);
        return widget;
    }
}
