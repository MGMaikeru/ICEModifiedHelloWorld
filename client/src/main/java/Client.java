
import java.net.InetAddress;
import java.util.Scanner;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

import Demo.PrinterPrx;

public class Client{
    public static void main(String[] args) {
        try(Communicator communicator = Util.initialize(args, "config.client")){
            PrinterPrx service = PrinterPrx.checkedCast(communicator.propertyToProxy("Printer.Proxy"));
            if (service == null) throw new Error("Invalid proxy");

            Scanner scanner = new Scanner(System.in);
            String username = System.getProperty("user.name");
            String hostname = InetAddress.getLocalHost().getHostName();

            while (true) { 
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;
                String message = username + "@" + hostname + ":" + input;
                System.out.println(service.printString(message).value);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}