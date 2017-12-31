package com.developer.fabian.feedrss.entities;

public class Enter {
    private int idImage;
    private String textUp;
    private String textDown;
    private String urlRSS;

    public Enter(int idImage, String textUp, String textDown, String urlRSS) {
        this.idImage = idImage;
        this.textUp = textUp;
        this.textDown = textDown;
        this.urlRSS = urlRSS;
    }

    public String getTextUp() {
        return textUp;
    }

    public String getTextDown() {
        return textDown;
    }

    public int getIdImage() {
        return idImage;
    }

    public String getUrlRSS() {
        return urlRSS;
    }
}
