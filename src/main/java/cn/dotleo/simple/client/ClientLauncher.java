package cn.dotleo.simple.client;

import cn.dotleo.simple.RpcClient;
import cn.dotleo.simple.depend.CalculatorService;

public class ClientLauncher {

    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        CalculatorService calculatorService = client.refer(CalculatorService.class, "127.0.0.1", 707);
        int result = calculatorService.add(1, 3);
        System.out.println("result: " + result);
    }
}
