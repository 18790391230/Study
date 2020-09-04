package com.wym.sentinel;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 *
 */
public class SpelUtils {

    private static ExpressionParser parser = new SpelExpressionParser();

    private static LocalVariableTableParameterNameDiscoverer discoverer =
            new LocalVariableTableParameterNameDiscoverer();


    public static String getSpelKeyIfNeed(String key, Method method, Object[] args) {

//        String[] parameterNames = discoverer.getParameterNames(method);
//        EvaluationContext evaluationContext = new StandardEvaluationContext();
//        for (int i = 0; i < parameterNames.length; i++) {
//            evaluationContext.setVariable(parameterNames[i], args[i]);
//        }
//
//        return parser.parseExpression(key).getValue(evaluationContext, String.class);
        return parser.parseExpression(key).getValue(String.class);
    }

}
