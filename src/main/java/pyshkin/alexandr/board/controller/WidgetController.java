package pyshkin.alexandr.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pyshkin.alexandr.board.model.Widget;
import pyshkin.alexandr.board.service.IWidgetService;

import java.util.Collection;

@RestController
@RequestMapping("widget")
public class WidgetController {

    private final IWidgetService widgetService;

    @Autowired
    public WidgetController(IWidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PutMapping()
    public Widget createWidget(@RequestBody Widget newWidget) {
        return widgetService.createWidget(newWidget);
    }

    @PutMapping("/{id}")
    public Widget updateWidget(
            @PathVariable("id") Long id,
            @RequestBody Widget updatedWidget) {
        return widgetService.updateWidget(id, updatedWidget);
    }

    @GetMapping("/{id}")
    public Widget getWidget(@PathVariable Long id) {
        return widgetService.getWidget(id);
    }

    @GetMapping
    public Collection<Widget> getAllWidgets() {
        return widgetService.getAllWidget();
    }

    @DeleteMapping("/{id}")
    public void removeWidget(@PathVariable Long id) {
        widgetService.removeWidget(id);
    }

    @GetMapping("/byRect")
    public Collection<Widget> getWidgetsByRect(@RequestParam Long minX,
                                               @RequestParam Long minY,
                                               @RequestParam Long maxX,
                                               @RequestParam Long maxY) {
        return widgetService.findByRectangle(minX, minY, maxX, maxY);
    }
}
