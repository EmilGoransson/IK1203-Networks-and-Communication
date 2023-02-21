public static void main(String[]args){
    int port = Integer.parseInt(args[0]);
    MyRunnable runnable = new MyRunnable(port);
    Thread thread = new Thread(runnable);
    thread.start(port);

}