package cn.dotleo.simple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class RpcServer {

    public void export(Object service, int port) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ObjectInputStream inputStream = null;
                            ObjectOutputStream outputStream = null;
                            try {
                                inputStream = new ObjectInputStream(socket.getInputStream());
                                outputStream = new ObjectOutputStream(socket.getOutputStream());
                                String methodName = inputStream.readUTF();
                                Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
                                Object[] args = (Object[]) inputStream.readObject();
                                Method method = service.getClass().getMethod(methodName, paramTypes);
                                Object result = method.invoke(service, args);
                                outputStream.writeObject(result);
                            } catch (Throwable throwable) {
                                outputStream.writeObject(throwable);
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
