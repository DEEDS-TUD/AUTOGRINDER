package de.grinder;

import static org.junit.Assert.*;

import org.junit.Test;

public class SettingsTest {

  @Test
  public void testGetInstance() {
    Settings settings = Settings.getInstance();
    Settings settings1 = Settings.getInstance();
    
    assertNotNull("Settings is not null", settings);
    assertTrue("Only one instance of Settings", settings == settings1);
  }
  
  @Test
  public void testGetSetting() {
    Settings settings = Settings.getInstance();
    String defaultConfigPath = settings.getSetting("defaultConfigPath");
    
    assertEquals("Correct defaultConfigPath loaded", ".", defaultConfigPath);
  }
  
  @Test
  public void testGetSetting_missingSetting() {
    Settings settings = Settings.getInstance();
    String missingSetting = settings.getSetting("missingSetting");
    
    assertNull("Missing setting is null", missingSetting);
  }

}
