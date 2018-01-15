package gai.mooc.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/15.
 */
//JSON格式的时候 JSON格式不序列化null值
@JsonSerialize( include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    public ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse createSuccess(){
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse createSuccess(String msg){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse createSuccess(T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), data);
    }


    public static <T> ServerResponse createSuccess(String msg, T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse createError(){
        return new ServerResponse(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse createError(String errMsg){
        return new ServerResponse(ResponseCode.ERROR.getCode(),errMsg);
    }

    public static <T> ServerResponse createError(int errCode, String errMsg){
        return new ServerResponse(errCode,errMsg);
    }


}
