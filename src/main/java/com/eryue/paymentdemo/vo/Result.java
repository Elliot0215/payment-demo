package com.eryue.paymentdemo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 二月
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private Integer code;
    private String message;
    private Map<String, Object> data;

    public static Result ok() {
        Result r = new Result();
        r.setCode(0);
        r.setMessage("成功");
        return r;
    }

    public static Result error() {
        Result r = new Result();
        r.setCode(-1);
        r.setMessage("失败");
        return r;
    }

    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
