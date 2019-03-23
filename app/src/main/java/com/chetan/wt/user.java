package com.chetan.wt;

public class user {

    String id;
    String name;
    String email;
    String degree;
    String city;
    String durl;
    public user() {
    }
    public user(String id, String name, String email, String degree, String city, String durl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.degree = degree;
        this.city = city;
        this.durl = durl;
    }
    public user(String id, String name, String email, String degree, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.degree = degree;
        this.city = city;
        this.durl="https://i.imgur.com/tGbaZCY.jpg";
    }



    public String getDurl() {
        return durl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDegree() {
        return degree;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }
}
