package com.jpataide.project.data;

import com.jpataide.project.adapter.VolumeInfo;

/**
 * Created by jpataide on 8/17/15.
 */
public class Item {
    private VolumeInfo volumeInfo;
    private String selfLink;

    public String getSelfLink() {
        return selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
}
