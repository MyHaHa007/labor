package cn.tianruan.LaborContractUser.WaitSign.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface WaitSignAPI {
    @GET("a/ldht/ldhtFormworkGuangdong/listByMobile;JSESSIONID={JSESSIONID}")
    Call<ResponseBody> getWaitSignData(@Path("JSESSIONID") String JSESSIONID,
                                       @Query("__ajax")boolean ajax,
                                       @Query("contractSignStatus")int contractSignStatus);

}
