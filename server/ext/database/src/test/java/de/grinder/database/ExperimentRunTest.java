package de.grinder.database;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import de.grinder.database.TestCase;

public class ExperimentRunTest {

  @Test
  public void testGetTestCase() {
    TestCase testCase = mock(TestCase.class);
    ExperimentRun experimentRun = new ExperimentRun(testCase);
    
    assertEquals(testCase, experimentRun.getTestCase());
  }
}
