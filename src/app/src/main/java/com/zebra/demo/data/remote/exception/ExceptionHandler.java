package com.zebra.demo.data.remote.exception;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.view.listener.ApiResponseListener;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class ExceptionHandler {
	public static final String TAG = "ExceptionHandler";
	
	/**
	 * Error Handling
	 * @param errorResource
	 * @param listener
	 */
	public static void handleResponseException(ErrorResource errorResource, ExceptionHandlerListener listener) {
		switch (errorResource.getStatus()) {
			case 401:
				listener.onInvalidLogin(errorResource);
			case 403:
				listener.onForbidden(errorResource);
			case 404:
				listener.onResourceNotFound(errorResource);
			case 409:
				listener.onResourceConflict(errorResource);
			case 500:
				listener.onInternalError(errorResource);
			default:
				listener.onUnknownException(errorResource);
		}
	}

	/**
	 * Throw
	 *
	 * @param e
	 * @param listener
	 */
	public static void handleListenerException(ApiResponseListener apiResponseListener, Throwable e, ExceptionHandlerListener listener) {
		if (e.getCause() instanceof ConnectException || e instanceof UnknownHostException) {
			listener.onInternetUnavailable();
		} else {
			if(apiResponseListener != null) {
				apiResponseListener.onFailed("Something went wrong, try again later");
			}
		}
	}


	/**
	 * Throw
	 *
	 * @param e
	 * @param listener
	 */
	public static void handleLoginListenerException(ApiResponseListener apiResponseListener, Throwable e, ExceptionHandlerListener listener) {
		if (e.getCause() instanceof ConnectException) {
			apiResponseListener.onFailed("Application tries to connect to a remote host but fails to establish a connection");
		} else if (e.getCause() instanceof UnknownHostException) {
			apiResponseListener.onFailed("Application is unable to resolve the hostname to an IP address");
		} else {
			if(apiResponseListener != null) {
				apiResponseListener.onFailed("Something went wrong, try again later");
			}
		}
	}
}
