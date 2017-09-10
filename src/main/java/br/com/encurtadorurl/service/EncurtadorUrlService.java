package br.com.encurtadorurl.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.encurtadorurl.model.Statistics;
import br.com.encurtadorurl.model.Url;

public interface EncurtadorUrlService {

	String shortenUrl(Url url, Statistics stat) throws JsonProcessingException, MalformedURLException, URISyntaxException;
	
	String encodeUrl(String urlOriginal);
	
	String validaUrl(String urlOriginal) throws URISyntaxException, MalformedURLException;

	String addPrefixAlias(String alias) throws MalformedURLException, URISyntaxException;

	String retrieveUrl(String alias) throws JsonProcessingException, MalformedURLException, URISyntaxException;

	List<Url> listUrlMostAccessed();
}
