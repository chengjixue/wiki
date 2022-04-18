package edu.xuecj.wiki.req;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/18 19:27
 */
public class UserQueryReq extends PageReq{
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "loginName='" + loginName + '\'' +
                "} " + super.toString();
    }
}
