package de.grinder.executor.cue;

import java.io.File;

/**
 * HelloWorld is a AbstractProcess for the HelloWorld target program.
 * 
 * @author Michael Tretter
 * 
 */
public class HelloWorld extends ProcessAbstraction {
  private final static String EXECUTABLE = ".." + File.separator + "helloworld"
      + File.separator + "helloworld";

  public HelloWorld() {
    super(new File(EXECUTABLE));
  }

  public String getName() {
    return "HelloWorld";
  }
}