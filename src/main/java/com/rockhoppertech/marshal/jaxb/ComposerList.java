package com.rockhoppertech.marshal.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.rockhoppertech.marshal.domain.Composer;

/**
 * @author gene
 * 
 */
@XmlRootElement(name = "composers")
// bind all non-static, non-transient fields
// to XML unless annotated with @XmlTransient
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ComposerList {

	private List<Composer> composers = new ArrayList<Composer>();

	// JAXB needs this default constructor
	public ComposerList() {
	}

	public ComposerList(List<Composer> composers) {
		this.setComposers(composers);
	}

	@XmlElement(name = "composer")
	// @XmlJavaTypeAdapter(value = ComposerAdapter.class)
	public List<Composer> getComposers() {
		return composers;
	}

	public void setComposers(List<Composer> composers) {
		this.composers = new ArrayList<Composer>(composers);
	}

	public ComposerList add(Composer composer) {
		this.composers.add(composer);
		return this;
	}

	public Composer findComposerByFamilyName(String name) {
		for (Composer c : composers) {
			if (c.getFamilyName().equals(name))
				return c;
		}
		return null;
	}
}
