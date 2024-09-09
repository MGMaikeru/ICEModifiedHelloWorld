
import java.net.InetAddress;
import java.net.UnknownHostException;
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
                System.out.print("Ingrese el comando (o 'exit' para salir): ");
                String input = scanner.nextLine();
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
        System.out.println("\n- Menú -");
        System.out.println("1. Calcular serie de Fibonacci y factores primos (Número entero positivo)");
        System.out.println("2. Listar interfaces de red (Comando 'listifs')");
        System.out.println("3. Listar puertos y servicios de una dirección IPv4 (Comando 'listports' seguido de la IP)");
        System.out.println("4. Ejecutar comando en el servidor (Iniciando con '!')");
    }

    private static void requeststest(PrinterPrx server) throws UnknownHostException {
        long time = System.currentTimeMillis();
        int throughput = 0;
        int unprocessed = 0;
        int total = 0;
        int missing = 0;
        String username = System.getProperty("user.name");
        String hostname = InetAddress.getLocalHost().getHostName();
        while (System.currentTimeMillis() - time < 1000) {
            String message = username + "@" + hostname + ":" + "listifs";
            Response response = (server.printString(message));
            if (response.responseTime > 0) {
                throughput++;
            } else {
                unprocessed++;
            }
            if (response.value == null || response.value.isEmpty()) {
                {
                    missing++;
                }
            }
            total = throughput + unprocessed + missing;
        }
        System.out.println(" ");
        System.out.println("Tiempos para ejecucion de listifs");
        System.out.println("Throughput: " + throughput + " requests/s");
        System.out.println("Unprocessed: " + unprocessed + " requests/s");
        System.out.println("Missing: " + missing + " requests/s");
        System.out.println("Total: " + total + " requests/s");

        time = System.currentTimeMillis();
        throughput = 0;
        unprocessed = 0;
        missing = 0;
        while (System.currentTimeMillis() - time < 1000) {
            String message = username + "@" + hostname + ":" + "10";
            Response response = (server.printString(message));
            if (response.responseTime > 0) {
                throughput++;
            } else {
                unprocessed++;
            }
            if (response.value == null || response.value.isEmpty()) {
                {
                    missing++;
                }
            }
            total = throughput + unprocessed + missing;
        }
        System.out.println(" ");
        System.out.println("Tiempos para ejecucion de fibonacci");
        System.out.println("Throughput: " + throughput + " requests/s");
        System.out.println("Unprocessed: " + unprocessed + " requests/s");
        System.out.println("Missing: " + missing + " requests/s");
        System.out.println("Total: " + total + " requests/s");

        time = System.currentTimeMillis();
        throughput = 0;
        unprocessed = 0;
        missing = 0;
        while (System.currentTimeMillis() - time < 10000) {
            String message = username + "@" + hostname + ":" + "listports localhost";
            Response response = (server.printString(message));
            if (response.responseTime > 0) {
                throughput++;
            } else {
                unprocessed++;
            }
            if (response.value == null || response.value.isEmpty()) {
                {
                    missing++;
                }
            }
            total = throughput + unprocessed + missing;
        }
        System.out.println(" ");
        System.out.println("Tiempos para ejecucion de nmap");
        System.out.println("Throughput: " + throughput + " requests/s");
        System.out.println("Unprocessed: " + unprocessed + " requests/s");
        System.out.println("Missing: " + missing + " requests/s");
        System.out.println("Total: " + total + " requests/s");

        time = System.currentTimeMillis();
        throughput = 0;
        unprocessed = 0;
        missing = 0;
        while (System.currentTimeMillis() - time < 1000) {
            String message = username + "@" + hostname + ":" + "!java -version";
            Response response = (server.printString(message));
            if (response.responseTime > 0) {
                throughput++;
            } else {
                unprocessed++;
            }
            if (response.value == null || response.value.isEmpty()) {
                {
                    missing++;
                }
            }
        }
        System.out.println(" ");
        System.out.println("Tiempos para ejecucion de comando en consola");
        total = throughput + unprocessed + missing;
        System.out.println("Throughput: " + throughput + " requests/s");
        System.out.println("Unprocessed: " + unprocessed + " requests/s");
        System.out.println("Missing: " + missing + " requests/s");
        System.out.println("Total: " + total + " requests/s");
    }
}