package org.openmrs.module.doubledataentry;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Willa Mhawila on 3/13/17.
 */
@Entity(name = "doubledataentry.ConfigurationRevision")
@Table(name = "doubledataentry_configuration_revision")
public class ConfigurationRevision implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "configuration_id")
	private Configuration configuration;
	
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	
	@Column(name = "end_date", nullable = false)
	private Date endDate;
	
	@Column(nullable = false)
	private Float frequency;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Float getFrequency() {
		return frequency;
	}
	
	public void setFrequency(Float frequency) {
		this.frequency = frequency;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		ConfigurationRevision that = (ConfigurationRevision) o;
		
		return id.equals(that.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
