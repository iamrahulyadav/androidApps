package com.mehndidesign.offline2018.helper;

import android.widget.ImageView;

/**
 * Created by ABC on 5/24/2018.
 */

public class ExploreDesigns {

    private int designBackgroundImage;
    private String designType;

    public ExploreDesigns(int designBackgroundImage, String designType) {
        this.designBackgroundImage = designBackgroundImage;
        this.designType = designType;
    }

    public int getDesignBackgroundImage() {
        return designBackgroundImage;
    }

    public void setDesignBackgroundImage(int designBackgroundImage) {
        this.designBackgroundImage = designBackgroundImage;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }
}
