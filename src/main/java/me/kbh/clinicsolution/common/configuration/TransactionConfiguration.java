package me.kbh.clinicsolution.common.configuration;

import static java.util.Collections.singletonList;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Aspect
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionConfiguration {

  static String TX_METHOD_NAME = "*";
  static String AOP_POINTCUT_EXPRESSION =
      "execution(* me.kbh.clinicsolution.domain.*.service..*.*(..))";

  TransactionManager transactionManager;

  @Bean
  public TransactionInterceptor txAdvice() {
    MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
    RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
    transactionAttribute.setName(TX_METHOD_NAME);
    transactionAttribute.setRollbackRules(
        singletonList(new RollbackRuleAttribute(Exception.class)));
    source.setTransactionAttribute(transactionAttribute);
    return new TransactionInterceptor(transactionManager, source);
  }

  @Bean
  public Advisor txAdviceAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
    return new DefaultPointcutAdvisor(pointcut, txAdvice());
  }
}
