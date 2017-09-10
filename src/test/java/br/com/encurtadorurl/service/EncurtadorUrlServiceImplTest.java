package br.com.encurtadorurl.service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.encurtadorurl.dao.EncurtadorUrlDAO;
import br.com.encurtadorurl.model.Url;

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
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void findAliasTest() throws MalformedURLException, URISyntaxException{
		encurtadorUrlDAO.save(urlBuilder.withAlias("gl").build());
		Assert.assertEquals("gl", encurtadorUrlDAO.findAlias("gl").getAlias());
	}
	@Test
	public void createAliasTest(){
		String result = encurtadorUrlService.encodeUrl(urlBuilder.build().getUrlOriginal());
		Assert.assertEquals("b42d6c7e", result);
	}
	
	@Test
	public void addPrefixAliasTest() throws MalformedURLException, URISyntaxException{
		Assert.assertEquals("http://mu.me/murillo", encurtadorUrlService.addPrefixAlias("murillo"));
	}
	
	@Test(expected = URISyntaxException.class)
	public void validaExceptionUriTest() throws MalformedURLException, URISyntaxException{
		encurtadorUrlService.validaUrl(";.ç=)#!%$");
	}
	
	@Test
	public void shortenUrlWhenExistsAliasTest() throws URISyntaxException, IOException{
		encurtadorUrlDAO.save(urlBuilder.withDefaultUrlObj().build());
		String urlJson = encurtadorUrlService.shortenUrl(urlBuilder.withDefaultUrlObj().build(), null);
		Assert.assertEquals("001", urlBuilder.jsonToUrl(urlJson).getErrCode());
	}
	
	@Test
	public void listUrlMostAccessedTest(){
		List<Url> urlsMostAccessed = Arrays.asList(
				urlBuilder.withUrlOriginal("teste13.com").withAccess(14).build(),
				urlBuilder.withUrlOriginal("teste1.com").withAccess(13).build(),
				urlBuilder.withUrlOriginal("teste2.com").withAccess(12).build(),
				urlBuilder.withUrlOriginal("teste3.com").withAccess(11).build(),
				urlBuilder.withUrlOriginal("teste4.com").withAccess(10).build(),
				urlBuilder.withUrlOriginal("teste5.com").withAccess(9).build(),
				urlBuilder.withUrlOriginal("teste6.com").withAccess(8).build(),
				urlBuilder.withUrlOriginal("teste7.com").withAccess(7).build(),
				urlBuilder.withUrlOriginal("teste8.com").withAccess(6).build(),
				urlBuilder.withUrlOriginal("teste9.com").withAccess(5).build());
		
		List<Url> urlsLessAccessed = Arrays.asList(
				urlBuilder.withUrlOriginal("teste10.com").withAccess(4).build(),
				urlBuilder.withUrlOriginal("teste11.com").withAccess(3).build(),
				urlBuilder.withUrlOriginal("teste12.com").withAccess(2).build());
		
		encurtadorUrlDAO.save(urlsMostAccessed);
		encurtadorUrlDAO.save(urlsLessAccessed);
		
		Assert.assertEquals(urlsMostAccessed, encurtadorUrlDAO.findUrlsMostAccessed());
		

	}
}
