package com.wym.drools.model;

import java.util.List;

/**
 *
 */
public class RuleInfo {

    private Integer id;

    private String ruleEnName;

    private String ruleCnName;

    private Integer priority;

    private Integer ruleSetId;

    private List<ConditionInfo> conditionList;

    private List<ActionInfo> actionInfoList;

    private String ruleCode;


    public List<ActionInfo> getActionInfoList() {
        return actionInfoList;
    }

    public void setActionInfoList(List<ActionInfo> actionInfoList) {
        this.actionInfoList = actionInfoList;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuleEnName() {
        return ruleEnName;
    }

    public void setRuleEnName(String ruleEnName) {
        this.ruleEnName = ruleEnName;
    }

    public String getRuleCnName() {
        return ruleCnName;
    }

    public void setRuleCnName(String ruleCnName) {
        this.ruleCnName = ruleCnName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(Integer ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public List<ConditionInfo> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<ConditionInfo> conditionList) {
        this.conditionList = conditionList;
    }
}
