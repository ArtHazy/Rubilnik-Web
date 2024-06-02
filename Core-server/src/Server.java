import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        Thread serveSocketThread = new Thread(()->{
            try {
                System.out.println("serving socket " + socket.getPort());
                var is = socket.getInputStream();
                var bisr = new BufferedReader(new InputStreamReader(is));
                var os = socket.getOutputStream();
                var bos = new BufferedWriter(new OutputStreamWriter(os)); // for flush to work
    
                boolean isServing = true;
                while (isServing) {
                    var req = bisr.readLine();
                    if (req.startsWith("GET /"+"hi")) {
                        // os.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hello world</h1>".getBytes()); isServing = false;
                        bos.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hello world</h1>"); isServing = false;
                        bos.flush();
                    }
                    if (req.startsWith("GET /"+"test/"+"html")) {
                        // os.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hi</h1>".getBytes()); os.flush();
                        bos.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hi</h1>"); bos.flush();
                        Thread.sleep(7000);
                        // os.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hello</h1>".getBytes()); os.flush();
                        bos.write("<h1>hello</h1>"); bos.flush();
                        isServing = false;
                    }
                    if (req.startsWith("GET /"+"test/"+"json")) {
                        // os.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hi</h1>".getBytes()); os.flush();
                        bos.write("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\n{'msg':'hello'}"); bos.flush();
                        Thread.sleep(7000);
                        // os.write("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\n<h1>hello</h1>".getBytes()); os.flush();
                        bos.write("{'msg':'hello2'}"); bos.flush();
                        isServing = false;
                    }
                }
                os.close();
            } catch (Exception e) {App.log(e.getMessage());}
        });
        serveSocketThread.start();
    }
}