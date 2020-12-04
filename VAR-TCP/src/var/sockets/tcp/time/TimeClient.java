package var.sockets.tcp.time;

import java.io.InputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class TimeClient {

    public static Date formatAsUnix(final String now){
		Date result = null;
		//We try to Parse the String as Long
		long nowLong = Long.parseLong(now);
		//and try to make a Date out of it
		result = new Date(nowLong);
       //} catch (NumberFormatException e) {
       //     System.err.println("The given input is not of a known \"Date\" format");
       // }
        return result;
    }

    public static Date format (String now){
        Date result = null;
		//We get an instance of the System Date Format
        DateFormat df = DateFormat.getInstance();
		//Then we try and parse the string as the given Format
		try {
            result = df.parse(now);
        }
        catch (ParseException e){
			//If we get an Error we try Formating it as a Unix Long type
            result = formatAsUnix(now);
        }
        
        return result;
    }

	public static void main(String[] args) {

		//Open a connection to Time Server
		try (Socket socket = new Socket(args[0], Integer.parseInt(args[1]));

				//Open a stream to read from time Server
				InputStream in = socket.getInputStream()) {

			//Initialize a String builder to Format our char array into a string		
			StringBuilder stringBuilder = new StringBuilder();
			
			//Initialise a temp buffer for our chars	
			int c;
			
			//Start Reading in char after char and saving it into the string builder
			while ((c = in.read()) != -1) {
				stringBuilder.append((char) c);
			}
			
			//We save the results as a String
			String result = stringBuilder.toString();
			
			//And we try to format it, it should be StringFormat or long agnostic
			Date now = format(result);
			System.out.println(now);
			
		}catch (NumberFormatException e) {
			System.err.println("The given input is not of a known \"Date\" format");
		}catch (Exception e) {
			System.err.println(e);
		}
	}
}
