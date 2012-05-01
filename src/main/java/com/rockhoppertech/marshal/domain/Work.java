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
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonBackReference;

/**
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Work.all", query = "SELECT o FROM Work o"),
		@NamedQuery(name = "Work.findByComposer", query = "SELECT o FROM Work AS o where o.composer.id = :composerid")

})
@Table(name = "WORKS")
@XmlRootElement
// @XmlAccessorType(XmlAccessType.PROPERTY)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkType", propOrder = { "id", "title", "completionDate",
		"premiereDate", "instrumentation" })
public class Work implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", nullable = false)
	private Long id;

	@Basic(optional = false)
	@Column(name = "title")
	private String title;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "COMPOSER_ID")
	// prevent a cycle
	@XmlTransient
	private Composer composer;

	@Basic(optional = true)
	@Temporal(TemporalType.DATE)
	private Date completionDate;

	@Basic(optional = true)
	@Temporal(TemporalType.DATE)
	private Date premiereDate;

	@Basic(optional = true)
	@Column(name = "instrumentation")
	private String instrumentation;

	/**
	 * The composer field is marked as xmltransient for jaxb. This will prevent
	 * a cycle. This method will restore the relationship when unmarshalled.
	 * 
	 * @param u
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		this.composer = (Composer) parent;
	}

	/**
	 * Initializes a new Work object.
	 */
	public Work() {
	}

	/**
	 * @param id
	 * @param string
	 */
	public Work(final Long id, final String title) {
		this.id = id;
		this.title = title;
	}

	public Work(final String title) {
		this.title = title;
	}

	public Work(final Composer composer, final String title) {
		this.title = title;
		this.setComposer(composer);
	}

	/*
	 * The many side of one-to-many / many-to-one bidirectional relationships
	 * must be the owning side, hence the mappedBy element cannot be specified
	 * on the ManyToOne annotation.
	 */
	@JsonBackReference
	public Composer getComposer() {
		return this.composer;
	}

	/**
	 * @return id
	 */

	public Long getId() {
		return this.id;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}

	public void setComposer(final Composer composer) {
		this.composer = composer;
	}

	/**
	 * @param i
	 */
	public void setId(final Long i) {
		this.id = i;
	}

	/**
	 * @param string
	 */
	public void setTitle(final String string) {
		this.title = string;
	}

	/**
	 * 
	 * 
	 * @return r
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append(" id=").append(this.id);
		sb.append(" title=\'").append(this.title).append('\'');
		if (this.composer != null) {
			sb.append(" cid=").append(this.composer.getId());
		} else {
			sb.append(" null composer");
		}

		return sb.toString();
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return this.completionDate;
	}

	/**
	 * @param completionDate
	 *            the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	/**
	 * @return the premiereDate
	 */
	public Date getPremiereDate() {
		return this.premiereDate;
	}

	/**
	 * @param premiereDate
	 *            the premiereDate to set
	 */
	public void setPremiereDate(Date premiereDate) {
		this.premiereDate = premiereDate;
	}

	/**
	 * @return the instrumentation
	 */
	public String getInstrumentation() {
		return this.instrumentation;
	}

	/**
	 * @param instrumentation
	 *            the instrumentation to set
	 */
	public void setInstrumentation(String instrumentation) {
		this.instrumentation = instrumentation;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.composer == null) ? 0 : this.composer.hashCode());
		result = prime * result
				+ ((this.title == null) ? 0 : this.title.hashCode());
		return result;
	}

	/**
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
		Work other = (Work) obj;
		if (this.composer == null) {
			if (other.composer != null)
				return false;
		} else if (!this.composer.equals(other.composer))
			return false;
		if (this.title == null) {
			if (other.title != null)
				return false;
		} else if (!this.title.equals(other.title))
			return false;
		return true;
	}

}
