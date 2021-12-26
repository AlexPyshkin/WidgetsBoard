package pyshkin.alexandr.board.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column
    protected Long posX;
    @Column
    protected Long posY;
    @Column
    protected Long zIndex;
    @Column
    protected Long width;
    @Column
    protected Long height;
    @Column
    protected Date changeDate;

    public Widget() {
    }

    public Widget(Long posX, Long posY, Long zIndex, Long width, Long height) {
        this.posX = posX;
        this.posY = posY;
        this.zIndex = zIndex;
        this.width = width;
        this.height = height;
        this.changeDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosX() {
        return posX;
    }

    public void setPosX(Long posX) {
        this.posX = posX;
    }

    public Long getPosY() {
        return posY;
    }

    public void setPosY(Long posY) {
        this.posY = posY;
    }

    public Long getzIndex() {
        return zIndex;
    }

    public void setzIndex(Long zIndex) {
        this.zIndex = zIndex;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Widget)) {
            return false;
        }

        Widget other = (Widget) obj;

        if ((getId() == null) || (other.getId() == null)) {
            return false;
        }

        if (getId().equals(other.getId())) {
            return true;
        }

        return super.equals(obj);
    }
}
