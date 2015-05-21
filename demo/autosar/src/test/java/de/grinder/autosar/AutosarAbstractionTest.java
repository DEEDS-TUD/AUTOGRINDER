package de.grinder.autosar;

import de.grinder.util.cue.CUEAbstractionTester;

public class AutosarAbstractionTest {

  public static void main(String args[]) {
    CUEAbstractionTester tester = new CUEAbstractionTester(new AutosarAbstraction());
    tester.show();
  }
}
