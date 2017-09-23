package cn.tianruan.LaborContractUser.Login.javabean;

/**
 * Created by Administrator on 2017/7/12.
 */
public class loginsuccessful {

    /**
     * id : 1
     * loginName : system
     * name : 系统管理员
     * mobileLogin : true
     * sessionid : b6b486a8919e4fc196358e10b6a82a2b
     */

    private String id;
    private String loginName;
    private String name;
    private boolean mobileLogin;
    private String sessionid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public void setMobileLogin(boolean mobileLogin) {
        this.mobileLogin = mobileLogin;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
