package de.grinder.util.cue;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Singleton registry for all known cueAbstractions
 * 
 * @author Michael Tretter
 *
 */
public class CUEAbstractionRegistry {

  private static CUEAbstractionRegistry instance;

  private Collection<Class<? extends CUEAbstraction>> cueAbstractions;

  private CUEAbstractionRegistry() {
    super();
    cueAbstractions = new LinkedList<>();
  }

  public static CUEAbstractionRegistry getInstance() {
    // TODO Implement thread safety
    if (null == instance) {
      instance = new CUEAbstractionRegistry();
    }

    return instance;
  }

  public Collection<Class<? extends CUEAbstraction>> getCUEAbstractions() {
    return Collections.unmodifiableCollection(cueAbstractions);
  }

  public void register(Class<? extends CUEAbstraction> clazz) {
    cueAbstractions.add(clazz);
  }
}
