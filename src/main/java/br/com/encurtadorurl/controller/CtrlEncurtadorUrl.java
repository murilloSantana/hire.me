package br.com.encurtadorurl.controller;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.encurtadorurl.model.Statistics;
import br.com.encurtadorurl.model.Url;
import br.com.encurtadorurl.service.EncurtadorUrlService;

/** Controlador responsável por tratar as requisições da API
 * @author Murillo Santana
 * @version 1.0.0
 */

@RestController
public class CtrlEncurtadorUrl {

	@Autowired
	private Statistics stat;
	@Autowired
	private EncurtadorUrlService encurtadorUrlService;

	//Método responsável por retornar um JSON com a url encurtada
	@RequestMapping(value = { "/shortenurl" }, method = RequestMethod.GET)
	public ResponseEntity<?> shortenUrl(@ModelAttribute Url urlObj, HttpServletRequest request)
			throws JsonProcessingException, MalformedURLException, URISyntaxException {
		stat.setStartRequest(Long.parseLong(request.getAttribute("startRequest").toString()));
		stat.setIpUser(request.getRemoteAddr());

		urlObj.setStatistics(stat.generateTimeTaken());
				
		return ResponseEntity.ok(encurtadorUrlService.shortenUrl(urlObj));
	}
	
	/* Retorna a url relacionada ao alias passado como parâmetro
	 * o método possui dois retornos possiveis, o primeiro é um ModelAndView que deve redirecionar 
	 * a page, o segundo é um JSON com dados informando um erro na busca.
	 */
	@RequestMapping(value = { "/retrieveurl" }, method = RequestMethod.GET)
	public Object retrieveUrl(@RequestParam("alias") String alias) {
		String valor = null;
		try {
			valor = encurtadorUrlService.retrieveUrl(alias);
			encurtadorUrlService.validaUrl(valor);
			return new ModelAndView("redirect:" + valor);

		} catch (URISyntaxException e) {
			return valor;
		} catch (JsonProcessingException e) {
			return valor;
		} catch (MalformedURLException e) {
			return valor;
		}
	}

	// Retorna os 10 alias mais acessados
	@RequestMapping(value = { "/mostAccessed" }, method = RequestMethod.GET)
	public ResponseEntity<?> urlsMostAccessed() {
		return ResponseEntity.ok(encurtadorUrlService.listUrlMostAccessed());
	}
}
