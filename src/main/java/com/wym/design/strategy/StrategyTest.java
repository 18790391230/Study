package com.wym.design.strategy;

import java.util.Scanner;

public class StrategyTest {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("请输入价格：");
        double money = Double.parseDouble(scanner.nextLine());
        System.out.println("请输入优惠方式：");
        CashContext cashContext = new CashContext(scanner.nextLine());
        System.out.println("结果是：" + cashContext.acceptCash(money));

    }
}
