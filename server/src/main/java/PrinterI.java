
import com.zeroc.Ice.Current;

import Demo.Printer;
import Demo.Response;

public class PrinterI implements Printer{
    @Override
    public Response printString(String message, Current current){
        System.out.println(message);
        String result = "";
        long time=0;
        String[] splitMessage = message.split(":", 2);
        String userHost = splitMessage[0];
        String service = splitMessage[1];

        if (service.trim().equalsIgnoreCase("listifs")) {
            result = Server.listInterfaces();
            System.out.println(userHost + ":\n" + result);
        } else if (service.matches("\\d+")) {
            int number = Integer.parseInt(service);
            String fibSeries = Server.fibonacci(number);
            String primeFactors = Server.primeFactors(number);
            System.out.println(userHost + ": Fibonacci series for " + number + " is: " + fibSeries);
            result = "Prime factors for " + number + " is:" + primeFactors;
        } else {
            result = "Unknown command.";
            System.out.println(userHost + ": " + result);
        }
        return new Response(0, result);
    }
}