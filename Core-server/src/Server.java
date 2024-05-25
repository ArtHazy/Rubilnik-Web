import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static boolean isRunning = false;
    static ServerSocket socket;
    static Thread serverThread = new Thread(()->{
        while (isRunning){
            try {
                Socket client = socket.accept();
                serveSocket(client);
                client.close();
            } catch (IOException e) { App.log(e.getMessage());}
        }
        try {socket.close();} catch (IOException e) { App.log(e.getMessage());}
        App.log("server ended");
    });

    public static void start() throws IOException { 
        socket = new ServerSocket(3000); 
        serverThread.start();
        isRunning = true;
    }
    public static void stop() throws IOException  { isRunning = false; }

    public static void serveSocket(Socket socket) throws IOException {
        System.out.println("serving socket " + socket.getPort());
        var is = socket.getInputStream();
        var bisr = new BufferedReader(new InputStreamReader(is));
        var os = socket.getOutputStream();
        var req = bisr.readLine();

        if (req.startsWith("GET /hi "))
            os.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hello world</h1>".getBytes());
        else
            os.write("HTTP/1.0 200 OK\nContent-Type: text/html\n\n<h1>404</h1>".getBytes());
        os.flush();
        os.close();
    }
}
