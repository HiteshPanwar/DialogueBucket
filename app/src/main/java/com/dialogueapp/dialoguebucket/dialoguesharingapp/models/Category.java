package com.dialogueapp.dialoguebucket.dialoguesharingapp.models;

/**
 * Created by HITESH on 9/6/2017.
 */

public class Category {


    public String categoryName;
    public String categoryPoster;
    public String categoryId;
    public String categoryLinker;

    public String getCategoryLinker() {
        return categoryLinker;
    }

    public void setCategoryLinker(String categoryLinker) {
        this.categoryLinker = categoryLinker;
    }

    public Category(String movieName, String dialoguePoster, String categoryId,String categoryLinker){
        this.categoryLinker=categoryLinker;
        this.categoryName = movieName;
        this.categoryPoster = dialoguePoster;
        this.categoryId = categoryId;


    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryPoster() {
        return categoryPoster;
    }

    public void setCategoryPoster(String categoryPoster) {
        this.categoryPoster = categoryPoster;
    }

}
