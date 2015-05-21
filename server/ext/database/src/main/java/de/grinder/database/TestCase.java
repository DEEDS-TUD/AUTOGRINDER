package de.grinder.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "testcases")
public class TestCase {
  
  @Id
  @GeneratedValue
  private int id;

  @Column(columnDefinition="LONGTEXT")
  private String configuration;
  
  public TestCase() {
    super();
  }

  public int getId() {
    return id;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }
  
  @Override
  public String toString() {
    return String.format("TestCase [id=%d]", id);
  }
}
