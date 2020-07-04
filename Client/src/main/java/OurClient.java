import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.channels.UnresolvedAddressException;
import java.util.Scanner;

/**
 * О, наш клиент
 * @author slendersold
 * @author Vsevolod01
 */
public class OurClient {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Для начала работы с коллекцией введите адрес сервера в формате \"адрес:порт\" или 'exit' для завершения программы.");
            System.out.print("//: ");
            if (!input.hasNextLine()) {
                break;
            }
            String inputString = input.nextLine();
            if (inputString.equals("exit")) {
                break;
            } else {
                try {
                    String[] trimString = inputString.trim().split(":", 2);
                    String addres = trimString[0];
                    int port = Integer.valueOf(trimString[1]);
                    if (port < 0 || port > 65535) {
                        System.out.println("Порт - число от 0 до 65535.");
                        continue;
                    }
                    SocketAddress socketAddress = new InetSocketAddress(addres, port);
                    System.out.println("Запуск прошёл успешно. Порт: " + port + ". Адрес: " + socketAddress);
                    ClientWorker worker = new ClientWorker(socketAddress);
                    if (worker.connectionCheck()) {
                        worker.startWork();
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка в записи номера порта.");
                } catch (IndexOutOfBoundsException | UnresolvedAddressException e) {
                    System.out.println("Адрес введён некорректно.");
                } catch (PortUnreachableException e) {
                    System.out.println("Похоже, сервер по этому адресу недоступен");
                } catch (InterruptedException e) {
                    System.out.println("Не знаю как, но InterruptedException. Обратитесь в тех. поддержку, которой нет.");
                } catch (IOException e) {
                    System.out.println("Не знаю как, но IOException. Обратитесь в тех. поддержку, которой нет.");
                }
            }
        }
        System.out.println("Работа программы завершена.");
        System.exit(0);
    }
}
