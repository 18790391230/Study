package com.wym.design.simplefacotory;

import java.io.IOException;
import java.util.Scanner;

public class SimpleFactoryTest {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("请输入第1个数：");
        double numberA = Double.parseDouble(scanner.nextLine());

        System.out.println("请输入第2个数：");
        double numberB = Double.parseDouble(scanner.nextLine());

        System.out.println("请输入操作符：");
        String oper = scanner.nextLine();
        Operation operation = OperationFactory.createOperation(oper);
        System.out.println("结果是：" + operation.getResult(numberA, numberB));
    }
}
