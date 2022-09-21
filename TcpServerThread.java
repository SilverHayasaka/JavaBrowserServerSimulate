package ~;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerThread {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);

        while (true) {
            Socket socket = server.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = socket.getInputStream();

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = bufferedReader.readLine();
                        System.out.println(line);

                        String[] arr = line.split(" ");
                        String htmlpath = arr[1].substring(1);

                        FileInputStream fis = new FileInputStream(htmlpath);

                        OutputStream os = socket.getOutputStream();
                        if (htmlpath.endsWith(".css")) {
                            os.write("HTTP/1.1 200 OK\r\n".getBytes());
                            os.write("Content-Type:text/css\r\n".getBytes());
                            os.write("\r\n".getBytes());
                        } else if (htmlpath.endsWith(".js")) {
                            os.write("HTTP/1.1 200 OK\r\n".getBytes());
                            os.write("Content-Type:text/javascript\r\n".getBytes());
                            os.write("\r\n".getBytes());
                        } else {
                            os.write("HTTP/1.1 200 OK\r\n".getBytes());
                            os.write("Content-Type:text/html\r\n".getBytes());
                            os.write("\r\n".getBytes());
                        }

                        int len = 0;
                        byte[] bytes = new byte[1024];
                        while ((len = fis.read(bytes)) != -1) {
                            os.write(bytes, 0, len);
                        }
                        fis.close();
                        socket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
