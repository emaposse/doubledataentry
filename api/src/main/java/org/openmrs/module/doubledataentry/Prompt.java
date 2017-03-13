package org.openmrs.module.doubledataentry;

import org.openmrs.User;
import org.openmrs.module.htmlformentry.HtmlForm;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Willa Mhawila on 3/13/17.
 */
@Entity(name = "doubledataentry.Prompt")
@Table(name = "doubledataentry_prompt")
public class Prompt {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "time_prompted")
    private Date timePrompted;

    @ManyToOne
    @JoinColumn(name = "html_form_id")
    private HtmlForm htmlForm;

    @Column(length = 38, nullable = false)
    private String uuid;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimePrompted() {
        return timePrompted;
    }

    public void setTimePrompted(Date timePrompted) {
        this.timePrompted = timePrompted;
    }

    public HtmlForm getHtmlForm() {
        return htmlForm;
    }

    public void setHtmlForm(HtmlForm htmlForm) {
        this.htmlForm = htmlForm;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prompt prompt = (Prompt) o;

        return uuid.equals(prompt.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
