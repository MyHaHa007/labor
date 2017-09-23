package cn.tianruan.LaborContractUser.ContractDetails.api;

import cn.tianruan.LaborContractUser.ContractDetails.javabean.contractdetailsjavabean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface ContractDetailsAPI {
    @GET("a/ldht/contractSign/showSignByMobile;JSESSIONID={JSESSIONID}")
    Call<contractdetailsjavabean> getContractDetailsData(@Path("JSESSIONID") String JSESSIONID,
                                                         @Query("__ajax")boolean ajax,
                                                         @Query("id")String id,
                                                         @Query("ldhtTemplate.id")String ldhtTemplateID);
    @FormUrlEncoded
    @POST("a/ldht/contractSign/saveSignByMobile;JSESSIONID={JSESSIONID}")
    Call<ResponseBody> postImg(@Path("JSESSIONID") String JSESSIONID,
                               @Query("__ajax")boolean ajax,
                               @Field("Id")String id,
                               @Field("staffImage")byte[] staffImage);
}
