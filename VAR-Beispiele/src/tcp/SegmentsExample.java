public class SegmentsExample {
/** 
- TCP uses one port for in and out communication
- TCP uses so called Segments
- TCP Assures that all the packets get to the expected Location, and also in the correct order
- TCP Server Socket != ClientSocket => 
I) ServerSocket.accept()<So called Welcome Socket> at port 45623 blocks and waits for a client to connect
II) ClietSocket.connect() at port 1234 connect to ServerSocket at 45623
III) ServerSocket opens a new port at 12323 and tells ClientSocket to talk over it from now on
IV) ServerSocket at port 45623 is now open again and awaits a new client.
Rinse and Repeat
*/


    

    
}
