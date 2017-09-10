package br.com.encurtadorurl.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.encurtadorurl.model.Url;

public class UrlBuilder {

	private String urlOriginal = "www.murillo.com";
	
	private String alias;
	
	private ObjectMapper mapper;
	
	private UrlBuilder(){}
	
	public Url build(){
		Url url = new Url();
		url.setUrlOriginal(urlOriginal);
		url.setAlias(alias);
		return url;
	}
	
	public UrlBuilder withDefaultUrlObj(){
		this.urlOriginal = "www.globo.com";
		this.alias = "80e56bbc";
		return this;
	}
	
	public UrlBuilder withUrlOriginal(String url){
		this.urlOriginal = url;
		return this;
	}
	
	public UrlBuilder withAlias(String alias){
		this.alias = alias;
		return this;
	}
	
	public Url jsonToUrl(String json) throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		return mapper.readValue(json, Url.class);
	}
}
