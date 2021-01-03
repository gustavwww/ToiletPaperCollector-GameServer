import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class TCPClient implements Runnable {

    private final String host;
    private final int port;

    private Socket socket;
    private PrintWriter writer;

    private Queue<String> queue = new LinkedList<>();

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            String input;
            while((input = reader.readLine()) != null) {

                queue.add(input.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTCP(String msg) {
        writer.println(msg + "\n");
    }

    public String dequeueMessage() {
        if (!queue.isEmpty()) {
            return queue.remove();
        }
        return null;
    }

}
