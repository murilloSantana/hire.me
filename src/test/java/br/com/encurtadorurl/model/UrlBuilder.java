package br.com.encurtadorurl.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UrlBuilder {

	private String urlOriginal = "www.murillo.com";
	
	private String alias;
	
	private ObjectMapper mapper;
	
	private Integer access;
	
	
	private UrlBuilder(){}
	
	public Url build(){
		Url url = new Url();
		url.setUrlOriginal(urlOriginal);
		url.setAlias(alias);
		url.setAccess(access);
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
	
	public UrlBuilder withAccess(Integer access){
		this.access = access;
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
	
	public List<Url> listJsonToListUrl(String json) throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<List<Url>>(){});
	}
	
	public String urlToJson(Url url) throws JsonProcessingException{
		mapper = new ObjectMapper();
		return mapper.writeValueAsString(url);
	}
	
	public List<Url> withUrlsMostAccessed(){
		return Arrays.asList(
				this.withUrlOriginal("teste13.com").withAccess(14).build(),
				this.withUrlOriginal("teste1.com").withAccess(13).build(),
				this.withUrlOriginal("teste2.com").withAccess(12).build(),
				this.withUrlOriginal("teste3.com").withAccess(11).build(),
				this.withUrlOriginal("teste4.com").withAccess(10).build(),
				this.withUrlOriginal("teste5.com").withAccess(9).build(),
				this.withUrlOriginal("teste6.com").withAccess(8).build(),
				this.withUrlOriginal("teste7.com").withAccess(7).build(),
				this.withUrlOriginal("teste8.com").withAccess(6).build(),
				this.withUrlOriginal("teste9.com").withAccess(5).build());
		
	}
	
	public List<Url> withUrlsLessAccessed(){
		return Arrays.asList(
				this.withUrlOriginal("teste10.com").withAccess(4).build(),
				this.withUrlOriginal("teste11.com").withAccess(3).build(),
				this.withUrlOriginal("teste12.com").withAccess(2).build());
	}
}
