package de.grinder.database;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.junit.Ignore;
import org.junit.Test;

public class DatabaseTest {

  @Test
  public void testGetEntityManager() {
    try {
      EntityManager em = Database.getEntityManager();
      assertNotNull("EntityManager should not be null.", em);
    } catch (Exception e) {
      fail("Exception should not occur: " + e);
    }
  }
  
  @Ignore("Test cases should reduce side effects on database")
  @Test
  public void testAddTarget() {
    Target expected = createTarget();
    Database.instance().addTarget(expected);
    
    Collection<Target> targets = Database.instance().getTargets();
    assertEquals(1, targets.size());
    Target actual = targets.iterator().next();
    assertEquals(expected.getConfiguration(), actual.getConfiguration());
  }
  
  private Target createTarget() {
    Target target = new Target();
    target.setName("TestTarget");
    target.setConfiguration("TestConfiguration");
    return target;
  }
}
