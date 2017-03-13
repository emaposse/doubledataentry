package org.openmrs.module.doubledataentry;

import org.openmrs.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by Willa Mhawila on 3/13/17.
 */
@Entity(name = "doubledataentry.Participant")
@Table(name = "doubledataentry_participant")
public class Participant {
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
