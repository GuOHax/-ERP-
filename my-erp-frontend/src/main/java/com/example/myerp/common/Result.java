// my-erp-frontend/src/main/java/com/example/myerp/common/Result.java
package com.example.myerp.common;

public class Result<T> {
    private int code;       // 0=成功，其他=失败
    private String message; // 提示信息
    private T data;         // 响应数据

    public Result() { }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <E> Result<E> success(E data, String message) {
        return new Result<>(0, message, data);
    }

    public static <E> Result<E> error(String message) {
        return new Result<>(1, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
