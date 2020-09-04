package com.wym.drools.model;

import java.util.List;

/**
 *
 */
public class ConditionInfo {

    private Integer ruleId;

    private Integer leftFeatureType;

    private Integer leftFeatureId;

    private Integer leftFeatureParentId;

    private Integer leftDataType;

    private Integer relationType;


    private Integer operChar;

    private Integer rightFeatureType;

    private Integer rightFeatureId;

    private Integer rightDataType;

    private Integer rightParentId;

    private String rightValue;

    private String leftEnName;

    private String leftCnName;

    private String rightCnName;

    private List<ConditionInfo> conditionList;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public List<ConditionInfo> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<ConditionInfo> conditionList) {
        this.conditionList = conditionList;
    }

    public Integer getLeftFeatureType() {
        return leftFeatureType;
    }

    public void setLeftFeatureType(Integer leftFeatureType) {
        this.leftFeatureType = leftFeatureType;
    }

    public Integer getLeftFeatureId() {
        return leftFeatureId;
    }

    public void setLeftFeatureId(Integer leftFeatureId) {
        this.leftFeatureId = leftFeatureId;
    }

    public Integer getLeftFeatureParentId() {
        return leftFeatureParentId;
    }

    public void setLeftFeatureParentId(Integer leftFeatureParentId) {
        this.leftFeatureParentId = leftFeatureParentId;
    }

    public Integer getLeftDataType() {
        return leftDataType;
    }

    public void setLeftDataType(Integer leftDataType) {
        this.leftDataType = leftDataType;
    }

    public Integer getOperChar() {
        return operChar;
    }

    public void setOperChar(Integer operChar) {
        this.operChar = operChar;
    }

    public Integer getRightFeatureType() {
        return rightFeatureType;
    }

    public void setRightFeatureType(Integer rightFeatureType) {
        this.rightFeatureType = rightFeatureType;
    }

    public Integer getRightFeatureId() {
        return rightFeatureId;
    }

    public void setRightFeatureId(Integer rightFeatureId) {
        this.rightFeatureId = rightFeatureId;
    }

    public Integer getRightDataType() {
        return rightDataType;
    }

    public void setRightDataType(Integer rightDataType) {
        this.rightDataType = rightDataType;
    }

    public Integer getRightParentId() {
        return rightParentId;
    }

    public void setRightParentId(Integer rightParentId) {
        this.rightParentId = rightParentId;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }

    public String getLeftEnName() {
        return leftEnName;
    }

    public void setLeftEnName(String leftEnName) {
        this.leftEnName = leftEnName;
    }

    public String getLeftCnName() {
        return leftCnName;
    }

    public void setLeftCnName(String leftCnName) {
        this.leftCnName = leftCnName;
    }

    public String getRightCnName() {
        return rightCnName;
    }

    public void setRightCnName(String rightCnName) {
        this.rightCnName = rightCnName;
    }
}
