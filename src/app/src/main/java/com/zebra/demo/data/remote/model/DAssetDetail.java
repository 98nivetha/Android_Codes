package com.zebra.demo.data.remote.model;

import androidx.annotation.NonNull;

public class DAssetDetail {
    public String SapAssetName;
    public String SapAssetNo;

    @NonNull
    @Override
    public String toString() {
        return SapAssetName == null ? "" : SapAssetName;
    }
}
