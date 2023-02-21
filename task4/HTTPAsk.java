import java.net.*;
import java.io.*;
import java.util.Arrays;

public class MyRunnable implements Runnable {
    private x Parameter;
    public MyRunnable(Socket socket) {
        this.Parameter = Parameter;
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            while (true) {
                //declare variables
                boolean shutdown = false;
                Integer timeout = null;
                Integer limit = null;
                String hostname = null;
                String string = "";
                int portServer = 0;
                int data = 0;
                byte[] response;

                ByteArrayOutputStream testBuffer = new ByteArrayOutputStream();
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();


                //Read URL, 13 is empty line.
                while (data != 13) {
                    data = clientSocket.getInputStream().read();

                    if (data != 13) {
                        testBuffer.write(data);
                    }
                }
                //manage URL
                String serverOutput = testBuffer.toString();
                String[] arrayServerOutput = serverOutput.split(" ");
                String statusResponse = "HTTP/1.1 400 Bad Request\r\n\r\n";


                //handle different conditions and check flags
                try {
                    String[] argArr = arrayServerOutput[1].split("&");
                    System.out.println(Arrays.toString(arrayServerOutput));
                    if (!arrayServerOutput[0].contains("GET") || !arrayServerOutput[2].contains("HTTP/1.1")) {
                        statusResponse = "HTTP/1.1 400 Bad Request\r\n\r\n";
                    } else if (argArr[0].contains("/ask?hostname=")) {
                        hostname = argArr[0].substring(14);
                        statusResponse = "HTTP/1.1 200 OK\r\n\r\n";
                    }
                    for (String s : argArr) {
                        if (s.contains("port=")) {
                            portServer = Integer.parseInt(s.substring(5));
                        }
                        if (s.contains("limit=")) {
                            limit = Integer.parseInt(s.substring(6));
                        }
                        if (s.contains("shutdown=")) {
                            shutdown = Boolean.parseBoolean(s.substring(9));
                        }
                        if (s.contains("string=")) {
                            string = s.substring(7);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    statusResponse = "HTTP/1.1 400 Bad Request\r\n\r\n";
                }


                //Connects and reads data from client
                OutputStream output = clientSocket.getOutputStream();

                TCPClient client = new TCPClient(shutdown, timeout, limit);

                if (hostname == null || portServer == 0) {
                    output.write(statusResponse.getBytes());
                    output.flush();
                    output.close();
                    serverSocket.close();
                    clientSocket.close();
                    continue;
                }
                try {
                    if (!string.equals("")) {
                        response = client.askServer(hostname, portServer, string.getBytes());
                    } else {
                        response = client.askServer(hostname, portServer);
                    }
                } catch (IOException e) {
                    statusResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
                    response = statusResponse.getBytes();
                }


                //writes data HTML
                output.write(statusResponse.getBytes());
                output.write(response);
                output.flush();
                output.close();
                serverSocket.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error using port: " + port);
            System.exit(-1);
        }
    }
}

