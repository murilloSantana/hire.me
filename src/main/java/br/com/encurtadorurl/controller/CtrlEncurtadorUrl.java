package br.com.encurtadorurl.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.encurtadorurl.model.Statistics;
import br.com.encurtadorurl.model.Url;
import br.com.encurtadorurl.service.EncurtadorUrlService;

@RestController
public class CtrlEncurtadorUrl {

	@Autowired
	private Statistics stat;
	@Autowired
	private EncurtadorUrlService encurtadorUrlService;

	@RequestMapping(value = { "/shortenurl" }, method = RequestMethod.GET)
	public ResponseEntity<?> shortenUrl(@ModelAttribute Url urlObj, HttpServletRequest request) {
		stat.setStartRequest(Long.parseLong(request.getAttribute("startRequest").toString()));
		stat.setIpUser(request.getRemoteAddr());

		try {
			urlObj.setStatistics(stat.generateTimeTaken());
			return ResponseEntity.ok(encurtadorUrlService.shortenUrl(urlObj, stat));
		} catch (JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (MalformedURLException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@RequestMapping(value = { "/retrieveurl" }, method = RequestMethod.GET)
	public Object retrieveUrl(@RequestParam("alias") String alias, HttpServletResponse response) throws IOException {
		// encurtadorUrlService.retrieveUrl(alias)
		String valor = null;
		try {
			valor = encurtadorUrlService.retrieveUrl(alias);
			encurtadorUrlService.validaUrl(valor);
			return new ModelAndView("redirect:" + valor);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			return valor;

		} catch (URISyntaxException e) {
			return valor;
		}
	}
	
	@RequestMapping(value = {"/mostAccessed"}, method = RequestMethod.GET)
	public void urlsMostAccessed(){
		for (Url url : encurtadorUrlService.listUrlMostAccessed()) {
			System.out.println(url.getUrlOriginal());
		}
	}
}
