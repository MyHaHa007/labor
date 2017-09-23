package cn.tianruan.LaborContractUser.register.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/22.
 */
public interface RegisterAPI {
    //注册接口
    @FormUrlEncoded
    @POST("a/sys/user/registerSubmitByMobile")
    Call<ResponseBody> register(@Field("mobile")String mobile,
                                @Field("newPassword")String newPassword,
                                @Field("confirmNewPassword")String confirmNewPassword,
                                @Field("name")String name,
                                @Field("identityCard")String identityCard,
                                @Field("permanentAddress")String permanentAddress,
                                @Field("correspondenceAddress")String correspondenceAddress,
                                @Field("xlxwType")String xlxwType,
                                @Field("phone")String phone,
                                @Field("email")String email,
                                @Field("idCardFrontBase64")String idCardFrontBase64,
                                @Field("idCardBackBase64")String idCardBackBase64,
                                @Field("xlxwBase64")String xlxwBase64);

}
