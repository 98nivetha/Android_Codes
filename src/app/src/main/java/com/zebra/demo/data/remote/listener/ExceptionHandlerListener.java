package com.zebra.demo.data.remote.listener;


import com.zebra.demo.data.remote.exception.ErrorResource;

public interface ExceptionHandlerListener {
	void onInternetUnavailable();
	void onInvalidLogin(ErrorResource errorResource);
	void onForbidden(ErrorResource errorResource);
	void onResourceNotFound(ErrorResource errorResource);
	void onInternalError(ErrorResource errorResource);
	void onUnknownException(ErrorResource errorResource);
	void onResourceConflict(ErrorResource errorResource);
}
