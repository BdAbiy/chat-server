import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Server IP (localhost)
        int serverPort = 8080; // Server port

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Construct JSON request for login or sign up
            
            JSONObject request = new JSONObject();
            request.put("methode", "Sign up"); // Change to "login" or "Sign up" as needed
            request.put("username", "testUser");
            request.put("password", "testPassword");

            // Send request to the server
            out.println(request.toString());
            out.flush(); // Ensure all data is sent

            // Read and print the server response
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Server Response: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
