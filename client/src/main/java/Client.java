
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Demo.Response;
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
                printMenu();
                System.out.print("Ingrese el comando: ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("testQA")) requeststest(service);
                if (input.equalsIgnoreCase("exit")) break;
                String message = username + "@" + hostname + ":" + input;
                long time= System.currentTimeMillis();
                Response response = service.printString(message);

                System.out.println("Server response: " + response.value);
                System.out.println("Time taken total: " + (System.currentTimeMillis() - time) + "ms");
                System.out.println("Received response or processing time of " + response.responseTime + "ms");
                System.out.println("latency is " + (System.currentTimeMillis() - time - response.responseTime) + "ms");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("- Calcular serie de Fibonacci y factores primos (Ingrese un número entero positivo)");
        System.out.println("- Listar interfaces de red disponibles (Use el comando: 'listifs')");
        System.out.println("- Escanear puertos y servicios de una dirección IPv4 (Use el comando: 'listports <IP>')");
        System.out.println("- Ejecutar un comando en el servidor (Precedido por '!')");
        System.out.println("- Realizar pruebas de atributos de calidad (Use el comando: 'testQA')");
        System.out.println("- Salir (Use el comando: 'exit')");
    }

    private static void requeststest(PrinterPrx server) throws UnknownHostException {
        testOperation(server, "listifs", 1000, "Tiempos para ejecucion de listifs");
        testOperation(server, "10", 1000, "Tiempos para ejecucion de fibonacci");
        testOperation(server, "listports localhost", 10000, "Tiempos para ejecucion de nmap");
        testOperation(server, "!java -version", 1000, "Tiempos para ejecucion de comando en consola");
    }

    private static void testOperation(PrinterPrx server, String command, long testDuration, String operationName) throws UnknownHostException {
        long startTime = System.currentTimeMillis();
        int throughput = 0;
        int unprocessed = 0;
        int missing = 0;
        int total = 0;
        List<Long> responseTimes = new ArrayList<>();

        String username = System.getProperty("user.name");
        String hostname = InetAddress.getLocalHost().getHostName();
        String message = username + "@" + hostname + ":" + command;

        while (System.currentTimeMillis() - startTime < testDuration) {
            long time = System.currentTimeMillis();
            Response response = server.printString(message);
            long responseTime = System.currentTimeMillis() - time;

            responseTimes.add(responseTime);

            if (response.responseTime > 0) {
                throughput++;
            } else {
                unprocessed++;
            }
            if (response.value == null || response.value.isEmpty()) {
                missing++;
            }
            total++;
        }

        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0);
        double jitter = calculateJitter(responseTimes);
        double missingRate = (double) missing / (testDuration / 1000);
        double unprocessRate = (double) unprocessed / (testDuration / 1000);

        System.out.println("\n" + operationName);
        System.out.println("Throughput: " + throughput + " requests/s");
        System.out.println("Unprocessed: " + unprocessed + " requests/s");
        System.out.println("Missing: " + missing + " requests/s");
        System.out.println("Total: " + total + " requests/s");
        System.out.println("Average Response Time: " + String.format("%.2f", avgResponseTime) + " ms");
        System.out.println("Max Response Time: " + maxResponseTime + " ms");
        System.out.println("Jitter: " + String.format("%.2f", jitter) + " ms");
        System.out.println("Missing Rate: " + String.format("%.2f", missingRate) + " events/s");
        System.out.println("Unprocess Rate: " + String.format("%.2f", unprocessRate) + " events/s");
    }

    private static double calculateJitter(List<Long> responseTimes) {
        if (responseTimes.size() < 2) return 0;
        double sumDifferences = 0;
        for (int i = 1; i < responseTimes.size(); i++) {
            sumDifferences += Math.abs(responseTimes.get(i) - responseTimes.get(i - 1));
        }
        return sumDifferences / (responseTimes.size() - 1);
    }
}