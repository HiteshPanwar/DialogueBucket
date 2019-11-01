package com.dialogueapp.dialoguebucket.dialoguesharingapp.models;

/**
 * Created by HITESH on 8/31/2017.
 */
public class Dialogue {

    public String movieName;
    public String dialoguePoster;
    public String dialogueName;
    public String dialogueId;
    public String storageLinker;



    public Dialogue(){

    }
    public Dialogue(String movieName,String dialoguePoster,String dialogueName,String dialogueId,String storageLinker){
        this.storageLinker=storageLinker;
        this.movieName = movieName;
        this.dialoguePoster = dialoguePoster;
        this.dialogueName = dialogueName;
        this.dialogueId = dialogueId;

    }

    public String getStorageLinker() {
        return storageLinker;
    }

    public void setStorageLinker(String storageLinker) {
        this.storageLinker = storageLinker;
    }

    public String getDialogueId() {
        return dialogueId;
    }

    public void setDialogueId(String dialogueId) {
        this.dialogueId = dialogueId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDialoguePoster() {
        return dialoguePoster;
    }

    public void setDialoguePoster(String moviePoster) {
        this.dialoguePoster = moviePoster;
    }

    public String getDialogueName() {
        return dialogueName;
    }

    public void setDialogueName(String dialogueName) {
        this.dialogueName = dialogueName;
    }


}
