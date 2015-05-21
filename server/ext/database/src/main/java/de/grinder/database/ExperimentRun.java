package de.grinder.database;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table( name = "experiment_run" )
public class ExperimentRun {
  
  @Id
  @GeneratedValue
  private int id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date time;

  @ManyToOne
  private TestCase testCase;
  
  @Column(columnDefinition="LONGTEXT")
  private String log;
  
  public ExperimentRun() {
    super();
  }
  
  public ExperimentRun(final TestCase testCase) {
    this();
    this.time = new Date();
    this.log = new String();
    this.testCase = testCase;
  }

  public int getId() {
    return id;
  }
  
  public Date getTime() {
    return time;
  }

  public String getLog() {
    return log;
  }

  public void appendLog(String text) {
    log = log + text;
  }

  public TestCase getTestCase() {
    return testCase;
  }
  
  public String getConfiguration() {
    return testCase.getConfiguration();
  }

  @Override
  public String toString() {
    return "Result [id=" + id + ", time=" + time + "]";
  }
  
  public void save() {
    EntityManager em = Database.getEntityManager();
    
    em.getTransaction().begin();
    if (!em.contains(this)){
      em.merge(this);
      em.flush();
    }
    em.getTransaction().commit();
    
  }
}