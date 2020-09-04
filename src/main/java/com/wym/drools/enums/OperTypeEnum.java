package com.wym.drools.enums;

/**
 *
 */
public enum OperTypeEnum {

    gt(1, ">"),
    eq(2, "=="),
    lt(3, "<"),
    gte(4, ">="),
    lte(5, "<="),
    ne(6, "!="),
    contain(7, "memberOf"),
    not_contain(8, "not memberOf");


    public static OperTypeEnum getByCode(Integer code) {
        for (OperTypeEnum value : OperTypeEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new RuntimeException("code未找到");
    }
    private final int code;

    private final String operChar;

    OperTypeEnum(int code, String operChar) {
        this.code = code;
        this.operChar = operChar;
    }

    public int getCode() {
        return code;
    }

    public String getOperChar() {
        return operChar;
    }
}
