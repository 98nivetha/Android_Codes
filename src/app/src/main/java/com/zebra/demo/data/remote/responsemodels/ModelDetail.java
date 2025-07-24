package com.zebra.demo.data.remote.responsemodels;

public class ModelDetail {
    private int Modelid;
    private String Modelcode;
    private String Modelname;
    private int Status;

    public int getModelid() {
        return Modelid;
    }

    public void setModelid(int modelid) {
        Modelid = modelid;
    }

    public String getModelcode() {
        return Modelcode;
    }

    public void setModelcode(String modelcode) {
        Modelcode = modelcode;
    }

    public String getModelname() {
        return Modelname;
    }

    public void setModelname(String modelname) {
        Modelname = modelname;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}

