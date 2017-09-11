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
import br.com.encurtadorurl.model.Url;

/** Serviço responsável por tratar as Urls e alias passados pelo usuário
 * @author Murillo Santana
 * @version 1.0.0
 */

@Service("encurtadorService")
public class EncurtadorUrlServiceImpl implements EncurtadorUrlService{

	private ObjectMapper mapper;
	
	@Autowired
	private EncurtadorUrlDAO encurtadorUrlDAO;
	
	private Url url;
	
	/*
	 * Método responsável por encurtar a url
	 * A função suporta 4 comportamentos diferentes de acordo com a url e o alias informados.
	 * São tratadas urls com alias não informado mas já existente no banco de dados, url sem
	 * alias porém já existente e também não existente e por último requisições apenas com a url.
	 */
	@Override
	public String shortenUrl(Url urlObj) throws JsonProcessingException, MalformedURLException, URISyntaxException {
		mapper = new ObjectMapper();
		
		if(urlObj.getUrlOriginal().trim() == "")
			return null;
		
		if(encurtadorUrlDAO.findAlias(urlObj.getAlias()) != null){
			Url urlError = new Url();
			urlError.setAlias(urlObj.getAlias());
			urlError.setErrCode("001");
			urlError.setDescription("CUSTOM ALIAS ALREADY EXISTS");
				
			return mapper.writeValueAsString(urlError);
		}
		Url urlOld;
		if((urlOld = encurtadorUrlDAO.findUrlOriginal(urlObj.getUrlOriginal())) != null && (urlObj.getAlias() == null || urlObj.getAlias().trim() == "")){
			urlOld.setStatistics(urlObj.getStatistics());
			
			return mapper.writeValueAsString(urlOld);
		}else{
			
			urlObj.setUrlOriginal(validaUrl(urlObj.getUrlOriginal()));
			urlObj.setAlias(urlObj.getAlias() == null || urlObj.getAlias().trim() == "" ? encodeUrl(urlObj.getUrlOriginal()) : urlObj.getAlias());
			urlObj.setAccess(0);
			
			encurtadorUrlDAO.save(urlObj);
			
			return mapper.writeValueAsString(urlObj);
			
		}
		
	}
	
	/*
	 * Método responsável por retornar uma url e redirecionar o usuario para a mesma
	 * A função recebe como parâmetro um alias passado pelo client, retornando um erro quando não encontrado
	 * no banco de dados e retornando a url para que o redirecionamento seja feito, além de acrescentar o 
	 * contador de acessos
	 */
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
	
	//Realiza uma pesquisa no Banco de dados pelas 10 urls mais acessadas
	@Override
	public List<Url> listUrlMostAccessed(){
		return encurtadorUrlDAO.findUrlsMostAccessed();
	}
	
	//Encurta a URL original passada pelo usuario usando o murmur3 (funcao hash nao criptografica)
	// de 32 bits, para ter um alias de tamanho fixo não grande
	@Override
	public String encodeUrl(String urlOriginal){
		return Hashing.murmur3_32().hashString(urlOriginal, StandardCharsets.UTF_8).toString();
	}
	
	//Verifica se a url é válida, e adiciona o protocolo http no caso da url vir sem
	@Override
	public String validaUrl(String urlOriginal) throws MalformedURLException, URISyntaxException{
		URI uri = new URI(urlOriginal);
        URL url = null;

		if(!uri.isAbsolute())
			url = new URL("http", urlOriginal, "");
		else
			url = uri.toURL();
		
		return url.toString();
	}
}
