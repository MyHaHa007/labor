package cn.tianruan.LaborContractUser.Login.api;

import cn.tianruan.LaborContractUser.Login.javabean.loginsuccessful;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface LoginAPI {
    //登录接口
    @FormUrlEncoded
    @POST("a/login?__ajax=true")
    Call<loginsuccessful> login(@Field("username")String username, @Field("password")String password,
                                @Field("mobileLogin")boolean isPhone);
}
