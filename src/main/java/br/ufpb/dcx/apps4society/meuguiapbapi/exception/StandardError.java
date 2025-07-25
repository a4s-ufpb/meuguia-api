package br.ufpb.dcx.apps4society.meuguiapbapi.exception;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class StandardError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private LocalDateTime timeStamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public StandardError(LocalDateTime timeStamp, Integer status, String error, String message, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "StandardError{" +
                "timeStamp=" + timeStamp +
                ", status=" + status +
                ", error=" + error +
                ", message=" + message +
                ", path=" + path +
                '}';
    }
}
