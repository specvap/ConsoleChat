package Client;

/**
 * Created by Windows on 25.02.2017.
 */
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    //IP и PORT сервера
    public static final String IP = "127.0.0.1";
    public static final int PORT = 7071;

    public static void main(String[] args) {
        Client clientExample = new Client();
        clientExample.start();
    }
    public void start(){
        try {

            Socket socket = new Socket(IP, PORT);

            Gson gson = new Gson();

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputConsole = "";
            //Создание получателя сообщений в отдельном потоке
            ClientReceiver clientReceiver = new ClientReceiver( serverReader,gson);
            clientReceiver.start();
            //Чтение потока ввода с консоли
            while (!(inputConsole = console.readLine()).equals("exit")&inputConsole!="") {
                //Превращение строки в объект Message
                Message message = parseMessage(inputConsole);
                //Превращение объекта Message в Json
                inputConsole = gson.toJson(message);
                //Вывод строки Json в поток вывода сокета
                writer.println(inputConsole);
                writer.flush();
            }
            writer.close();
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Парсинг сообщения
    public Message parseMessage(String inputConsole){

        //Получаем первый символ
        char firstChar = inputConsole.charAt(0);
        String[] split;
        Message message;

        if(firstChar=='@'){
            //Отделяем подстроку со второго символа
            inputConsole=inputConsole.substring(1);
            //Делим на две части
            split = inputConsole.split(":");
            //Убираем лишние пробелы
            split[0]=split[0].trim();
            split[1]=split[1].trim();

            String receiver=split[0];
            String body=split[1];

            message = new Message(null,receiver,body);
        }
        else{
            message = new Message(null,null,inputConsole);
        }
        //Возвращаем полученное сообщение
        return message;
    }
}