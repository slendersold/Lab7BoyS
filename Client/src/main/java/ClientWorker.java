import All.Instrumentum.SendRecieve;
import All.Instrumentum.ServerMessage;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;


public class ClientWorker {
    private SendRecieve manager;
    private DatagramChannel datagramChannel = DatagramChannel.open();
    private SocketAddress socketAddress;
    private Agregator stringCommandManager;

    {
        stringCommandManager = new StringCommandManager();
    }

    public ClientWorker(SocketAddress socketAddress) throws IOException {
        this.socketAddress = socketAddress;
        manager = new SendRecieveServerMessage(socketAddress, datagramChannel);
        datagramChannel.configureBlocking(false);
    }

    public void startWork() throws IOException {
        String commandString = "";
        try (Scanner commandReader = new Scanner(System.in)) {
            System.out.print("//: ");
            while (!commandString.equals("exit")) {
                if (!commandReader.hasNextLine()) {
                    break;
                } else {
                    try {
                        commandString = commandReader.nextLine();
                        Command command = stringCommandManager.getCommandFromString(commandString);
                        if (command != null) {
                            if (this.connectionCheck()) {
                                manager.send(command);
                                ServerMessage message = manager.receive();
                                if (message != null) {
                                    if (message.getMessage() != null)
                                        System.out.println(message.getMessage());
                                    if (message.getVehicles() != null)
                                        for (Vehicle p : message.getVehicles()) System.out.println(p);
                                } else System.out.println("Ответ cервера некорректен");
                            } else System.out.println("Подключение потеряно.");
                        } else {
                            System.out.println("Команда не была отправлена.");
                        }
                    } catch (NullPointerException e) {
                        System.out.println("NullPointerException! Скорее всего, неверно указана дата при создании объекта.");
                    }
                }
                System.out.print("//: ");
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка при попытке считать ответ от сервера.");
        }
    }

    public boolean connectionCheck() throws IOException, InterruptedException {
        System.out.println("Проверка соединения:");
        datagramChannel.connect(socketAddress);
        datagramChannel.disconnect();
        datagramChannel.socket().setSoTimeout(1000);
        manager.send(new Command(CommandList.CHECK, "Привет"));
        ServerMessage receive = manager.receive();
        if (receive != null) {
            System.out.println(receive.getMessage());
            if (receive.getMessage().equals("Good connect. Hello from server!")) {
                return true;
            } else {
                System.out.println("Неверное подтверждение от сервера!");
                return false;
            }
        } else return false;
    }

}
