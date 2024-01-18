import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeParser {

    protected static final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    protected static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd_H:m:s");
    protected static final DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy-M-d_HH:mm:ss");
    protected static final DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy-M-d_H:m:s");

    protected static boolean parseDateTime(String strDate, DateTimeFormatter formatter) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);

            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
