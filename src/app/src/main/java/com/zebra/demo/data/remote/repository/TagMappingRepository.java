package com.zebra.demo.data.remote.repository;

import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.ApiResponse;
import com.zebra.demo.data.remote.model.AssetDetail;
import com.zebra.demo.data.remote.model.AssetHeader;
import com.zebra.demo.data.remote.model.FilterAssetNameRequest;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.GetAllAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.InsertAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.InsertAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.RFIDTagMappingRequest;
import com.zebra.demo.data.remote.model.UpdateAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.UpdateAssetRFIDMappingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagMappingRepository extends BaseApiService {
    private static TagMappingRepository metaDetailRepository;

    public static TagMappingRepository getInstance() {
        if (metaDetailRepository == null) {
            metaDetailRepository = new TagMappingRepository();
        }
        return metaDetailRepository;
    }

    //Get All Asset RFID Mapping
    public void getAllAssetRFIDMapping( int Assetrfidmappingid, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {


        GetAllAssetRFIDMappingRequest getAllAssetRFIDMappingRequest = new GetAllAssetRFIDMappingRequest();
        getAllAssetRFIDMappingRequest.Assetrfidmappingid = Assetrfidmappingid;

        Call<FilterAssetRFIDMappingResponse> call = getApiService().getAllAssetRFIDMapping(getAllAssetRFIDMappingRequest);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    //Insert Asset RFID Mapping
    public void insertAssetRFIDMapping(InsertAssetRFIDMappingRequest insertAssetRFIDMappingRequest, ResponseListener<InsertAssetRFIDMappingResponse> responseListener) {
        Call<InsertAssetRFIDMappingResponse> call = getApiService().insertAssetRFIDMapping(insertAssetRFIDMappingRequest);
        call.enqueue(new Callback<InsertAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<InsertAssetRFIDMappingResponse> call, Response<InsertAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<InsertAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    //Update Asset RFID Mapping
    public void updateAssetRFIDMapping(UpdateAssetRFIDMappingRequest updateAssetRFIDMappingRequest, ResponseListener<UpdateAssetRFIDMappingResponse> responseListener) {
        Call<UpdateAssetRFIDMappingResponse> call = getApiService().updateAssetRFIDMapping(updateAssetRFIDMappingRequest);
        call.enqueue(new Callback<UpdateAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<UpdateAssetRFIDMappingResponse> call, Response<UpdateAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }
            @Override
            public void onFailure(Call<UpdateAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }

    //Get Building
    public void fetchBuildingDetailsRequest(String companycode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"Companycode\": \""+ companycode +"\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Building
    public void fetchFloorDetailsRequest(String Buildingcode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"Buildingcode\": \""+ Buildingcode +"\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Location
    public void fetchLocationDetailsRequest(String Floorcode, String Buildingcode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"Floorcode\": \""+ Floorcode + "\" , \"Buildingcode\" : \"" + Buildingcode + "\"}";


        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Asset Class
    public void fetchAssetClasDetailsRequest(String Locationcode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {


        String rawJson = "{ \"Locationcode\": \""+ Locationcode +"\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Asset Detail
    public void fetchAssetDetailsRequest(String Assetclass, String Locationcode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {


        String rawJson = "{ \"Assetclass\": \""+ Assetclass + "\" , \"Locationcode\" : \"" + Locationcode + "\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Sub Asset Detail
    public void fetchSubAssetDetailsRequest(String Assetcode, String Locationcode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"Assetcode\": \""+ Assetcode + "\" , \"Locationcode\" : \"" + Locationcode + "\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Sub Asset Part Detail
    public void fetchSubAssetPartDetailsRequest(String SubAssetcode, String tagStatus,/* String Flag,*/ ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        //String rawJson = "{ \"SubAssetcode\": \""+ SubAssetcode + "\", \"Locationcode\" : \"" + Locationcode + "\" , \"Flag\" : \"" + Flag + "\"}";
        String rawJson = "{ \"SubAssetcode\": \""+ SubAssetcode + "\", \"Tagstatus\" : \"" + tagStatus + "\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    public void fetchAllDropDownValuesBySubAssetPartDetailsRequest(String subAssetPartscode, ResponseListener<FilterAssetRFIDMappingResponse> responseListener) {

        String rawJson = "{ \"SubAssetPartscode\": \""+ subAssetPartscode + "\"}";

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );

        Call<FilterAssetRFIDMappingResponse> call = getApiService().filterAssetRFIDMapping(requestBody);
        call.enqueue(new Callback<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onResponse(Call<FilterAssetRFIDMappingResponse> call, Response<FilterAssetRFIDMappingResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<FilterAssetRFIDMappingResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }
}
