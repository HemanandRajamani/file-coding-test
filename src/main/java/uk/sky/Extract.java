package uk.sky;

import lombok.Data;

/**
 * @author hemanand.rajamani
 * created on 26/11/20
 */
public class Extract {
	public Long timeStamp;
	public String country;
	public Double responseTime;

	public Extract (Long timeStamp, String country, Double responseTime) {
		this.timeStamp = timeStamp;
		this.country = country;
		this.responseTime = responseTime;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setResponseTime(Double responseTime) {
		this.responseTime = responseTime;
	}

	public Double getResponseTime() {
		return responseTime;
	}

}
