package de.grinder.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent representation of a target system.
 * 
 * @author Michael Tretter
 *
 */
@Entity
@Table(name = "targets")
public class Target {
  
  @Id
  @GeneratedValue
  private int id;
  private String name;
  
  @Column(length = 4096)
  private String configuration;

  public Target() {
    super();
  }
  
  public Target(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getConfiguration() {
    return configuration;
  }
  
  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }
}
