package com.wym.drools.enums;

/**
 *
 */
public enum DataTypeEnum {

    Int(1, "Integer"),
    Double(2, "Double"),
    String(3, "String"),
    ;


    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    DataTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DataTypeEnum getByCode(int code) {

        for (DataTypeEnum value : DataTypeEnum.values()) {
            if (code == value.getCode()) {
                return value;
            }
        }
        throw new RuntimeException("");
    }
}
