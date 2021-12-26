package pyshkin.alexandr.board.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pyshkin.alexandr.board.model.Widget;

import java.util.Collection;

@Repository
public interface IWidgetH2Repository extends CrudRepository<Widget, Long> {
    Widget findByzIndex(Long zIndex);

    @Query(value = "Select max(w.z_index) from widget w", nativeQuery = true)
    Long findMaxZIndex();

    @Query(value = "Select w from Widget w where w.posX >= :minX AND w.posY >= :minY and w.posX + w.width <= :maxX AND w.posY + w.height <= :maxY")
    Collection<Widget> findByRectangle(Long minX, Long minY, Long maxX, Long maxY);
}
