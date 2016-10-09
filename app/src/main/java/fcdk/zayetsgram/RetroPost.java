package fcdk.zayetsgram;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroPost {
    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<Object> updateDate(@FieldMap Map<String,String> map);
}
