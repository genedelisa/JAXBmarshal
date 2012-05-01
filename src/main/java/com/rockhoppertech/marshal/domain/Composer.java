/*
 * $Id$
 * 
 * Copyright 1998,1999,2000,2001,2002,2003 by Rockhopper Technologies, Inc.
 * (RTI), 75 Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A. All
 * rights reserved.
 * 
 * This software is the confidential and proprietary information of Rockhopper
 * Technologies, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with RTI.
 */

/*
 * Created on Oct 25, 2004
 * 
 */
package com.rockhoppertech.marshal.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Composer.all", query = "SELECT c FROM Composer c JOIN FETCH c.works")
})
@Table(name = "COMPOSERS")
@XmlRootElement
// @XmlAccessorType(XmlAccessType.PROPERTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComposerType", propOrder = { "id", "familyName", "givenName",
		"middleName", "works" })
public class Composer implements Serializable, Comparable<Composer> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = true)
	@XmlElement(name = "givenName", required = false)
	@Column(name = "givenName", length = 128)
	private String givenName;

	@Basic(optional = true)
	@XmlElement(name = "middleName", required = false)
	@Column(name = "middleName", length = 128)
	private String middleName;

	@Basic(optional = false)
	@XmlElement(name = "familyName", required = true)
	@Column(name = "familyName", length = 255)
	private String familyName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "composer", targetEntity = Work.class, fetch = FetchType.EAGER)
	// see
	// http://jira.codehaus.org/browse/JACKSON-235?page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#issue-tabs
	@JsonManagedReference
	// forward reference, serialized
	// http://wiki.fasterxml.com/JacksonFeatureBiDirReferences
	@XmlElementWrapper(name = "works")
	@XmlElement(name = "work", required = true)
	private Set<Work> works = new HashSet<Work>();


	/**
	 * Initializes a new Composer object.
	 */
	public Composer() {
	}

	/**
	 * @param id
	 * @param ln
	 * @param on
	 */
	public Composer(final String givenName, final String familyName) {
		this.familyName = familyName;
		this.givenName = givenName;
	}

	public Composer(final String givenName, final String middleName,
			final String familyName) {
		this.familyName = familyName;
		this.givenName = givenName;
		this.middleName = middleName;
	}

	/**
	 * @param work
	 */
	public Composer add(final Work work) {
		this.getWorks().add(work);
		work.setComposer(this);
		return this;
	}

	public int compareTo(final Composer o) {
		return this.getFullName().compareTo(o.getFullName());
	}

	public String getFullName() {
		if (this.familyName == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder(this.familyName);
		if (this.givenName != null) {
			sb.append(',');
			sb.append(this.givenName);
		}
		if (this.middleName != null) {
			sb.append(',');
			sb.append(this.middleName);
		}
		return sb.toString();
	}

	public void setFullName(String s) {
		// no op to make JSON happy
	}

	/**
	 * @return the identifier
	 */

	public Long getId() {
		return this.id;
	}

	/**
	 * @return the last name
	 */
	public String getFamilyName() {
		return this.familyName;
	}


	@JsonManagedReference
	public Set<Work> getWorks() {
		return this.works;
	}

	/**
	 * @param i
	 */
	public void setId(final Long i) {
		this.id = i;
	}

	/**
	 * 
	 * @param string
	 */
	public void setFamilyName(final String familyName) {
		this.familyName = familyName;
	}


	public boolean remove(Work work) {
		return this.getWorks().remove(work);
	}

	/**
	 * 
	 * 
	 * @return r
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Composer ");
		sb.append(" id:\'").append(this.getId()).append('\'');
		sb.append(" ln:\'").append(this.getFamilyName()).append('\'');
		sb.append(" gn:\'").append(this.getGivenName()).append('\'');
		sb.append(" mn:\'").append(this.getMiddleName()).append('\'');

		Collection<Work> ws = this.getWorks();
		if ((ws != null) && (ws.size() > 0)) {
			sb.append('\n');
			for (final Work w : ws) {
				sb.append('\t').append(w.getTitle()).append('\n');
			}
		}
		return sb.toString();
	}

	/**
	 * @return the givenName
	 */
	public String getGivenName() {
		return this.givenName;
	}

	/**
	 * @param givenName
	 *            the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.getFamilyName() == null) ? 0 : this.getFamilyName()
						.hashCode());
		result = prime
				* result
				+ ((this.getGivenName() == null) ? 0 : this.getGivenName()
						.hashCode());
		result = prime
				* result
				+ ((this.getMiddleName() == null) ? 0 : this.getMiddleName()
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Composer other = (Composer) obj;
		if (this.getFamilyName() == null) {
			if (other.getFamilyName() != null)
				return false;
		} else if (!this.getFamilyName().equals(other.getFamilyName()))
			return false;
		if (this.getGivenName() == null) {
			if (other.getGivenName() != null)
				return false;
		} else if (!this.getGivenName().equals(other.getGivenName()))
			return false;
		if (this.getMiddleName() == null) {
			if (other.getMiddleName() != null)
				return false;
		} else if (!this.getMiddleName().equals(other.getMiddleName()))
			return false;
		return true;
	}

}
