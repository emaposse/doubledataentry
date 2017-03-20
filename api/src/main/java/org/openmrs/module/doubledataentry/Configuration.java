package org.openmrs.module.doubledataentry;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.module.htmlformentry.HtmlForm;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Willa Mhawila on 3/13/17.
 */
@Entity(name = "doubledataentry.Configuration")
@Table(name = "doubledataentry_configuration")
public class Configuration extends BaseOpenmrsMetadata implements Serializable {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "html_form_id")
	private HtmlForm htmlForm;
	
	@Column
	private Integer revision = 1;
	
	@Column
	private Float frequency;
	
	@Column
	private Boolean published = false;
	
	public Configuration() {
		this.setName("Configuration");
	}
	
	public Configuration(HtmlForm htmlForm, Float frequency) {
		this.setName("Configuration");
		this.htmlForm = htmlForm;
		this.frequency = frequency;
	}
	
	public Configuration(HtmlForm htmlForm, Float frequency, Boolean published) {
		this.setName("Configuration");
		this.htmlForm = htmlForm;
		this.frequency = frequency;
		this.published = published;
	}
	
	@Override
	public Integer getId() {
		return this.id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public HtmlForm getHtmlForm() {
		return htmlForm;
	}
	
	public void setHtmlForm(HtmlForm htmlForm) {
		this.htmlForm = htmlForm;
	}
	
	public Integer getRevision() {
		return revision;
	}
	
	public void revise(Integer revision) {
		this.revision = revision;
	}
	
	public Float getFrequency() {
		return frequency;
	}
	
	public void setFrequency(Float frequency) {
		this.frequency = frequency;
	}
	
	public Boolean getPublished() {
		return published;
	}
	
	public void setPublished(Boolean published) {
		this.published = published;
	}
}
