package com.zebra.demo.data.remote.repository;

import com.zebra.demo.BuildConfig;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.ApiResponse;
import com.zebra.demo.data.remote.model.EmployeeAssetScannedInsertRequest;
import com.zebra.demo.data.remote.model.EmployeeDetail;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchRequest;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchResponse;
import com.zebra.demo.data.remote.model.EmployeesAllocatedAssetDetailRequest;
import com.zebra.demo.data.remote.model.EmployeesAllocatedAssetDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeVerifyRepository extends BaseApiService {
    private static EmployeeVerifyRepository employeeVerifyRepository;

    public static EmployeeVerifyRepository getInstance() {
        if (employeeVerifyRepository == null) {
            employeeVerifyRepository = new EmployeeVerifyRepository();
        }
        return employeeVerifyRepository;
    }

    //Employee List
    public void employeeAssetVerificationInitial(ResponseListener<List<EmployeeDetail>> responseListener) {
        Call<List<EmployeeDetail>> call = getApiService().employeeAssetVerificationInitial("BuildConfig.AUTHORIZATION");
        call.enqueue(new Callback<List<EmployeeDetail>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetail>> call, Response<List<EmployeeDetail>> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetail>> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Employee Detail with Assert
    public void employeesAllocatedAssetDetails(EmployeesAllocatedAssetDetailRequest employeesAllocatedAssetDetailRequest, ResponseListener<EmployeesAllocatedAssetDetailsResponse> responseListener) {
        Call<EmployeesAllocatedAssetDetailsResponse> call = getApiService().employeesAllocatedAssetDetails("BuildConfig.AUTHORIZATION", employeesAllocatedAssetDetailRequest);
        call.enqueue(new Callback<EmployeesAllocatedAssetDetailsResponse>() {
            @Override
            public void onResponse(Call<EmployeesAllocatedAssetDetailsResponse> call, Response<EmployeesAllocatedAssetDetailsResponse> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<EmployeesAllocatedAssetDetailsResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Get Assert Status
    public void employeeVerifyAssetFetch(EmployeeVerifyAssetFetchRequest employeeVerifyAssetFetchRequest, ResponseListener<EmployeeVerifyAssetFetchResponse> responseListener) {
        Call<EmployeeVerifyAssetFetchResponse> call = getApiService().employeeVerifyAssetFetch("BuildConfig.AUTHORIZATION", employeeVerifyAssetFetchRequest);
        call.enqueue(new Callback<EmployeeVerifyAssetFetchResponse>() {
            @Override
            public void onResponse(Call<EmployeeVerifyAssetFetchResponse> call, Response<EmployeeVerifyAssetFetchResponse> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<EmployeeVerifyAssetFetchResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

    //Employee Asset Scanned Insert
    public void employeeAssetScannedInsert(EmployeeAssetScannedInsertRequest employeeAssetScannedInsertRequest, ResponseListener<ApiResponse> responseListener) {
        Call<ApiResponse> call = getApiService().employeeAssetScannedInsert("BuildConfig.AUTHORIZATION", employeeAssetScannedInsertRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }

}

/*public void employeeVerifyTagUpdate(String Remarks, List<ScanAssetDetail> scanDetailList, ResponseListener<ApiResponse> responseListener) {


        Call<ApiResponse> call = getApiService().employeeVerifyTagUpdate(BuildConfig.AUTHORIZATION, Remarks,scanDetailList);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });

    }*/