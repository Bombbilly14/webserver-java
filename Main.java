import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket (8090)) {
            System.out.println("Server Started.\nListening for messages...");

            while (true) {
                // want infinite loop for socket connection - always online receiving messages

                try (Socket client = serverSocket.accept()) {
                    System.out.println("debug: got new message" + client.toString());
                    InputStreamReader isr = new InputStreamReader(client.getInputStream());

                    BufferedReader br = new BufferedReader(isr);

                    StringBuilder request = new StringBuilder();
                    
                    String line; //temp variable called line that holds one line at a time of our message
                    line = br.readLine();

                    while (!line.isBlank()) {
                        request.append(line + "\r\n");
                        line = br.readLine();
                    }

                    // System.out.println("Request --");
                    // System.out.println(request);
                    
                    String firstLine = request.toString().split("\n")[0];

                    String resource = firstLine.split(" ")[1];

                    System.out.println(resource);

                    if (resource.equals("/william")) {
                        FileInputStream image = new FileInputStream("me.jpg");
                        System.out.println(image.toString());

                        OutputStream clientOutput = client.getOutputStream();
                        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        clientOutput.write(("\r\n").getBytes());
                        clientOutput.write(image.readAllBytes());
                        clientOutput.flush();

                        image.close();
                        


                    } else if (resource.equals("/hello")) {
                        OutputStream clientOutput = client.getOutputStream();
                        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        clientOutput.write(("\r\n").getBytes());
                        clientOutput.write(("Hello world!").getBytes());
                        clientOutput.flush();

                        

                    } else {
                        OutputStream clientOutput = client.getOutputStream();
                        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
                        clientOutput.write(("\r\n").getBytes());
                        clientOutput.write(("What are you looking for?").getBytes());
                        clientOutput.flush();

                        
                    }

                    client.close();
                }
            }
        }
    }
}
