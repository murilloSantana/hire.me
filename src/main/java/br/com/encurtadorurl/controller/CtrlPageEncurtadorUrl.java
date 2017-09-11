package br.com.encurtadorurl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.encurtadorurl.model.Url;

@Controller
public class CtrlPageEncurtadorUrl {

	@RequestMapping(value = {"/encurtador"}, method = RequestMethod.GET )
	public String pageEncurtador(Model model){
		model.addAttribute("url", new Url());
		return "encurtador";
	}
}
