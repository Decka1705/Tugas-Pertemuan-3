import java.io.*;
import java.net.*;

public class BlockingKlien {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected to server");

            out.println("Halo Guyssss!!!!!");
            String response = in.readLine();
            System.out.println("Received message from server: " + response);

        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error in socket communication: " + e.getMessage());
        }
    }
}
