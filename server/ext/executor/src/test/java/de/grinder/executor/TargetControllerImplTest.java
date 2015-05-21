package de.grinder.executor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.grinder.executor.TargetControllerImpl;
import de.grinder.executor.servers.Listener;
import de.grinder.util.cue.CUEAbstraction;

public class TargetControllerImplTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testTargetControllerImpl() {
    TargetControllerImpl targetController = new TargetControllerImpl();

    assertNull(targetController.getCueAbstraction());

    Collection<Listener> listeners = targetController.getListeners();
    assertNotNull(listeners);
    assertTrue(listeners.isEmpty());
  }

  @Test
  public void testRegisterAndUnregisterListener() {
    TargetControllerImpl targetController = new TargetControllerImpl();
    Listener listener = mock(Listener.class);

    // Register listener
    targetController.registerListener(listener);

    Collection<Listener> listeners = targetController.getListeners();
    assertEquals(1, listeners.size());

    verify(listener).setMessageHandler(targetController);

    // Unregister listener
    targetController.unregisterListener(listener);
    listeners = targetController.getListeners();
    assertEquals(0, listeners.size());
  }

  @Test
  @Ignore
  public void testExperimentLivecycle() {
    TargetControllerImpl targetController = new TargetControllerImpl();

    CUEAbstraction cueAbstraction = mock(CUEAbstraction.class);
    targetController.setCueAbstraction(cueAbstraction);

    targetController.start();
    verify(cueAbstraction).start();
    
    targetController.reset();
    verify(cueAbstraction).reset();
    
    targetController.stop();
    verify(cueAbstraction).stop();
  }
}
