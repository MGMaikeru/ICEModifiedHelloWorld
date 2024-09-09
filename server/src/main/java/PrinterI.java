
import com.zeroc.Ice.Current;

import Demo.Printer;
import Demo.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrinterI implements Printer{
    @Override
    public Response printString(String message, Current current){
        System.out.println(message);
        String result = "";
        long time=0;
        String[] splitMessage = message.split(":", 2);
        String userHost = splitMessage[0];
        String service = splitMessage[1];

        if (service.matches("\\d+")) {
            time = System.currentTimeMillis();
            int number = Integer.parseInt(service);
            String fibSeries = fibonacci(number);
            String primeFactors = primeFactors(number);
            System.out.println(userHost + ": Fibonacci series for " + number + " is: " + fibSeries);
            result = "Prime factors for " + number + " is:" + primeFactors;
        } else if (service.startsWith("listifs")) {
            time = System.currentTimeMillis();
            String interfaces = listInterfaces();
            System.out.println(userHost + ": Network interfaces: " + interfaces);
            result = interfaces;
        } else if (service.startsWith("listports")) {
            time = System.currentTimeMillis();
            String[] parts = service.split(" ");
            if (parts.length > 1) {
                String ipAddress = parts[1];
                result = listPortsServices(ipAddress);
                System.out.println(userHost + ": Open ports for " + ipAddress + ": " + result);
            } else {
                result = "Error: No IP address provided.";
            }
        }else if(service.startsWith("!")){
            time = System.currentTimeMillis();
            String command = service.substring(1);
            result = executeCMD(command);
            System.out.println(userHost + ": Command execution result: " + result);
        } else {
            result = "Unknown command.";
            System.out.println(userHost + ": " + result);
        }
        if (time == 0) {
            return new Response(0, result);
        }
        long timetotal = System.currentTimeMillis() - time;
        return new Response(timetotal, result);
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

    public static String listInterfaces() {
        StringBuilder sb = new StringBuilder();
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface netint : interfaces) {
                sb.append(String.format("Display name: %s, Name: %s%n",
                        netint.getDisplayName(), netint.getName()));
            }
        } catch (SocketException e) {
            sb.append("Error listing network interfaces: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public static String listPortsServices(String ip){
        StringBuilder ports = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("nmap -p- " + ip);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                ports.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            ports.append("Error executing nmap command.");
        }
        return ports.toString();
    }

    public static String executeCMD(String command){
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            result.append("Error executing command.");
        }
        return result.toString();
    }
}