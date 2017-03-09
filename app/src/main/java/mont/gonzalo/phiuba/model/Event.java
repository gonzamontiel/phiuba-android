package mont.gonzalo.phiuba.model;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private String title;
    private Date start;
    private Date end;
    private Date parsedDate;
    private Extra extra;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public String getImg() {
        return getExtra().getImage();
    }

    public String getText() {
        return getExtra().getInfo();
    }

    public void setParsedDate(Date parsedDate) {
        this.parsedDate = parsedDate;
    }
}