package br.com.encurtadorurl.service;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.encurtadorurl.dao.EncurtadorUrlDAO;
import br.com.encurtadorurl.model.Url;
import br.com.encurtadorurl.model.UrlBuilder;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EncurtadorUrlServiceImplTest{


	@Autowired
	private EncurtadorUrlDAO encurtadorUrlDAO;
	
	@Autowired
	private EncurtadorUrlService encurtadorUrlService;
	
	@InjectMocks
	private UrlBuilder urlBuilder;
	
	
	@Before
	public void before(){
		initMocks(this);
	}
	
	@Test
	public void findAliasTest() throws MalformedURLException, URISyntaxException{
		encurtadorUrlDAO.save(urlBuilder.withAlias("gl").build());
		assertEquals("gl", encurtadorUrlDAO.findAlias("gl").getAlias());
	}
	
	@Test
	public void findUrlOriginal(){
		encurtadorUrlDAO.save(urlBuilder.withAlias("www.murillo.com").build());
		assertEquals("murillo.com", encurtadorUrlDAO.findUrlOriginal("murillo.com").getUrlOriginal());
	}
	
	@Test
	public void createAliasTest(){
		String result = encurtadorUrlService.encodeUrl(urlBuilder.build().getUrlOriginal());
		assertEquals("b42d6c7e", result);
	}

	@Test(expected = URISyntaxException.class)
	public void validaExceptionUriTest() throws MalformedURLException, URISyntaxException{
		encurtadorUrlService.validaUrl(";.ç=)#!%$");
	}
	
	@Test
	public void shortenUrlWhenExistsAliasTest() throws URISyntaxException, IOException{
		encurtadorUrlDAO.save(urlBuilder.withDefaultUrlObj().build());
		String urlJson = encurtadorUrlService.shortenUrl(urlBuilder.withDefaultUrlObj().build());
		assertEquals("001", urlBuilder.jsonToUrl(urlJson).getErrCode());
	}
	
	@Test
	public void shortenUrlWithoutAliasTest() throws URISyntaxException, IOException{
		String urlJson = encurtadorUrlService.shortenUrl(urlBuilder.withUrlOriginal("murillo.com").build());
		assertEquals(encurtadorUrlService.encodeUrl("murillo.com"), urlBuilder.jsonToUrl(urlJson).getAlias());
	}

	@Test
	public void shortenUrlWithoutAliasWhenExistsUrlOriginalTest() throws URISyntaxException, IOException{
		Url url = encurtadorUrlDAO.save(urlBuilder.withUrlOriginal("murillo.com").build());
		String urlJson = encurtadorUrlService.shortenUrl(urlBuilder.withUrlOriginal("murillo.com").build());
		assertEquals(urlBuilder.urlToJson(url), urlJson);
	}
	
	@Test
	public void shortenUrlWithAliasTest() throws URISyntaxException, IOException{
		String alias = "mu";
		String urlJson = encurtadorUrlService.shortenUrl(urlBuilder.withUrlOriginal("murillo.com").withAlias(alias).build());
		assertEquals(alias, urlBuilder.jsonToUrl(urlJson).getAlias());
	}
	
	@Test
	public void listUrlMostAccessedTest(){
		List<Url> urlsMostAccessed = urlBuilder.withUrlsMostAccessed();
		
		List<Url> urlsLessAccessed = urlBuilder.withUrlsLessAccessed();
		
		encurtadorUrlDAO.save(urlsMostAccessed);
		encurtadorUrlDAO.save(urlsLessAccessed);
		
		assertEquals(urlsMostAccessed, encurtadorUrlService.listUrlMostAccessed());

	}
	
	@Test
	public void retrieveUrlTest() throws URISyntaxException, IOException{
		String urlJson = encurtadorUrlService.retrieveUrl("bemobi");
		assertEquals("002", urlBuilder.jsonToUrl(urlJson).getErrCode());
	}
	
	@Test
	public void retrieveUrlExistsTest() throws URISyntaxException, IOException{
		String urlExpected = "http://bemobi.com.br" ;
		encurtadorUrlDAO.save(urlBuilder.withAlias("bemobi").withAccess(0).withUrlOriginal("http://www.bemobi.com.br").build());
		String urlResponse = encurtadorUrlService.retrieveUrl("bemobi");
		assertEquals(urlExpected, urlResponse);
	}
	
	@Test
	public void retrieveUrlAccessTest() throws JsonProcessingException, MalformedURLException, URISyntaxException{
		encurtadorUrlDAO.save(urlBuilder.withAlias("bemobi").withAccess(0).withUrlOriginal("http://www.bemobi.com.br").build());
		encurtadorUrlService.retrieveUrl("bemobi");
		encurtadorUrlService.retrieveUrl("bemobi");
		
		MatcherAssert.assertThat(2, is(encurtadorUrlDAO.findAlias("bemobi").getAccess()));

	}
	
	
	@Test
	public void validaUrlTest() throws JsonProcessingException, MalformedURLException, URISyntaxException{
		String url = urlBuilder.withUrlOriginal("http://bemobi.com.br").build().getUrlOriginal();
		assertEquals("http://bemobi.com.br", encurtadorUrlService.validaUrl(url));
	}

}