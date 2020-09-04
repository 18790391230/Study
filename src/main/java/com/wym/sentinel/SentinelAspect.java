package com.wym.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@Aspect
public class SentinelAspect {

    @Pointcut("execution(* com.wym.mybatis.controller..*(..))&&@annotation(com.wym.sentinel.Throttle)")
    public void around() {

    }
//    @Throttle(resource = "{T(com.wym.sentinel.SentinelEnum).InterfaceA.code}", qps = 300)

    @Around("around()")
    public Object doAround(ProceedingJoinPoint joinPoint) {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Throttle annotation = AnnotationUtils.getAnnotation(method, Throttle.class);
        String resource = SpelUtils.getSpelKeyIfNeed(annotation.resource(), method, joinPoint.getArgs());
        initFlowRule(resource, annotation.qps());

        Entry entry = null;
        try {
            entry = SphU.entry(resource);
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return null;
        }finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    private static List<FlowRule> flowRules = new ArrayList<>();

    private void initFlowRule(String resource, int qps) {
        FlowRule flowRule = new FlowRule();
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setResource(resource);
        flowRule.setCount(qps);
        if (flowRules.contains(flowRule)) {
            return;
        }
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);

    }
}
