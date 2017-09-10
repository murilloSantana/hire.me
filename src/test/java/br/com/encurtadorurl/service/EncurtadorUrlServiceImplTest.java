package br.com.encurtadorurl.service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

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
	
}
