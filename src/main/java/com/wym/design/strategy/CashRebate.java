package com.wym.design.strategy;

public class CashRebate implements CashSuper {


    private double moneyRebate = 0.0;

    public CashRebate(double moneyRebate) {
        this.moneyRebate = moneyRebate;
    }

    @Override
    public double acceptCash(double money) {
        return money * moneyRebate;
    }
}
