import java.io.*;
import java.net.*;

public class BlockingServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Server is listening on port " + PORT);

            String message = in.readLine();
            System.out.println("Menerima Pesan dari Klien: " + message);

            out.println("Halo Guys");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
