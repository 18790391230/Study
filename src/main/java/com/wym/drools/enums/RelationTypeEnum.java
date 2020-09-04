package com.wym.drools.enums;

/**
 *
 */
public enum RelationTypeEnum {

    AND(0, "&&"),

    OR(1, "||");

    private final int code;

    private final String operChar;

    RelationTypeEnum(int code, String operChar) {
        this.code = code;
        this.operChar = operChar;
    }

    public int getCode() {
        return code;
    }

    public String getOperChar() {
        return operChar;
    }

    public static RelationTypeEnum getByCode(int code) {

        for (RelationTypeEnum value : RelationTypeEnum.values()) {
            if (code == value.getCode()) {
                return value;
            }
        }
        throw new RuntimeException("");
    }
}
