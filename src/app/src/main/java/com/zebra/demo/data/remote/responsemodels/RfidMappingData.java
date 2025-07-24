package com.zebra.demo.data.remote.responsemodels;
import java.util.List;

public class RfidMappingData {
    private List<InwardDetail> D_InwardDetails;
    private List<ModelDetail> D_ModelDetails;
    private List<MaterialDetail> D_MaterialDetails;
    private List<RfidDetail> GD_RfidDetails;

    public List<InwardDetail> getD_InwardDetails() {
        return D_InwardDetails;
    }

    public void setD_InwardDetails(List<InwardDetail> d_InwardDetails) {
        D_InwardDetails = d_InwardDetails;
    }

    public List<ModelDetail> getD_ModelDetails() {
        return D_ModelDetails;
    }

    public void setD_ModelDetails(List<ModelDetail> d_ModelDetails) {
        D_ModelDetails = d_ModelDetails;
    }

    public List<MaterialDetail> getD_MaterialDetails() {
        return D_MaterialDetails;
    }

    public void setD_MaterialDetails(List<MaterialDetail> d_MaterialDetails) {
        D_MaterialDetails = d_MaterialDetails;
    }

    public List<RfidDetail> getGD_RfidDetails() {
        return GD_RfidDetails;
    }

    public void setGD_RfidDetails(List<RfidDetail> gD_RfidDetails) {
        GD_RfidDetails = gD_RfidDetails;
    }
}
