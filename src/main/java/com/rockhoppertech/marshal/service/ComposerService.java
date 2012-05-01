package com.rockhoppertech.marshal.service;

import java.util.List;

import com.rockhoppertech.marshal.domain.Composer;

/**
 * @author Gene De Lisa
 * 
 */
public interface ComposerService {

	public Composer create(Composer composer);

	public Composer findByPK(Long id);

	public List<Composer> findAll();

	public Composer findComposerByFamilyMiddleAndGivenNames(
			String familyName, String givenName, String middleName);

	public Composer update(Composer composer);

	public void delete(Composer composer);

	public List<Composer> findComposerByFamilyName(String familyName);

}
