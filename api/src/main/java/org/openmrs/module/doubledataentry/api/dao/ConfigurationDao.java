/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.doubledataentry.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.doubledataentry.Configuration;
import org.openmrs.module.doubledataentry.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.security.krb5.Config;

@Repository("doubledataentry.ConfigurationDao")
public class ConfigurationDao {
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Configuration saveConfiguration(Configuration configuration) throws DAOException {
		sessionFactory.getCurrentSession().saveOrUpdate(configuration);
		return configuration;
	}
	
	public Configuration getConfiguration(Integer id) {
		return (Configuration) sessionFactory.getCurrentSession().get(Configuration.class, id);
	}
	
	public Configuration getConfiguration(String uuid) {
		return (Configuration) sessionFactory.getCurrentSession().createCriteria(Configuration.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
}
