package com.wym.design.strategy;

public class CashContext implements CashSuper{


    private CashSuper cashSuper;

    public CashContext(String type) {
        switch (type) {
            case "正常收费":
                cashSuper = new CashNormal();
                break;
            case "满一百返二十":
                cashSuper = new CashReturn(100, 20);
                break;
            case "8折":
                cashSuper = new CashRebate(0.8);
                break;
        }

    }

    @Override
    public double acceptCash(double money) {
        return cashSuper.acceptCash(money);
    }
}
