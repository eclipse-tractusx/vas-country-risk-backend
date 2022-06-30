package com.catenax.valueaddedservice.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link react.domain.DataSource} entity.
 */
public class DashBoardDto implements Serializable {

    private Long id;

    private String bpn;

    private String legalName;

    private String address;

    private String city;

    private String country;

    private Float score;

    private String rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBpn() {
        return bpn;
    }

    public void setBpn(String bpn) {
        this.bpn = bpn;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
