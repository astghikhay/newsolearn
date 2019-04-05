package com.astghik.newsolearn.listeners;

public interface NetworkRequestListener<T> {
    void onResponseReceive(T obj);

    void onError(String message);


}
