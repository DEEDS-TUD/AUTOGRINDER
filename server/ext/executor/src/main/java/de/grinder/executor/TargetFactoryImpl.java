package de.grinder.executor;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.executor.servers.AbstractListener;
import de.grinder.executor.servers.TCPServer;
import de.grinder.util.cue.CUEAbstraction;
import de.grinder.util.cue.CUEAbstractionRegistry;

public class TargetFactoryImpl implements TargetFactory {
  private final static Logger LOGGER = LoggerFactory
      .getLogger(TargetFactoryImpl.class);
  
  public TargetController createTargetController(final Reader description) throws TargetCreationException {
    TargetControllerImpl targetController = new TargetControllerImpl();
    
    LOGGER.info("Parsing xml target configuration");
    final Document doc = parseXML(description);
    
    // Create cueAbstraction
    final Element cueDescription = doc.getRootElement().getChild(
        "cue_abstraction");
    CUEAbstraction cueAbstraction = createCueAbstraction(cueDescription);
    targetController.setCueAbstraction(cueAbstraction);
    
    // Create listeners 
    @SuppressWarnings("unchecked")
    final List<Element> serverDescriptions = doc.getRootElement()
        .getChild("servers").getChildren();
    for (Element serverDescription : serverDescriptions) {
      AbstractListener abstractServer = createServer(serverDescription);
      targetController.registerListener(abstractServer);
    }
    
    return targetController;
  }
  
  /**
   * Parse the XML file and return the XML Structure as {@link Document}.
   * 
   * @param file
   *          The XML file
   * @return The XML Structure as {@link Document}
   * @throws TargetCreationException
   */
  public Document parseXML(final Reader description)
      throws TargetCreationException {
    // TODO Add a validity check of the given XML
    Document document;

    try {
      final SAXBuilder builder = new SAXBuilder();
      document = builder.build(description);
    } catch (IOException | JDOMException e) {
      throw new TargetCreationException(e);
    }

    return document;
  }

  /**
   * Create the {@link AbstractListener}s that are specified in the xml
   * configuration.
   * 
   * @return
   */
  public AbstractListener createServer(Element description)
      throws TargetCreationException {
    final String clazz = description.getChildText("class");
    final int port = Integer.parseInt(description.getChildText("port"));

    final TCPServer server;

    try {
      server = (TCPServer) Class.forName(clazz).newInstance();
      server.setPort(port);
    } catch (Exception e) {
      throw new TargetCreationException("Creation of AbstractServer failed.", e);
    }

    return server;
  }

  /**
   * Return a {@link CUEAbstraction} for the given description.
   * 
   * @param description
   *          Description of a cueAbstraction
   * @return {@link CUEAbstraction} specified by the description
   * @throws TargetCreationException
   *           If the given description is not a valid cueAbstraction
   */
  public CUEAbstraction createCueAbstraction(final Element description)
      throws TargetCreationException {
    final Collection<Class<? extends CUEAbstraction>> cueAbstractions = CUEAbstractionRegistry.getInstance().getCUEAbstractions();
    final String name = description.getChildText("name");
    LOGGER.info("Creating cueAbstraction: " + name);
    
    // Look up given name
    for(Class<? extends CUEAbstraction> cueAbstraction: cueAbstractions) {
      if(name.equals(cueAbstraction.getName())) {
        LOGGER.info("Found cueAbstraction: " + name);
        try {
          return cueAbstraction.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          LOGGER.error("Failed to instantiate {}", cueAbstraction.getName());
        }
      }
    }

    // TODO Replace Exception with a more appropriate reaction.
    LOGGER.info("Cannot find cueAbstraction: " + name);
    throw new TargetCreationException("Cannot create cueAbstraction: " + name.toString());
  }
}
