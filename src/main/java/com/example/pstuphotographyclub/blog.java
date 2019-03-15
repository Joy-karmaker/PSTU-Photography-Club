package com.example.pstuphotographyclub;

/**
 * Created by JOY KARMAKER on 20-Aug-17.
 */
public class blog {
    private String title, caption, image;

    public blog() {

    }

    public blog(String title, String caption, String image) {
        this.title = title;
        this.caption = caption;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
