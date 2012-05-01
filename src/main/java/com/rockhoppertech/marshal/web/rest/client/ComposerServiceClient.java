package com.rockhoppertech.marshal.web.rest.client;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.rockhoppertech.marshal.domain.Composer;
import com.rockhoppertech.marshal.jaxb.ComposerList;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ComposerServiceClient {
	private Logger log = LoggerFactory.getLogger(ComposerServiceClient.class);
	private static final String SERVICE_URL = "http://localhost:8080/jaxbmarshal/composerws";

	RestTemplate restTemplate = new RestTemplate();

	public Composer create(Composer composer) {
		log.debug("creating " + composer);
		ResponseEntity<Composer> response = restTemplate.postForEntity(
				SERVICE_URL, composer, Composer.class);
		Composer posted = response.getBody();
		URI url = response.getHeaders().getLocation();
		log.debug("posted URL location is " + url.toString());

		return posted;
	}

	public ComposerList finalAll() {
		log.debug("finding all composers");
		ComposerList composers = restTemplate.getForObject(SERVICE_URL
				+ "/composers", ComposerList.class);
		return composers;
	}

	public ComposerList findByFamilyName(String familyName) {
		log.debug("finding composers by family name" + familyName);
		Map<String, String> vars = Collections.singletonMap("familyName",
				familyName);

		ComposerList cl = restTemplate.getForObject(SERVICE_URL
				+ "/{familyName}", ComposerList.class, vars);
		return cl;
	}

	public void update(Composer composer) {
		log.debug("updating " + composer);
		restTemplate.put(SERVICE_URL, composer);
	}

	public void delete(Composer composer) {
		log.debug("deleting " + composer);
		restTemplate.delete(SERVICE_URL + "/{Id}", composer.getId());
	}

}
