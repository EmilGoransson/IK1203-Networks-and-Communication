
import java.net.*;
import java.io.*;
public class ConcHTTPAsk {
    public static void main(String[] args) throws IOException{


            if(args.length != 1){
                System.out.println("bad input");
            }
            try{
                int port = Integer.parseInt(args[0]);
                ServerSocket sSocket = new ServerSocket(port);
                while(true){
                    Socket clientSocket = sSocket.accept();
                    MyRunnable runnable = new MyRunnable(clientSocket);
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            }
            catch(IOException e){
                System.exit(-1);
            }
    }
}
