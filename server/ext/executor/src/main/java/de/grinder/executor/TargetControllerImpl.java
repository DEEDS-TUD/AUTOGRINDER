package de.grinder.executor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grinder.database.Campaign;
import de.grinder.database.ExperimentRun;
import de.grinder.database.TestCase;
import de.grinder.executor.servers.Listener;
import de.grinder.util.message.Message;
import de.grinder.util.message.MessageType;
import de.grinder.util.cue.CUEAbstraction;

/**
 * Controls the execution of experiments for one target system.
 * 
 * @author Michael Tretter
 */
public class TargetControllerImpl implements TargetController, MessageHandler {

  private final static Logger LOGGER = LoggerFactory
      .getLogger(TargetControllerImpl.class);

  /**
   * {@link Listener}s that provide messages for this controller.
   */
  private Set<Listener> listeners;

  /**
   * The {@link CUEAbstraction} to control the target system.
   */
  private CUEAbstraction cueAbstraction;

  /**
   * The current campaign.
   */
  Iterator<TestCase> iterator;

  /**
   * The current experiment.
   */
  private volatile ExperimentRun experiment;

  private Runner runner;

  private ExecutorService executor = Executors.newCachedThreadPool();

  public TargetControllerImpl() {
    super();
    this.listeners = new HashSet<>();
    this.runner = new Runner();
  }

  public void registerListener(Listener listener) {
    LOGGER.info("Register {}", listener);
    listener.setMessageHandler(this);
    listeners.add(listener);
    executor.submit(listener);
  }

  public void unregisterListener(Listener listener) {
    LOGGER.info("Unregister {}", listener);
    listener.setMessageHandler(null);
    listeners.remove(listener);
    listener.stop();
  }

  public Collection<Listener> getListeners() {
    return new HashSet<Listener>(listeners);
  }

  public void setCueAbstraction(CUEAbstraction cueAbstraction) {
    this.cueAbstraction = cueAbstraction;
  }

  public CUEAbstraction getCueAbstraction() {
    return cueAbstraction;
  }

  @Override
  public void setCampaign(Campaign campaign) {
    this.iterator = campaign.iterator();
  }

  @Override
  public Message handle(Message message) {
    Message response;
    switch (message.getType()) {
    case FINISH_EXPERIMENT:
      synchronized (runner) {
        runner.notifyAll();
      }
      response = null;
      break;
    case LOG:
      experiment.appendLog(new String(message.getBody()));
      response = null;
      break;
    case GET_CONFIGURATION:
      response = new Message(MessageType.SEND_CONFIGURATION);
      String configuration = experiment.getConfiguration();
      LOGGER.debug("Sending configuration: {}", configuration);
      response.setBody(configuration.getBytes());
      break;
    default:
      LOGGER.debug("Unrecognized message: {}", message);
      response = null;
      break;
    }
    return response;
  }

  /**
   * Start the campaign for this Target.
   * 
   * Starts a new Thread that sequentially runs each experiment of the campaign.
   */
  public void start() {
    if (iterator == null) {
      LOGGER.warn("No Campaign loaded.");
    }

    if (runner.stopped) {
      runner.stopped = false;
      executor.submit(runner);
    }
  }

  @Override
  public void stop() {
    runner.stopped = true;
  }

  @Override
  public void reset() {
    // TODO Break execution of runner
    executor.submit(reset);
  }

  /**
   * Run the campaign that is loaded for this target.
   * 
   * This method loads the next experiment, if it exists, resets the target, and
   * waits for the end of the experiment run.
   */
  private class Runner implements Runnable {
    private volatile boolean stopped = true;

    public synchronized void run() {

      new FutureTask<Void>(start).run();
      while (!stopped && iterator.hasNext()) {

        TestCase testCase = iterator.next();
        LOGGER.debug("Test case id: {}", testCase.getId());

        LOGGER.info("Starting experiment...");
        experiment = new ExperimentRun(testCase);
//        experiment.save();
        LOGGER.debug("Experiment id: {}", experiment.getId());

//        executor.submit(reset); - Running 2 subsequent submit() can lead to unpredictable behavior (execution might be in reverse order!)
        executor.submit(run);
        try {
          LOGGER.info("Waiting for end of experiment...");
          wait();
          LOGGER.info("Continue experiment.");
        } catch (InterruptedException e) {
          // TODO Handle interrupt
        }

        experiment.save();
      }
      this.stopped = true;
      executor.submit(stop);
    }
  }

  private Callable<Void> start = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Starting {}...", cueAbstraction);
      cueAbstraction.start();
      return null;
    }
  };

  private Callable<Void> stop = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Stopping {}...", cueAbstraction);
      cueAbstraction.stop();
      return null;
    }
  };

  private Callable<Void> reset = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Resetting {}...", cueAbstraction);
      cueAbstraction.reset();
      return null;
    }
  };

  private Callable<Void> run = new Callable<Void>() {
    @Override
    public Void call() {
      LOGGER.info("Starting {}...", experiment);
      cueAbstraction.runExperiment();
      return null;
    }
  };
}
