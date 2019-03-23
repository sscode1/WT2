package com.chetan.wt;

public class feedback_user {
    private String email;
    private Float rating;
    private String feedback;
    private String user;
    private feedback_user() {
    }

    public feedback_user(String email, String user, Float rating, String feedback) {
        this.email = email;
        this.rating = rating;
        this.feedback = feedback;
        this.user=user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
