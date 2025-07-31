package com.zebra.demo.data.remote.responsemodels;

import java.util.List;

public class GetAllRackDatasResponse {
    public Boolean Result;
    public String ErrorCode;
    public String Message;
    public Data Data;

    public static class Data {
        public List<RackDetails> D_RackDetails;
    }
}
