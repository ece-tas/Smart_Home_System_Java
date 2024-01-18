import java.io.IOException;
import java.time.LocalDateTime;

public class TimeParser extends DateTimeParser {
    private String[] removedTab;
    private String strTimeValue;
    private String strTimeValue2;



    public TimeParser(String[] removedTab, String strTimeValue, String strTimeValue2) {
        super();
        this.removedTab = removedTab;
        this.strTimeValue = strTimeValue;
        this.strTimeValue2 = strTimeValue2;


    }


    public LocalDateTime getLocalDateTime (String time) {

        String strDate = time;
    try {
        if (parseDateTime(strDate, formatter1)) {
            return LocalDateTime.parse(strDate, formatter1);
        }  else if (parseDateTime(strDate, formatter2)) {
            return LocalDateTime.parse(strDate, formatter2);
        } else if (parseDateTime(strDate, formatter3)) {
            return LocalDateTime.parse(strDate, formatter3);
        } else {
            return LocalDateTime.parse(strDate, formatter4);
        }
    } catch (NullPointerException e) {
        e.printStackTrace();
    }

        return null;
    }
    public boolean boolInitialTime () {

        String strDate = removedTab[1];
        return parseDateTime(strDate, formatter1) || parseDateTime(strDate, formatter2) ||
                parseDateTime(strDate, formatter3) || parseDateTime(strDate, formatter4);
    }



    public String strSetInitialTime () {
        String strDate = removedTab[1];
        return strDate;
    }





    public boolean boolTime () {

        String strDate = removedTab[1];
        return parseDateTime(strDate, formatter1) || parseDateTime(strDate, formatter2) ||
                parseDateTime(strDate, formatter3) || parseDateTime(strDate, formatter4);
    }


    public String strSetTime () {
        String strDate = removedTab[1];
        return strDate;
    }




    public boolean boolSwitchTime () {

        String strDate = removedTab[2];
        return parseDateTime(strDate, formatter1) || parseDateTime(strDate, formatter2) ||
                parseDateTime(strDate, formatter3) || parseDateTime(strDate, formatter4);
    }


    public String strSetSwitchTime () {
        String strDate = removedTab[2];
        return strDate;
    }






    public boolean boolSkipMinutes () throws IOException {
        String strMinutes = removedTab[1];
        try {
            int intMinutes = Integer.parseInt(strMinutes);
            try {
                if (intMinutes < 0) {
                    throw new ERROR("Time cannot be reversed!");
                }
                else if (intMinutes == 0) {
                    throw new ERROR("There is nothing to skip!");
                }
                else {
                    return true;
                }
            } catch (ERROR e) {
                FileOutput.writeToFile(String.valueOf(e));
                return false;
            }
        }
        catch (NumberFormatException | IOException e) {
            FileOutput.writeToFile("ERROR: Erroneous command!");
            return false;
        }
    }

    public int getSkipMinutes () {
        String strMinutes = removedTab[1];
        return Integer.parseInt(strMinutes);
    }



}
