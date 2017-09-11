package br.com.encurtadorurl.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.encurtadorurl.Application;
import br.com.encurtadorurl.dao.EncurtadorUrlDAO;
import br.com.encurtadorurl.model.Url;
import br.com.encurtadorurl.model.UrlBuilder;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CtrlEncurtadorUrlTest {
	
	@Autowired
	private MockMvc mock;
	
	@InjectMocks
	private UrlBuilder urlBuilder;
	
	@Autowired
	private EncurtadorUrlDAO encurtadorUrlDAO;
	
	@Test
	public void accessShortenUrl() throws Exception{
		ResultActions result = mock.perform(MockMvcRequestBuilders.get("/shortenurl?urlOriginal=globo.com"));
		Url url = urlBuilder.jsonToUrl(result.andReturn().getResponse().getContentAsString());
		
		assertEquals("80e56bbc", url.getAlias());
	}
	
	@Test
	public void accessRetrieveUrl() throws Exception{
		encurtadorUrlDAO.save(urlBuilder.withAlias("globo").withUrlOriginal("globo.com").build());	
		
		ResultActions result = mock.perform(MockMvcRequestBuilders.get("/retrieveurl?alias=errado"));
		Url url = urlBuilder.jsonToUrl(result.andReturn().getResponse().getContentAsString());

		assertEquals("SHORTENED URL NOT FOUND", url.getDescription());
	}
	
}
