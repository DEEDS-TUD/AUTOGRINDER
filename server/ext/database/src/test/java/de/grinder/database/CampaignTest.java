package de.grinder.database;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class CampaignTest {
  
  @Test
  public void testGet() {
    Campaign campaign = new Campaign();
    
    TestCase t1 = createTestCase(1);
    TestCase t2 = createTestCase(2);
    
    campaign.add(t1);
    campaign.add(t2);
    
    assertEquals(t2, campaign.get(2));
  }
  
  private TestCase createTestCase(int id) {
    TestCase tc = mock(TestCase.class);
    when(tc.getId()).thenReturn(id);
    return tc;
  }
}
