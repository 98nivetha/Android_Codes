package com.zebra.demo.data.remote.responsemodels;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class RackAndMaterialWrapper implements Serializable {
    @SerializedName("V_RackDetails")
    public RackDetails rackDetails;

    @SerializedName("V_MaterialDetails")
    public List<MaterialDetail> materialDetails;
}

