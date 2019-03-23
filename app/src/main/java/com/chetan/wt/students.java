package com.chetan.wt;

public class students {

    String id;
    String name;
    String mail;
    String qualification;
    String city;
    String durl;

    public students() {
    }

    public students(String id, String name, String email, String degree, String city, String durl) {
        this.id = id;
        this.name = name;
        this.mail = email;
        this.qualification = degree;
        this.city = city;
        this.durl = durl;
    }
    public students(String id, String name, String email, String degree, String city) {
        this.id = id;
        this.name = name;
        this.mail = email;
        this.qualification = degree;
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
        return mail;
    }

    public String getDegree() {
        return qualification;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

}
