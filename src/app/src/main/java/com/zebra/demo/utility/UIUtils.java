package com.zebra.demo.utility;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.zebra.demo.view.listener.NetworkRetryListener;

public class UIUtils {
    public static void showNetworkRetryDialog(Context context, NetworkRetryListener networkRetryListener) {

        if(context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("No Internet connection available")
                    .setPositiveButton("Retry", (dialog, id) -> {
                        if (networkRetryListener != null) {
                            networkRetryListener.onClickRetry();
                        }
                    })
                    .setNegativeButton("Close", (dialog, id) -> {
                        if (networkRetryListener != null) {
                            networkRetryListener.onClickClose();
                        }
                    });
            builder.create().show();
        }
    }

    public static void showNoNetworkDialog(Context context) {
        if(context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("No Internet connection available")
                    .setPositiveButton("Ok", (dialog, id) -> {
                    });
            builder.create().show();
        }
    }
}
