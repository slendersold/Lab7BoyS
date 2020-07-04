import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LoginSignin {

    public static void signin(ObjectOutputStream SendtoServer, ObjectInputStream GetfromServer) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        System.out.println("Добро пожаловать в приложение! Для работы с коллекцией необходимо авторизироваться.\n" +
                "Для этого введите в консоль log, если у вас уже есть аккаунт, или reg для регистрации");
        Scanner in = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            if (!in.hasNextLine()) {
                break;
            }
            String input = in.nextLine();

            switch (input) {
                case ("log"): {
                    System.out.println("Введите ваш логин");
                    String login = in.nextLine();
                    System.out.println("Введите ваш пароль");
                    String password = in.nextLine();

                    SendtoServer.writeObject("login");
                    SendtoServer.writeObject(login);
                    byte[] a = MessageDigest.getInstance("SHA-512").digest(password.getBytes());
                    SendtoServer.writeObject(new String(a));
                    String response = (String) GetfromServer.readObject();
                    if (response.equals("Ok")) {
                        System.out.println("Вы успешно вошли. Для получения справки по доступным командам введите help");
                        flag = false;
                    } else
                        System.out.println(response);
                    break;
                }
                case ("reg"): {
                    System.out.println("Введите ваш логин");
                    String login = in.nextLine();
                    System.out.println("Введите ваш пароль");
                    String password = in.nextLine();

                    SendtoServer.writeObject("reg");
                    SendtoServer.writeObject(login);
                    byte[] a = MessageDigest.getInstance("SHA-512").digest(password.getBytes());
                    SendtoServer.writeObject(new String(a));
                    String response = (String) GetfromServer.readObject();
                    if (response.equals("Ok")) {
                        System.out.println("Вы успешно вошли. Для получения справки по доступным командам введите help");
                        flag = false;
                    } else
                        System.out.println(response);
                    break;
                }
                default:
                    break;

            }

        }

    }

}
