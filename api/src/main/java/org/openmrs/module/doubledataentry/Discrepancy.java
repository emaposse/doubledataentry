package org.openmrs.module.doubledataentry;

import org.openmrs.*;
import org.openmrs.module.htmlformentry.HtmlForm;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Willa Mhawila on 3/13/17.
 */
@Entity(name = "doubledataentry.Discrepancy")
@Table(name = "doubledataentry_discrepancy")
public class Discrepancy {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "html_form_id", nullable = false)
	private HtmlForm htmlForm;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Concept question;
	
	@Column(name = "initial_answer")
	private String initialAnswer;
	
	@Column(name = "second_answer")
	private String secondAnswer;
	
	@Column(name = "chosen_answer")
	private String chosenAnswer;
	
	@Column(name = "answer_type")
	private String answerType;
	
	@ManyToOne
	@JoinColumn
	private User creator;
	
	@ManyToOne
	@JoinColumn(name = "provider_id")
	private Provider provider;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;
	
	@ManyToOne
	@JoinColumn(name = "encounter_id")
	private Encounter encounter;
	
	@Column(name = "date_recorded")
	private Date dateRecorded;
	
	@Column(length = 38)
	private String uuid;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public HtmlForm getHtmlForm() {
		return htmlForm;
	}
	
	public void setHtmlForm(HtmlForm htmlForm) {
		this.htmlForm = htmlForm;
	}
	
	public Concept getQuestion() {
		return question;
	}
	
	public void setQuestion(Concept question) {
		this.question = question;
	}
	
	public String getInitialAnswer() {
		return initialAnswer;
	}
	
	public void setInitialAnswer(String initialAnswer) {
		this.initialAnswer = initialAnswer;
	}
	
	public String getSecondAnswer() {
		return secondAnswer;
	}
	
	public void setSecondAnswer(String secondAnswer) {
		this.secondAnswer = secondAnswer;
	}
	
	public String getChosenAnswer() {
		return chosenAnswer;
	}
	
	public void setChosenAnswer(String chosenAnswer) {
		this.chosenAnswer = chosenAnswer;
	}
	
	public String getAnswerType() {
		return answerType;
	}
	
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Provider getProvider() {
		return provider;
	}
	
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Encounter getEncounter() {
		return encounter;
	}
	
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	
	public Date getDateRecorded() {
		return dateRecorded;
	}
	
	public void setDateRecorded(Date dateRecorded) {
		this.dateRecorded = dateRecorded;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		Discrepancy discrepancy = (Discrepancy) o;
		
		return uuid.equals(discrepancy.uuid);
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}
