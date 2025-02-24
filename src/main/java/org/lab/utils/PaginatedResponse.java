package org.lab.utils;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> data;
    private long total;

    // Constructor
    public PaginatedResponse(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    // Getters and setters
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

