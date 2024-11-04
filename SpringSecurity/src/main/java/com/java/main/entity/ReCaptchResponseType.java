package com.java.main.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReCaptchResponseType {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTimestamp;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("score") // For reCAPTCHA v3, you may want to handle this
    private Double score;

    @JsonProperty("action") // For reCAPTCHA v3, you may want to handle this
    private String action;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getChallengeTimestamp() {
        return challengeTimestamp;
    }

    public void setChallengeTimestamp(String challengeTimestamp) {
        this.challengeTimestamp = challengeTimestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
