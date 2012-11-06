package gw.testharness.environmentalcondition;

import gw.testharness.IEnvironmentalCondition;

public class JRockitVMCondition implements IEnvironmentalCondition {

  @Override
  public boolean isConditionMet() {
    String vmName = System.getProperty("java.vm.name");
    return vmName != null && vmName.toLowerCase().contains("jrockit");
  }
}
