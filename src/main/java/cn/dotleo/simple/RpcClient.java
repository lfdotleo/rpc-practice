package cn.dotleo.simple;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RpcClient {

    public <T> T refer(Class<T> serviceClass, String host, int port) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[] {serviceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
                Socket socket = null;
                ObjectOutputStream outputStream = null;
                ObjectInputStream inputStream = null;
                try {
                    socket = new Socket(host, port);
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(method.getName());
                    outputStream.writeObject(method.getParameterTypes());
                    outputStream.writeObject(arguments);
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    Object result = inputStream.readObject();
                    if (result instanceof Throwable) {
                        throw (Throwable) result;
                    }
                    return result;
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        });
    }
}
