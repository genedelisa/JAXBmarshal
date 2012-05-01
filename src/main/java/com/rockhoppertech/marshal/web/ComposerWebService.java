package com.rockhoppertech.marshal.web;

import java.net.URI;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import com.rockhoppertech.marshal.domain.Composer;
import com.rockhoppertech.marshal.jaxb.ComposerList;
import com.rockhoppertech.marshal.service.ComposerService;

/**
 * 
 * 
 * GET /composerws/composers all composers GET /composerws/{composerid} get
 * specified composer GET /composerws/{familyName} get specified composers POST
 * /composerws create new composer PUT /composerws/{composerid} update composer
 * DELETE /composerws/{composerid} delete composer
 * 
 * 
 * 
 * @author gene <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
@Controller
@RequestMapping("/composerws")
public class ComposerWebService {

	@Resource
	private ComposerService composerService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
	// @RequestMapping(method = RequestMethod.POST, value = "/new")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Composer saveComposer(@RequestBody Composer composer,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("creating " + composer);
		Composer c = composerService.create(composer);
		String requestUrl = request.getRequestURL().toString();
		URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl,
				c.getId());
		response.setHeader("Location", uri.toASCIIString());
		return c;
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Composer updateComposer(@RequestBody Composer composer) {
		logger.debug("updating " + composer);
		// merge in the jpa dao
		composerService.update(composer);
		return composer;
	}

	// curl -i -H "Accept: application/json" http://localhost:8080/jaxbmarshal/composerws/Mozart/Wolfgang/Amadeus
	
	@RequestMapping("/{familyName}/{givenName}/{middleName}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	Composer findComposerByFamilyMiddleAndGivenNames(
			@PathVariable String familyName, @PathVariable String givenName,
			@PathVariable String middleName) {
		logger.debug(familyName);
		return composerService.findComposerByFamilyMiddleAndGivenNames(
				familyName, givenName, middleName);
	}

	@RequestMapping("/{familyName}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	ComposerList findComposerByFamilyName(@PathVariable String familyName) {
		logger.debug(familyName);
		return new ComposerList(
				composerService.findComposerByFamilyName(familyName));
	}

	// curl -i -H "Accept: application/json"
	// http://localhost:8080/concertws/composerws/composers

	@RequestMapping("/composers")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	ComposerList findAll() {
		return new ComposerList(composerService.findAll());
	}

	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("Id") Long id) {
		logger.debug("delete");
		Composer composer = composerService.findByPK(id);
		composerService.delete(composer);

	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception e) {
		logger.error("failed", e);
		logger.error("reason: ", e.getMessage());
		e.printStackTrace();
	}
}
