package com.wym.drools.enums;

/**
 *
 */
public enum  FeatureTypeEnum {


    Variable(1),

    Constant( 2),

    Parameter(3),

    InterfaceVariable( 4),

    Input(5),


    ActionVariable(7)

    ;

    public static FeatureTypeEnum getByCode(int code) {
        for (FeatureTypeEnum value : FeatureTypeEnum.values()) {
            if (code == value.getVarType()) {
                return value;
            }
        }
        throw new RuntimeException("");
    }

    private final int varType;

    FeatureTypeEnum(int varType) {
        this.varType = varType;
    }

    public int getVarType() {
        return varType;
    }
}
