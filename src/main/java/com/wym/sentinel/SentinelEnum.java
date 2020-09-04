package com.wym.sentinel;

/**
 *
 */
public enum SentinelEnum {

    InterfaceA("AAA", "hfladsjfl"),
    InterfaceB("BBB", "hfladsjfl"),
    ;

    SentinelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
