package de.grinder.executor;

import static org.junit.Assert.*;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.grinder.executor.TargetCreationException;
import de.grinder.executor.TargetFactoryImpl;
import de.grinder.executor.cue.HelloWorld;
import de.grinder.util.cue.CUEAbstraction;

public class TargetFactoryImplTest {
  TargetFactoryImpl targetFactory;
  
  @Before
  public void setUp() {
    targetFactory = new TargetFactoryImpl();
  }

  @Test
  @Ignore
  public void testCreateCUEAbstraction() {
    Element description = new Element("cueAbstraction");
    Element name = new Element("name");
    name.setText("HelloWorld");
    description.addContent(name);
    
    try {
      CUEAbstraction cueAbstraction = targetFactory.createCueAbstraction(description);
      assertTrue(cueAbstraction instanceof HelloWorld);
    } catch (TargetCreationException e) {
      e.printStackTrace();
      fail();
    }
  }
  
  @Test
  public void testCreateUnregisteredCUEAbstraction() {
    Element description = new Element("cueAbstraction");
    Element name = new Element("name");
    name.setText("NoHelloWorld");
    description.addContent(name);

    CUEAbstraction cueAbstraction = null;
    try {
      cueAbstraction = targetFactory.createCueAbstraction(description);
      fail();
    } catch (TargetCreationException e) {
      assertNull(cueAbstraction);
      assertEquals("Cannot create cueAbstraction: " + name.getText(), e.getMessage());
    }
  }
    
  /*
  @Test
  public void testCreateServer() {
    String description = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " +
    		"<target> " +
    		"<servers> " +
    		"<server> " +
    		"<class>de.grinder.targetrunner.servers.TCPServer</class> " +
    		"<port>4444</port> " +
    		"</server> " +
    		"</servers> " +
    		"</target> ";
    
    final TargetController targetController;
    
    try (StringReader reader = new StringReader(description)) {
      targetController = targetFactory.createTargetController(reader);
    } catch (Exception e) {
      fail("Exception: " + e.getMessage());
    }
    
    // Check Servers
    Collection<Listener> listeners = targetController.getListeners();

    assertEquals("Number of listeners should be 1.", 1, listeners.size());
    for (Listener listener : listeners) {
      assertTrue("A TCPServer should be loaded.",
          listener instanceof TCPServer);
    }
  }
  */
}
