
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
                printMenu();
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("exit")) break;

                String input = "";
                switch (choice) {
                    case "1":
                        System.out.print("Ingrese un número entero positivo: ");
                        input = scanner.nextLine();
                        break;
                    case "2":
                        System.out.print("Ingrese el comando 'listifs': ");
                        input = scanner.nextLine();
                        break;
                    case "3":
                        System.out.print("Ingrese el comando 'listports' junto a una dirección IPv4: ");
                        input = scanner.nextLine();
                        break;
                    case "4":
                        System.out.print("Ingrese un comando para ejecutar (iniciando con '!'): ");
                        input = scanner.nextLine();
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        continue;
                }

                String message = username + "@" + hostname + ":" + input;
                System.out.println(service.printString(message).value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("\n- Menú -");
        System.out.println("1. Calcular serie de Fibonacci y factores primos");
        System.out.println("2. Listar interfaces de red");
        System.out.println("3. Listar puertos y servicios de una dirección IPv4");
        System.out.println("4. Ejecutar comando en el servidor");
        System.out.println("Escriba 'exit' para salir");
        System.out.print("Elija una opción: ");
    }
}