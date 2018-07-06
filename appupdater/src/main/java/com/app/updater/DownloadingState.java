package com.app.updater;

/**
 * Created by Pravesh Sharma on 06-07-2018.
 */

public enum DownloadingState {
    STATUS_FAILED("STATUS_FAILED"),
    STATUS_PAUSED("STATUS_PAUSED"),
    STATUS_PENDING("STATUS_PENDING"),
    STATUS_RUNNING("STATUS_RUNNING"),
    STATUS_SUCCESSFUL("STATUS_SUCCESSFUL");

    private String status;

    DownloadingState(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }


}
