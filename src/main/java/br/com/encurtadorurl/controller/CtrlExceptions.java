package br.com.encurtadorurl.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;

/** Controller que trata genericamente as possiveis exceções da api
 * @author Murillo Santana
 * @version 1.0.0
 */
@ControllerAdvice
public class CtrlExceptions {

	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<?> uriSyntaxException(){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<?> jsonProcessingException(){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@ExceptionHandler(MalformedURLException.class)
	public ResponseEntity<?> malformedURLException(){
		System.out.println("entrou");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<?> ioException(){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
}
