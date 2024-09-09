import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.Object;

import java.util.ArrayList;
import java.util.List;

public class Server{
    public static void main(String[] args) {
        int status = 0;
        try(Communicator communicator = Util.initialize(args, "config.server")){
            ObjectAdapter adapter = communicator.createObjectAdapter("Printer");
            Object object = new PrinterI();
            adapter.add(object, Util.stringToIdentity("Server"));
            adapter.activate();
            System.out.println("Server started...");
            communicator.waitForShutdown();
        }catch(Exception e){

        }
    }

    public static String fibonacci(int number) {
        List<Integer> series = new ArrayList<>();
        int a = 0, b = 1;
        while (number-- > 0) {
            series.add(a);
            int temp = a + b;
            a = b;
            b = temp;
        }
        return series.toString();
    }

    public static String primeFactors(int number) {
        List<Integer>  primes= new ArrayList<>();
        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                primes.add(i);
                number /= i;
            }
        }
        return primes.toString();
    }
    
}