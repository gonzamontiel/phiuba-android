package mont.gonzalo.phiuba.model;

import java.util.Date;

/**
 * Created by Gonzalo Montiel on 2/6/17.
 */
public interface Calendable {
    String getTitle();
    String getDescription();
    Date getStart();
    Date getEnd();
    String getLocation();
}
