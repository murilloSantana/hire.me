package br.com.encurtadorurl.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.encurtadorurl.model.Url;

public interface EncurtadorUrlService {

	String shortenUrl(Url url) throws JsonProcessingException, MalformedURLException, URISyntaxException;
	
	String encodeUrl(String urlOriginal);
	
	String validaUrl(String urlOriginal) throws URISyntaxException, MalformedURLException;

	String retrieveUrl(String alias) throws JsonProcessingException, MalformedURLException, URISyntaxException;

	List<Url> listUrlMostAccessed();
}
