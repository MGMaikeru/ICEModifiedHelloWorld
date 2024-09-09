
import com.zeroc.Ice.Current;

import Demo.Printer;
import Demo.Response;

public class PrinterI implements Printer{
    @Override
    public Response printString(String message, Current current){
        System.out.println(message);
        String result = "";
        long time=0;
        return new Response(0, result);
    }
}