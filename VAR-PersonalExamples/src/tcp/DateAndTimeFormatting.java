package tcp;

import java.util.Date;
import java.text.DateFormat;

public class DateAndTimeFormatting {
 
    private static final Date NOW = new Date();
    private static final long NOW_UNIX = NOW.getTime();
    private static final String NOW_LOCALE = DateFormat.getInstance().format(NOW);

    public Date formatAsUnix(long now){
        Date result = null; 
        result = new Date(now);
        return result;
    }

    public Date formatAsLocale (String now){
        Date result = null;
        result = new Date(now);
        return result;
    }
 
 
 
 
 
    public static void main(String[] args) {
        
    }
}