package com.wym.drools.model;

import com.wym.drools.enums.DataTypeEnum;
import com.wym.drools.enums.FeatureTypeEnum;
import com.wym.drools.enums.OperTypeEnum;
import com.wym.drools.enums.RelationTypeEnum;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ConditionUtil {

    public static void main(String[] args) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setPriority(100);
        ruleInfo.setRuleEnName("rule1");
        ruleInfo.setRuleCnName("规则1");
        List<ConditionInfo> conditionList = new ArrayList<>();
        ruleInfo.setConditionList(conditionList);

        ConditionInfo conditionInfo1 = new ConditionInfo();
        conditionInfo1.setLeftEnName("ymd");
        conditionInfo1.setRelationType(RelationTypeEnum.AND.getCode());
        conditionInfo1.setLeftFeatureType(FeatureTypeEnum.Variable.getVarType());
        conditionInfo1.setOperChar(OperTypeEnum.eq.getCode());
        conditionInfo1.setRightValue("123");
        conditionInfo1.setRightDataType(DataTypeEnum.String.getCode());

        ConditionInfo conditionInfo2 = new ConditionInfo();
        conditionInfo2.setLeftEnName("risk_score");
        conditionInfo2.setRelationType(RelationTypeEnum.AND.getCode());
        conditionInfo2.setLeftFeatureType(FeatureTypeEnum.InterfaceVariable.getVarType());
        conditionInfo2.setOperChar(OperTypeEnum.gte.getCode());
        conditionInfo2.setRightValue("10");
        conditionInfo2.setRightDataType(DataTypeEnum.Int.getCode());
        conditionList.add(conditionInfo1);
        conditionList.add(conditionInfo2);

        ConditionInfo conditionInfo3 = new ConditionInfo();
        conditionInfo3.setLeftEnName("ymd3");
        conditionInfo3.setRelationType(RelationTypeEnum.AND.getCode());
        conditionInfo3.setLeftFeatureType(FeatureTypeEnum.Variable.getVarType());
        conditionInfo3.setOperChar(OperTypeEnum.eq.getCode());
        conditionInfo3.setRightValue("456");
        conditionInfo3.setRightDataType(DataTypeEnum.String.getCode());
        conditionList.add(conditionInfo3);

        ConditionInfo conditionInfo4 = new ConditionInfo();
        conditionInfo4.setLeftEnName("ymd3");
        conditionInfo4.setRelationType(RelationTypeEnum.OR.getCode());
        conditionInfo4.setLeftFeatureType(FeatureTypeEnum.Variable.getVarType());
        conditionInfo4.setOperChar(OperTypeEnum.eq.getCode());
        conditionInfo4.setRightValue("456");
        conditionInfo4.setRightDataType(DataTypeEnum.String.getCode());
        List<ConditionInfo> conditionList2 = new ArrayList<>();
        conditionList2.add(conditionInfo4);
        conditionInfo3.setConditionList(conditionList2);

        ConditionInfo conditionInfo5 = new ConditionInfo();
        conditionInfo5.setLeftEnName("ymd4");
        conditionInfo5.setRelationType(RelationTypeEnum.OR.getCode());
        conditionInfo5.setLeftFeatureType(FeatureTypeEnum.Variable.getVarType());
        conditionInfo5.setOperChar(OperTypeEnum.eq.getCode());
        conditionInfo5.setRightValue("678");
        conditionInfo5.setRightDataType(DataTypeEnum.String.getCode());
        conditionList2.add(conditionInfo5);
//        ----------------------------------------
        ActionInfo actionInfo1 = new ActionInfo();
        actionInfo1.setLeftDataType(DataTypeEnum.Int.getCode());
        actionInfo1.setLeftFeatureId(1);
        actionInfo1.setLeftFeatureType(FeatureTypeEnum.Input.getVarType());
        actionInfo1.setRightDataType(DataTypeEnum.Int.getCode());
        actionInfo1.setRightValue("0");

        ActionInfo actionInfo2 = new ActionInfo();
        actionInfo2.setLeftDataType(DataTypeEnum.String.getCode());
        actionInfo2.setLeftFeatureId(10);
        actionInfo2.setLeftFeatureType(FeatureTypeEnum.Input.getVarType());
        actionInfo2.setRightDataType(DataTypeEnum.String.getCode());
        actionInfo2.setRightValue("A-021");
        List<ActionInfo> actionInfoList = new ArrayList<>();
        actionInfoList.add(actionInfo1);
        actionInfoList.add(actionInfo2);
        ruleInfo.setActionInfoList(actionInfoList);



        ConditionUtil conditionUtil = new ConditionUtil();
        String header = conditionUtil.buildRuleHeader("abc");
        System.out.println(header);


        String condition = conditionUtil.buildCoreCode(ruleInfo);
        System.out.println(condition);

        String action = conditionUtil.buildActionCode(ruleInfo.getActionInfoList());
        System.out.println(action);

        List<CommonVariableInfo> variableInfoList = conditionUtil.buildVariableInfoList();

        KieHelper helper = new KieHelper();
        helper.addContent(header + condition + action, ResourceType.DRL);
        KieBase kieBase = helper.build();
        fire(variableInfoList, kieBase);
        fire(variableInfoList, kieBase);

    }

    private static void fire(List<CommonVariableInfo> variableInfoList, KieBase kieBase) {
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        KieSession kieSession = kieBase.newKieSession();
        kieSession.setGlobal("map", map);
        kieSession.insert(variableInfoList);
        int fireCount = kieSession.fireAllRules();
        map = (Map<String, String>) kieSession.getGlobal("map");
        kieSession.dispose();
        System.out.println("====" + (System.currentTimeMillis() - start));
        System.out.println(fireCount);
        System.out.println(map);
    }

    public List<CommonVariableInfo> buildVariableInfoList() {
        List<CommonVariableInfo> list = new ArrayList<>();
        CommonVariableInfo variableInfo1 = new CommonVariableInfo();
        variableInfo1.setDataType(DataTypeEnum.String.getCode());
        variableInfo1.setEnName("ymd");
        variableInfo1.setFeatureType(1);
        variableInfo1.setValue("123");

        CommonVariableInfo variableInfo2 = new CommonVariableInfo();
        variableInfo2.setDataType(DataTypeEnum.Int.getCode());
        variableInfo2.setEnName("risk_score");
        variableInfo2.setFeatureType(4);
        variableInfo2.setValue("123");

        CommonVariableInfo variableInfo3 = new CommonVariableInfo();
        variableInfo3.setDataType(DataTypeEnum.String.getCode());
        variableInfo3.setEnName("ymd4");
        variableInfo3.setFeatureType(1);
        variableInfo3.setValue("678");
        list.add(variableInfo1);
        list.add(variableInfo2);
        list.add(variableInfo3);

        return list;
    }

    public String buildCoreCode(RuleInfo ruleInfo) {

        StringBuilder coreSb = new StringBuilder();

        coreSb.append(MessageFormat.format("rule \"{0}\"", ruleInfo.getRuleEnName()));

        coreSb.append("\n").append("salience ").append(ruleInfo.getPriority()).append("\n");
        coreSb.append("when").append("\n").append("$list : List();");

        coreSb.append(MessageFormat.format("\nexists({0})", buildConditionExpress(ruleInfo)));

        return coreSb.toString();
    }

    public String buildActionCode(List<ActionInfo> actionInfoList) {

        StringBuilder sb = new StringBuilder();
        sb.append("\nthen\n");

        int i = 0;
        for (ActionInfo actionInfo : actionInfoList) {
            FeatureTypeEnum featureType = FeatureTypeEnum.getByCode(actionInfo.getLeftFeatureType());
            switch (featureType) {
                case Input:
                    String result = MessageFormat.format("Object {0} = {1};", "result" + i,
                            getActionValueOf(actionInfo));
                    sb.append(result).append("\n");
                    sb.append(MessageFormat.format("if(map.get(\"{0}\") != null)",
                            actionInfo.getLeftFeatureId().toString()));
                    sb.append("{");
                    sb.append(MessageFormat.format("map.put(\"{0}\", map.get(\"{1}\") + \",\" + String.valueOf({2}));",
                            actionInfo.getLeftFeatureId().toString(), actionInfo.getLeftFeatureId().toString(),
                            "result" + i));
                    sb.append("}else{");
                    sb.append(MessageFormat.format("map.put(\"{0}\", String.valueOf({1}));",
                            actionInfo.getLeftFeatureId().toString(),
                            "result" + i));

                    sb.append("}");

                    break;
                case ActionVariable:

                    break;
            }
            i++;
        }

        return sb.toString() + "\nend";
    }

    private String buildConditionExpress(RuleInfo ruleInfo) {

        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (ConditionInfo conditionInfo : ruleInfo.getConditionList()) {
            String str = null;
            if (conditionInfo.getConditionList() != null) {
                str = buildChildExpress(conditionInfo.getConditionList());
                sb.append(str);
            } else {
                str = MessageFormat.format("CommonVariableInfo(enName == \"{0}\" && featureType == {1} && {2}" +
                                " {3} {4})",
                        conditionInfo.getLeftEnName(), conditionInfo.getLeftFeatureType(),
                        getValueOf(conditionInfo), OperTypeEnum.getByCode(conditionInfo.getOperChar()).getOperChar(),
                        buildValueExpress(conditionInfo));
                sb.append("(");
                sb.append(str);
                sb.append(" from $list;");
                sb.append(")");
            }
            if (++i != ruleInfo.getConditionList().size()) {
                sb.append(" ");
                sb.append(RelationTypeEnum.getByCode(conditionInfo.getRelationType()).getOperChar());
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private String buildRuleHeader(String sceneId) {

        StringBuilder sb = new StringBuilder();
        sb.append("package com.wym.drl.").append(sceneId).append("\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("import com.wym.drools.model.CommonVariableInfo;\n");

        sb.append("global java.util.Map map\r\n");
        return sb.toString();
    }
    private String buildChildExpress(List<ConditionInfo> conditionList) {


        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (ConditionInfo conditionInfo : conditionList) {

            if (conditionInfo.getConditionList() != null) {
                String str = buildChildExpress(conditionInfo.getConditionList());
                sb.append(str);
            } else {
                String str = MessageFormat.format("CommonVariableInfo(enName == \"{0}\" && featureType == {1} && {2}" +
                                " {3} {4})",
                        conditionInfo.getLeftEnName(), conditionInfo.getLeftFeatureType(),
                        getValueOf(conditionInfo), OperTypeEnum.getByCode(conditionInfo.getOperChar()).getOperChar(),
                        buildValueExpress(conditionInfo));
                sb.append("(");
                sb.append(str);
                sb.append(" from $list;");
                sb.append(")");
            }
            if (++i != conditionList.size()) {
                sb.append(" ");
                sb.append(RelationTypeEnum.getByCode(conditionInfo.getRelationType()).getOperChar());
                sb.append(" ");
            }
        }

        return "(" + sb.toString() + ")";
    }

    private String getValueOf(ConditionInfo conditionInfo) {

        DataTypeEnum dataType = DataTypeEnum.getByCode(conditionInfo.getRightDataType());
        String str = null;
        switch (dataType) {
            case Int:
                str = "Integer.valueOf(value)";
                break;
            case Double:
                str = "Double.valueOf(value)";
                break;
            case String:
                str = "String.valueOf(value)";
                break;
        }
        return str;
    }

    private String getActionValueOf(ActionInfo actionInfo) {

        DataTypeEnum dataType = DataTypeEnum.getByCode(actionInfo.getLeftDataType());
        String str = null;
        switch (dataType) {
            case Int:
                str = MessageFormat.format("Integer.valueOf(\"{0}\")", actionInfo.getRightValue());
                break;
            case Double:
                str = MessageFormat.format("Double.valueOf(\"{0}\")", actionInfo.getRightValue());
                break;
            case String:
                str = MessageFormat.format("String.valueOf(\"{0}\")", actionInfo.getRightValue());
                break;
        }
        return str;
    }

    private String buildValueExpress(ConditionInfo conditionInfo) {

//        OperTypeEnum operType = OperTypeEnum.getByCode(conditionInfo.getOperChar());
//        switch (operType) {
//            case eq:
//
//                break;
//            case gt:
//
//                break;
//            case lt:
//
//                break;
//            case ne:
//
//                break;
//            case gte:
//
//                break;
//            case lte:
//                break;
//            case contain:
//
//                break;
//
//            case not_contain:
//                break;
//
//        }

        DataTypeEnum dataType = DataTypeEnum.getByCode(conditionInfo.getRightDataType());
        switch (dataType) {
            case String:
                return String.format("\"%s\"", conditionInfo.getRightValue());
            case Double:

            case Int:
                return conditionInfo.getRightValue();
        }
        return null;
    }
}
