package com.azzdorfrobotics.android.legstep.model;

/**
 * Created on 26.01.2016
 *
 * @author Mykola Tychyna (imykolapro)
 */
public class OptionMenuItem {

    public String title;
    public int itemId;
    public boolean langItem;
    public int imageId;

    public OptionMenuItem(String title, int itemId, boolean langItem, int imageId) {
        this.title = title;
        this.itemId = itemId;
        this.langItem = langItem;
        this.imageId = imageId;
    }

    public OptionMenuItem(String title, int itemId) {
        this.title = title;
        this.itemId = itemId;
        this.langItem = false;
        this.imageId = -1;
    }
}
