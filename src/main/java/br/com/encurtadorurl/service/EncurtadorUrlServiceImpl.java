package br.com.encurtadorurl.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import br.com.encurtadorurl.dao.EncurtadorUrlDAO;
import br.com.encurtadorurl.model.Statistics;
import br.com.encurtadorurl.model.Url;

@Service("encurtadorService")
public class EncurtadorUrlServiceImpl implements EncurtadorUrlService{

	private ObjectMapper mapper;
	
	@Autowired
	private EncurtadorUrlDAO encurtadorUrlDAO;
	
	private Url url;
	
	@Override
	public String shortenUrl(Url urlObj, Statistics stat) throws JsonProcessingException, MalformedURLException, URISyntaxException {
		mapper = new ObjectMapper();
		
		if(urlObj.getAlias() == null){
			//Encurtando a URL original passada pelo usuario usando o murmur3 (funcao hash nao criptografica)
			// de 32 bits, para ter um alias de tamanho fixo não grande
			urlObj.setUrlOriginal(validaUrl(urlObj.getUrlOriginal()));
			urlObj.setAlias(encodeUrl(urlObj.getUrlOriginal()));
			urlObj.setAccess(0);
			
			if(encurtadorUrlDAO.findUrlOriginal(urlObj.getUrlOriginal()) != null)
				return mapper.writeValueAsString(urlObj);
		}
		
		if(encurtadorUrlDAO.findAlias(urlObj.getAlias()) != null || encurtadorUrlDAO.findAlias(addPrefixAlias(urlObj.getAlias())) != null){
			Url urlError = new Url();
			urlError.setAlias(urlObj.getAlias());
			urlError.setErrCode("001");
			urlError.setDescription("CUSTOM ALIAS ALREADY EXISTS");
				
			return mapper.writeValueAsString(urlError);
		}
		
		encurtadorUrlDAO.save(urlObj);
		
		return mapper.writeValueAsString(urlObj);
	}
	
	@Override
	public String retrieveUrl(String alias) throws JsonProcessingException, MalformedURLException, URISyntaxException{
		url = encurtadorUrlDAO.findAlias(alias);
		mapper = new ObjectMapper();
		
		if(url == null){
			Url urlError = new Url();
			urlError.setErrCode("002");
			urlError.setDescription("SHORTENED URL NOT FOUND");
			urlError.setAlias(alias);
			
			return mapper.writeValueAsString(urlError);
		}
		
		url.setAccess(url.getAccess()+1);
		
		encurtadorUrlDAO.save(url);
		
		return validaUrl(url.getUrlOriginal());
	}
	
	@Override
	public List<Url> listUrlMostAccessed(){
		return encurtadorUrlDAO.findUrlsMostAccessed();
	}
	
	@Override
	public String addPrefixAlias(String alias) throws MalformedURLException, URISyntaxException{
		StringBuffer buffer = new StringBuffer();
		buffer.append("mu.me/");
		buffer.append(alias);
		
		return validaUrl(buffer.toString());
	}
	
	@Override
	public String encodeUrl(String urlOriginal){
		return Hashing.murmur3_32().hashString(urlOriginal, StandardCharsets.UTF_8).toString();
	}
	
	@Override
	public String validaUrl(String urlOriginal) throws URISyntaxException, MalformedURLException{
		URI uri = new URI(urlOriginal);
        URL url = null;

		if(!uri.isAbsolute())
			url = new URL("http", urlOriginal, "");
		else
			url = uri.toURL();
		
		return url.toString();
	}
}
