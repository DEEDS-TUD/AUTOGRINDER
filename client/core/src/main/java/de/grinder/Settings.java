package de.grinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {
  private final static Logger LOGGER = LoggerFactory.getLogger(Settings.class);
  
  private final Properties properties;
  
  private static Settings instance;
  
  private Settings() {
    properties = new Properties();
    try (InputStream fis = this.getClass().getClassLoader()
        .getResourceAsStream("config.properties")) {
      properties.load(fis);
    } catch (IOException e) {
      LOGGER.error("Cannot read config.properties file.");
    }
  }
  
  public static Settings getInstance() {
    // TODO Implement thread safety
    if(null == instance ) {
      instance = new Settings();
    }
    
    return instance;
  }
  
  public String getSetting(String name) {
    return (String) properties.get(name);
  }

}
