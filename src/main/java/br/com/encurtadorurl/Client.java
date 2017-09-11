package br.com.encurtadorurl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.encurtadorurl.model.Url;

/** Client responsável por consumir o serviço fornecido pelo encurtador de url
 * @author Murillo Santana
 * @version 1.0.0
 */

public class Client {

	private static String urlMostAccessed = "http://localhost:8080/mostAccessed";

	private static String urlRetrieve = "http://localhost:8080/retrieveurl";

	private static String urlShorten = "http://localhost:8080/shortenurl";

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		urlsMostAccessed();
		retrieveUrl();
		shortenUrlWithAlias();
		shortenUrlWithoutAlias();
	}

	public static ResponseEntity<String> generateRestTemplate(String uri) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
	}

	public static void shortenUrlWithoutAlias() {
		final String uri = urlShorten;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("urlOriginal", "globo.com");
		
		ResponseEntity<String> result = generateRestTemplate(builder.build().encode().toString());
		
		System.out.println(result.getBody());
	}

	public static void shortenUrlWithAlias() {
		final String uri = urlShorten;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
				.queryParam("urlOriginal", "murillo.com")
				.queryParam("alias", "teste2");
		
		ResponseEntity<String> result = generateRestTemplate(builder.build().encode().toString());
		
		System.out.println(result.getBody());
	}

	public static void retrieveUrl() {
		final String uri = urlRetrieve;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("alias", "teste");

		ResponseEntity<String> result = generateRestTemplate(builder.build().encode().toString());

		System.out.println(result.getBody());
	}

	public static void urlsMostAccessed() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		final String uri = urlMostAccessed;

		ResponseEntity<String> result = generateRestTemplate(uri);

		List<Url> urls = mapper.readValue(result.getBody(), new TypeReference<List<Url>>() {});
		for (Url url : urls) {
			System.out.println(url.getUrlOriginal());
		}
	}
}
