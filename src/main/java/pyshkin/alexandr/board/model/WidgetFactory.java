package pyshkin.alexandr.board.model;

/**
 * Фабрика виджетов. Просто чтобы не нарушать принцип Single Responsibility
 */
public interface WidgetFactory {
    Widget createWidget(Long posX, Long posY, Long zIndex, Long width, Long height);
}
