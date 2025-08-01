package com.zebra.demo.data.remote.responsemodels;

public class DisplayItem {
    private PutAwayScanningResponse.MaterialItem materialItem;
    private boolean isValid;

    public DisplayItem(PutAwayScanningResponse.MaterialItem materialItem, boolean isValid) {
        this.materialItem = materialItem;
        this.isValid = isValid;
    }

    public PutAwayScanningResponse.MaterialItem getMaterialItem() {
        return materialItem;
    }

    public boolean isValid() {
        return isValid;
    }
}
