package edu.xuecj.wiki.resp;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/8 23:04
 */
public class CommonResp<T> {
    /*
    * 业务上的登录成功或失败
    * */
    private boolean success=true;
//    返回信息
    private String message;
//    返回泛型数据，自定义数据类型
    private T content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommResp{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
