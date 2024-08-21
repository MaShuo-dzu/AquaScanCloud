package org.qinian.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public static Result success(int code, String msg, Object data) {
        Result r = new Result();
        r.setData(data);
        r.setMsg(msg);
        r.setCode(code);
        return r;
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setData(data);
        r.setMsg(msg);
        r.setCode(code);
        return r;
    }
}
