package de.grinder.autosar;

import java.io.File;
import java.io.IOException;
// import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.util.cue.CUEAbstraction;
import de.grinder.util.message.Proxy;

/**
 * This class is the {@link CUEAbstraction} for AUTOSAR systems.
 * 
 * @author DEEDS Group, 2014
 * 
 */
public class AutosarAbstraction implements CUEAbstraction {
  private static final Logger LOGGER = LoggerFactory.getLogger(AutosarAbstraction.class);
  
  // Experiment Configuration Data
  private static final int PORT = 4445;
  private static final String IMAGE = "C:\\Users\\tretter\\EBWorkspace\\os_demo_OS_ExecTimeMon\\output\\bin\\PA_XPC56XXL_os_demo.out";
  private static final String CONNECTION = "XKT564L";

  private Proxy grinderProxy;

  private MultiDebugger d;

  public AutosarAbstraction() {
    d = new MultiDebugger();
  }

  private void halt() {
	  while (true) ;
  }

  @Override
  public void start() {
    LOGGER.debug("Starting {}", this);
    try {
      LOGGER.info("Starting new MULTI debugger instance");
      d.start(new File(IMAGE), PORT);
      LOGGER.info("Sending connection request to MULTI");
      d.send("disconnect; connect \"" + CONNECTION + "\"");
      while( !d.readLine().contains("Target cpu") ) ;

      LOGGER.info("Sending prepare_target request to MULTI");
      d.send("prepare_target -verify=sparse");
      String prepareTargetResponse;
      do {
    	  prepareTargetResponse = d.readLine();
    	  if (prepareTargetResponse.contains("INCOHERENT")) {
			  LOGGER.error("MULTI: Target is incoherent.");
			  this.halt();
    	  }
      } while (!prepareTargetResponse.contains("verify: no incoherencies detected"));
 
      LOGGER.info("Sending hardbrk config request to MULTI");
      d.send("hardbrk write -- messageIndication { if (messageIndication >= 0xCAFE + 1 && messageIndication <= 0xCAFE + 3) { if (messageIndication == 0xCAFE + 1) { echo \"GET_CONFIGURATION\"; } if (messageIndication == 0xCAFE + 2) { echo \"FINISH_EXPERIMENT\"; } if (messageIndication == 0xCAFE + 3) { echo \"LOG\"; } } else {resume} }");
      while( !d.readLine().contains("resume") ) ;
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }

    try {
      grinderProxy = new Proxy("127.0.0.1", 4444);
      grinderProxy.connect();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  @Override
  public void stop() {
    try {
      LOGGER.info("Stopping {}, sending reset request to MULTI", this);
      d.send("reset");
      while( !d.readLine().contains("Reset and halted") ) ;
      grinderProxy.disconnect();
      d.stop();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void reset() {
    try {
      LOGGER.info("Resetting {}, sending reset request to MULTI", this);
      d.send("reset");
      while( !d.readLine().contains("Reset and halted") ) ;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void runExperiment() {
    try {
      LOGGER.info("Send to MULTI: reset");
      d.send("reset");
      while( !d.readLine().contains("Reset and halted") ) ;

      LOGGER.info("Send to MULTI: continue");
      d.send("c");

      String debuggerOutput;
	  SimpleDateFormat df = new SimpleDateFormat( "yyyyMMdd-HHmmss" );
	  String date = df.format(new Date());
	  boolean logfileSentToDB = false;
      do {
    	  debuggerOutput = d.readLine();
    	  if (debuggerOutput.contains("GET_CONFIGURATION")) {
    		  LOGGER.info("Received from MULTI: GET_CONFIGURATION");
    		  d.readLine();							// skip additional message

    		  String configuration = grinderProxy.getConfiguration();
    		  LOGGER.info("Configuration is \"{}\", writing to configuration.", configuration);
    		  d.send(String.format("memwrite -str configuration \"%s\"; c", configuration));
    	  }
    	  else if (debuggerOutput.contains("LOG")) {
    		  LOGGER.info("Received from MULTI: LOG");
    		  d.readLine();							// skip additional message

    		  String debuggerCommand = "p msgType";
    		  d.send(debuggerCommand);
    		  int msgType;
    		  // Output format: msgType = 273 << Dead >>
    		  Matcher m = Pattern.compile("\\w+ = (\\w+).*").matcher(d.readLine());
    		  if (m.matches()) {
    			  msgType = Integer.parseInt(m.group(1));
    			  LOGGER.debug("Parsed msgType as \"{}\"", msgType);
    		  } else {
    			  LOGGER.error("Parsing debugger response to command \"{}\" failed.", debuggerCommand);
    			  while (true) ;
//    			  break;
    		  }

    		  debuggerCommand = "p /a *message";
    		  d.send(debuggerCommand);
    		  String msgContent;
			  // Output format: "logAccelPedalPosition" << Part of Expression Dead >>
    		  m = Pattern.compile("\"(.+)\".*").matcher(d.readLine());
    		  if (m.matches()) {
    			  msgContent = m.group(1);
    			  LOGGER.debug("Parsed msgContent as \"{}\"", msgContent);
    		  } else {
    			  LOGGER.error("Parsing debugger response to command \"{}\" failed.", debuggerCommand);
    			  while (true) ;
//    			  break;
    		  }

    		  // Parse error type (if any)
    		  String errorMessage = "";
    		  if ((msgType & 0x0100) != 0) {
    			  errorMessage += "LOG_ERR_EMCONIGFMISSING ";
    		  }
    		  if ((msgType & 0x0200) != 0) {
    			  errorMessage += "LOG_ERR_EMCONFIGMALFORMED ";
    		  }
    		  if ((msgType & 0x0400) != 0) {
    			  errorMessage += "LOG_ERR_EMNOTCONFIGURED ";
    		  }
    		  if ((msgType & 0x0800) != 0) {
    			  errorMessage += "LOG_ERR_EMVICTIMMALFORMED ";
    		  }
    		  
    		  if ((msgType & 0x0001) != 0) {
    			  LOGGER.info("Target notification: \"{}\", msgContent \"{}\".", errorMessage, msgContent);
    		  }
    		  if ((msgType & 0x0002) != 0) {
    			  LOGGER.error("Target error: \"{}\", msgContent \"{}\".", errorMessage, msgContent);
    			  while (true) ;
    		  }

    		  if ((msgType & 0x0010) != 0) {
    			  LOGGER.info("Target: \"{}\".", msgContent);
    		  }

    		  if ((msgType & 0x0020) != 0) {
    			  grinderProxy.log(msgContent);
    		  }

    		  if ((msgType & 0x0040) != 0) {
    			  if (!logfileSentToDB) {
    				  grinderProxy.log("c:\\GM_ACC\\logs\\" + date + "-*.log");
    				  logfileSentToDB = true;
    			  }
    			  String logFilename = String.format("c:\\GM_ACC\\logs\\%s-%s.log", date, msgContent);
    			  LOGGER.info("Logging \"{}\" to file \"{}\".", msgContent, logFilename);
    			  d.send(String.format("output -multi -noprefix -copytopane no %s; p %s; output -multi -normal", logFilename, msgContent));
    			  while( !d.readLine().contains("MULTI command output is not redirected.") ) ;
    		  }

    		  
    		  d.send("c");

    	  }
    	  else if (debuggerOutput.contains("FINISH_EXPERIMENT")) {
    	      LOGGER.info("Received from MULTI: FINISH_EXPERIMENT");
    		  break;
    	  }
    	  else {
    		  LOGGER.info("Received unhandled content \"{}\"", debuggerOutput);
    	  }
      } while (true);

      grinderProxy.finishExperiment();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return;
  }
}
