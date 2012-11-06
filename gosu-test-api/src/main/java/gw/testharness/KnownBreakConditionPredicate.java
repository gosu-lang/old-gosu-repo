package gw.testharness;

import gw.util.Predicate;

import java.lang.annotation.Annotation;

public class KnownBreakConditionPredicate implements Predicate<Annotation> {
  @Override
  public boolean evaluate(Annotation o) {
    return isKnownBreakCondition((KnownBreakCondition) o);
  }

  public static boolean isKnownBreakCondition(KnownBreakCondition kbCond) {
    for (Class<? extends IEnvironmentalCondition> conditionClass : kbCond.value()) {
      IEnvironmentalCondition condition;
      try {
        condition = conditionClass.newInstance();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      if (condition.isConditionMet()) {
        return true;
      }
    }
    return false;
  }
}
