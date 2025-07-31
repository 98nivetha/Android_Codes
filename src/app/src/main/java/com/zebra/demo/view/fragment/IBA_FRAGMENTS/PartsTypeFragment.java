package com.zebra.demo.view.fragment.IBA_FRAGMENTS;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.databinding.FragmentPartsTypeBinding;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;

public class PartsTypeFragment extends Fragment implements RecyclerViewItemClickListener, ApiResponseListener {
    private FragmentPartsTypeBinding binding;

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            ReturnPartsValidInfoResponse.ValidItem selectedItem =
                    getArguments().getParcelable("selected_item");

            if (selectedItem != null) {
                // Use selectedItem.getMaterialname(), etc.
            }
        }
    }




    @Override
    public void onSuccess(Object data) {
        // Handle success here if needed
    }

    @Override
    public void onFailed(Object data) {
        // Handle error here if needed
    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {
        // You can access the clicked item here
    }
}
