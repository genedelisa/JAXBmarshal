package com.rockhoppertech.marshal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rockhoppertech.marshal.domain.Composer;
import com.rockhoppertech.marshal.domain.Work;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
@Service
public class DefaultComposerService implements ComposerService {
	private Map<String, Composer> composers;

	public DefaultComposerService() {
		this.composers = new HashMap<String, Composer>();
		Composer composer = null;
		Long id = 1L;
		this.create(composer = new Composer("Wolfgang", "Amadeus", "Mozart"));
		composer.setId(id++);
		composer.add(new Work("Jupiter Symphony"));
		composer.add(new Work("Piano Concerto No. 25"));
		this.create(composer = new Composer("Johann", "Sebastian", "Bach"));
		composer.setId(id++);
		composer.add(new Work("Goldberg Variations"));
	}

	@Override
	public Composer create(Composer composer) {
		this.composers.put(composer.getFullName(), composer);
		return composer;
	}

	@Override
	public Composer findByPK(Long id) {
		// boy is this clunky
		List<Composer> all = findAll();
		for (Composer composer : all) {
			if (composer.getId().equals(id))
				return composer;
		}
		return null;
	}

	@Override
	public List<Composer> findAll() {
		final List<Composer> all = new ArrayList<Composer>();
		for (final Composer composer : this.composers.values()) {
			all.add(composer);
		}
		return Collections.unmodifiableList(all);
	}

	@Override
	public Composer findComposerByFamilyMiddleAndGivenNames(String familyName,
			String givenName, String middleName) {
		List<Composer> all = findAll();
		Composer result = null;
		for (Composer composer : all) {
			if (composer.getFamilyName().equalsIgnoreCase(familyName)
					&& composer.getGivenName().equalsIgnoreCase(givenName)
					&& composer.getMiddleName().equalsIgnoreCase(middleName)) {
				result = composer;
				break;
			}
		}
		return result;
	}

	@Override
	public Composer update(Composer composer) {
		if (this.composers.containsKey(composer.getFullName())) {
			this.composers.put(composer.getFullName(), composer);
		} else {
			throw new IllegalArgumentException(
					"composer is unknown. How can I update that? " + composer);
		}
		return composer;
	}

	@Override
	public void delete(Composer composer) {
		if (this.composers.containsKey(composer.getFullName())) {
			this.composers.remove(composer);
		} else {
			throw new IllegalArgumentException(
					"Composer is unknown. How can I delete that? " + composer);
		}
	}

	@Override
	public List<Composer> findComposerByFamilyName(String familyName) {
		List<Composer> results = new ArrayList<Composer>();
		List<Composer> all = findAll();
		for (Composer composer : all) {
			if (composer.getFamilyName().equals(familyName))
				results.add(composer);
		}
		return results;
	}

}
