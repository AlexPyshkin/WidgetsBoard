package pyshkin.alexandr.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pyshkin.alexandr.board.datasource.WidgetDataSource;
import pyshkin.alexandr.board.repository.IWidgetRepository;
import pyshkin.alexandr.board.repository.WidgetH2Repository;
import pyshkin.alexandr.board.repository.WidgetInnerDSRepository;

@Configuration
@ComponentScan
public class BoardConfiguration {
    @Bean
    public IWidgetRepository widgetRepository(WidgetDataSource widgetDataSource) {
//        return new WidgetH2Repository();
        return new WidgetInnerDSRepository(widgetDataSource);
    }

}
