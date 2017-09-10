package br.com.encurtadorurl.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Component
public class Statistics {

	@JsonProperty("time_taken")
	private String timeTaken;

	@JsonProperty("ip_user")
	private String ipUser;

	@JsonIgnore
	private long startRequest;

	public String getIpUser() {
		return ipUser;
	}

	public void setIpUser(String ipUser) {
		this.ipUser = ipUser;
	}

	public long getStartRequest() {
		return startRequest;
	}

	public void setStartRequest(long startRequest) {
		this.startRequest = startRequest;
	}

	public Statistics generateTimeTaken() {
		StringBuffer buffer = new StringBuffer();
		timeTaken = buffer.append(System.currentTimeMillis() - this.startRequest).append("ms").toString();
		return this;				
	}
}
