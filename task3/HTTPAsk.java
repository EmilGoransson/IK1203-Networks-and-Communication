import java.net.*;
import java.io.*;

public class HTTPAsk {


    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String response200 = "HTTP/1.1 200 OK\r\n\r\n";
        String response400 = "HTTP/1.1 400 Bad Request\r\n\r\n";
        String response404 = "HTTP/1.1 404 Not Found\r\n\r\n";
        try {
            while (true) {
                boolean shutdown = false;
                Integer timeout = null;
                Integer limit = null;
                String hostname = null;
                String string = "";
                int portServer = 0;
                int data = 0;
                byte[] response;
                String statusRes;
                ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                OutputStream output = clientSocket.getOutputStream();

                //Read URL, 13 is empty line.
                while (data != 13) {
                    data = clientSocket.getInputStream().read();
                    if (data != 13) {
                        dataBuffer.write(data);
                    }
                }
                String serverOutput = dataBuffer.toString();
                String[] arrServerOutput = serverOutput.split(" ");
                String[] argArr = arrServerOutput[1].split("&");

                if(!arrServerOutput[1].contains("/ask")){
                    output.write(response404.getBytes());
                    output.flush();
                    output.close();
                    serverSocket.close();
                    clientSocket.close();
                    continue;
                }

                if (arrServerOutput[0].contains("GET") && arrServerOutput[2].contains("HTTP/1.1")
                        && arrServerOutput[1].contains("/ask?hostname=")) {
                    for (String s : argArr) {
                        if (s.contains("hostname=")) {
                            hostname = s.substring(14);
                        }
                        if (s.contains("port=")) {
                            portServer = Integer.parseInt(s.substring(5));
                        }
                        if (s.contains("limit=")) {
                            limit = Integer.parseInt(s.substring(6));
                        }
                        if (s.contains("string=")) {
                            string = s.substring(7);
                        }
                        if (s.contains("timeout=")) {
                            timeout = Integer.parseInt(s.substring(8));
                        }
                    }
                    if ((hostname != null) && (portServer != 0)) {
                        TCPClient client = new TCPClient(shutdown, timeout, limit);

                        try {
                            if (!string.equals("")) {
                                response = client.askServer(hostname, portServer, string.getBytes());
                            } else {
                                response = client.askServer(hostname, portServer);
                            }
                            output.write(response200.getBytes());
                            output.write(response);
                            output.flush();
                            output.close();
                            serverSocket.close();
                            clientSocket.close();

                        } catch (IOException e) {
                            output.write(response404.getBytes());
                            output.flush();
                            output.close();
                            serverSocket.close();
                            clientSocket.close();

                        }

                    } else {
                        output.write(response400.getBytes());
                        output.flush();
                        output.close();
                        serverSocket.close();
                        clientSocket.close();

                    }

                } else {
                    output.write(response400.getBytes());
                    output.flush();
                    output.close();
                    serverSocket.close();
                    clientSocket.close();

                }


            }
        } catch (IOException e) {
            System.exit(-1);
        }

    }

}
