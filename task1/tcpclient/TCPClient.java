package tcpclient;
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class TCPClient {

    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port) throws IOException{
        ByteArrayOutputStream testBuffer = new ByteArrayOutputStream();
        int data = 0;

        Socket cSocket = new Socket(hostname, port);

        while(data != -1){
            data = cSocket.getInputStream().read();
            if(data != -1) {
                testBuffer.write(data);
            }
        }

        return testBuffer.toByteArray();
    }


    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        ByteArrayOutputStream testBuffer = new ByteArrayOutputStream();
        int data = 0;

        Socket cSocket = new Socket(hostname, port);

        cSocket.getOutputStream().write(toServerBytes);

        while(data != -1){
            data = cSocket.getInputStream().read();
            if(data != -1) {
                testBuffer.write(data);
            }
        }

        return testBuffer.toByteArray();
    }
}
