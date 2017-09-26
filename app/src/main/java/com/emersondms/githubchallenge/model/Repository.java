package com.emersondms.githubchallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Repository {

    @SerializedName("name") @Expose
    private String name;

    @SerializedName("description") @Expose
    private String description;

    @SerializedName("forks") @Expose
    private int forks;

    @SerializedName("stargazers_count") @Expose
    private int stars;

    @SerializedName("owner") @Expose
    private User user;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getForks() {
        return forks;
    }
    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
