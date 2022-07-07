package cn.dotleo.simple.server;

import cn.dotleo.simple.RpcServer;
import cn.dotleo.simple.depend.CalculatorService;

import java.io.IOException;

public class ServerLauncher {

    public static void main(String[] args) throws IOException {
        CalculatorService calculatorService = new CalculatorServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.export(calculatorService, 707);
    }
}
