package de.grinder.executor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import de.grinder.database.Target;

public class Executor {
  
  private Collection<TargetController> controllers = new ArrayList<>();
  
  private static Executor instance = new Executor();
  
  public TargetController addController(Target target) {
    TargetFactory factory = new TargetFactoryImpl();
        
    try (StringReader reader = new StringReader(target.getConfiguration())) {
      TargetController controller = factory.createTargetController(reader);
      controllers.add(controller);
      return controller;
    } catch (TargetCreationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("<> Failure : don't return target controller.");
    return null;
  }

  public static Executor instance() {
    return instance;
  }

}
