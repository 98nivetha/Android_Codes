package com.zebra.demo.data.remote.repository;
import com.zebra.demo.data.remote.BaseApiService;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.FilterAssetNameRequest;
import com.zebra.demo.data.remote.model.MetaDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MetaDetailRepository extends BaseApiService {
    private static MetaDetailRepository metaDetailRepository;

    public static MetaDetailRepository getInstance() {
        if (metaDetailRepository == null) {
            metaDetailRepository = new MetaDetailRepository();
        }
        return metaDetailRepository;
    }

    public void fetchMetaDetails(int mapstatus, ResponseListener<MetaDetail> responseListener) {
        FilterAssetNameRequest filterAssetNameRequest = new FilterAssetNameRequest(null,null,null,mapstatus);
        Call<MetaDetail> call = getApiService().fetchMetaDetails("BuildConfig.AUTHORIZATION",filterAssetNameRequest);
        call.enqueue(new Callback<MetaDetail>() {
            @Override
            public void onResponse(Call<MetaDetail> call, Response<MetaDetail> response) {
                if(response.code() == 200) {
                    responseListener.onDataReceived(response.body());
                } else {
                    responseListener.onError("Something went wrong, try again later");
                }
            }

            @Override
            public void onFailure(Call<MetaDetail> call, Throwable t) {
                responseListener.onFailure(t);
            }
        });
    }
}
