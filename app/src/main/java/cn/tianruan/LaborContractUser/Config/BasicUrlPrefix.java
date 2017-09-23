package cn.tianruan.LaborContractUser.Config;

/**
 * Created by Administrator on 2017/7/2.
 */
public class BasicUrlPrefix {
    private static String urlprefix="http://39.108.69.214:8080/ldht/";

    public static String getUrlprefix() {
        return urlprefix;
    }

    public static void setUrlprefix(String urlprefix) {
        BasicUrlPrefix.urlprefix = urlprefix;
    }
}
