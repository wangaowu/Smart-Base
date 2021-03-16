package com.bytemiracle.base.framework.utils;

class ProgressModel {
    private long ContentLength;
    private long CurrentBytes;
    private boolean done;
    public ProgressModel(long totalBytesRead, long l, boolean b) {
    }

    public long getContentLength() {
        return ContentLength;
    }

    public void setContentLength(long contentLength) {
        ContentLength = contentLength;
    }

    public long getCurrentBytes() {
        return CurrentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        CurrentBytes = currentBytes;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
