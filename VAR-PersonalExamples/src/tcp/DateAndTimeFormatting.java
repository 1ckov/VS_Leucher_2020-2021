package tcp;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class DateAndTimeFormatting {
 
    private static final Date NOW = new Date();
    private static final long NOW_UNIX = NOW.getTime();
    private static final String NOW_LOCALE = DateFormat.getInstance().format(NOW);

    public Date formatAsUnix(final String now){
        Date result = null;
        try {
        long nowLong = Long.parseLong(now);
        result = new Date(nowLong);
        } catch (NumberFormatException e) {
            System.err.println("The given input is not of a known \"Date\" format");
        }
        return result;
    }

    public Date format (String now){
        Date result = null;
        DateFormat df = DateFormat.getInstance();
        try {
            result = df.parse(now);
        }
        catch (ParseException e){
            result = formatAsUnix(now);
        }
        
        return result;
    }
 
 
 
 
 
    public static void main(String[] args) {
        DateAndTimeFormatting testClass = new DateAndTimeFormatting();
        String nowDF = DateAndTimeFormatting.NOW_LOCALE;
        String nowL = Long.toString(DateAndTimeFormatting.NOW_UNIX);
        System.out.println(testClass.format(nowDF));
        System.out.println(testClass.format(nowL));
        System.out.println(testClass.format("loool"));
    }
}