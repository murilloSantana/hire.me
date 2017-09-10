package br.com.encurtadorurl.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonInclude(Include.NON_NULL)
public class Url {
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long urlId;
	
	@JsonProperty("url_original")
	private String urlOriginal;
	
	private String alias;
	
	@JsonIgnore
	private Integer access;
	
	@Transient
	@JsonProperty("err_code")
	private String errCode;

	@Transient
	private String description;
	
	@Transient
	private Statistics statistics;
	
	public Long getUrlId() {
		return urlId;
	}

	public void setUrlId(Long urlId) {
		this.urlId = urlId;
	}

	public String getUrlOriginal() {
		return urlOriginal;
	}

	// tratando a url para padronizacao, o www é removido para tratar requisicoes com ou sem www da mesma maneira
	public void setUrlOriginal(String urlOriginal) {
		urlOriginal = urlOriginal.replaceFirst(".*://", "");

		if(urlOriginal.startsWith("www."))
			urlOriginal = urlOriginal.replaceFirst("www.", "");
		
		this.urlOriginal = urlOriginal;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}

	
}
