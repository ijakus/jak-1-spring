package com.roundtable.spring.request;

public class SortingRequest {

    private Long arraySize;

    public SortingRequest() {
    }

    public SortingRequest(final Long arraySize) {
        this.arraySize = arraySize;
    }

    public Long getArraySize() {
        return arraySize;
    }

    public void setArraySize(final Long arraySize) {
        this.arraySize = arraySize;
    }
}
