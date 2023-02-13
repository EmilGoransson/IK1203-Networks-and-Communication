package tcpclient;
import java.net.*;
import java.io.*;
//BIT LIMIT MIGHT BE WRONG! NOT SURE! EVERYTHING ELSE SHOULD WORK THOUGH.
public class TCPClient {
    boolean shutdown = false;
    Integer timeout = null;
    Integer limit = null;
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port) throws IOException{
        if(timeout == null){
            timeout = 0;
        }
        if(limit == null){
            limit = Integer.MAX_VALUE;
        }
        int countByte = 0;


        ByteArrayOutputStream testBuffer = new ByteArrayOutputStream();
        int data = 0;

        Socket cSocket = new Socket(hostname, port);
        //shutdown
        if(shutdown){
            cSocket.shutdownOutput();
        }
        cSocket.setSoTimeout(timeout);
        try{
            while(data != -1 && countByte <= limit){
                data = cSocket.getInputStream().read();

                if(data != -1 && countByte <= limit) {
                    countByte++;
                    testBuffer.write(data);
                }
            }
            cSocket.close();
            return testBuffer.toByteArray();
        }
        catch(SocketException e){
            cSocket.close();
            return testBuffer.toByteArray();
        }


    }


    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        ByteArrayOutputStream testBuffer = new ByteArrayOutputStream();
        int data = 0;
        if(timeout == null){
            timeout = 0;
        }
        if(limit == null){
            limit = Integer.MAX_VALUE;
        }
        int countByte = 0;

        Socket cSocket = new Socket(hostname, port);
        cSocket.getOutputStream().write(toServerBytes);
        //shutdown
        if(shutdown){
            cSocket.shutdownOutput();
        }
        cSocket.setSoTimeout(timeout);
        try{
            while(data != -1 && countByte <= limit){
                countByte+=data;
                System.out.println(data);
                data = cSocket.getInputStream().read();

                if(data != -1 && countByte <= limit) {
                    testBuffer.write(data);
                }
            }
            cSocket.close();
            return testBuffer.toByteArray();
        }
        catch(SocketTimeoutException e){
            cSocket.close();
            return testBuffer.toByteArray();
        }
    }
}
