package fcdk.zayetsgram;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.HashMap;
import java.util.Map;


import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity {
    WebView webView;
    private String code;
    private String token;

    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.instagram.com")
            .build();

    private RetroPost retroPost = retrofit.create(RetroPost.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView)findViewById(R.id.webview);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);




        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Uri uri = Uri.parse(url);
                code = uri.getQueryParameter("code");
//                Toast.makeText(MainActivity.this, code, Toast.LENGTH_SHORT).show();

                if (code!=null){
                    getData(code);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id=e6b21190d0f940a789dd78ef3eb3e2ac&redirect_uri=http://yourcallback.com&response_type=code");
    }


    private void getData (String code){
        final Map<String,String> dateMap = new HashMap<String, String>();
        dateMap.put("client_id","e6b21190d0f940a789dd78ef3eb3e2ac");
        dateMap.put("client_secret","9c2e1b0c74cb4755a8ea67f7e8ab8c95");
        dateMap.put("grant_type","authorization_code");
        dateMap.put("redirect_uri","http://yourcallback.com");
        dateMap.put("code",code);

        Call<Object> call = retroPost.updateDate(dateMap);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                String result = response.body().toString();
                token = result.substring(14,65);
                webView.loadUrl("https://api.instagram.com/v1/users/self/?access_token="+token);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }

        });

    }

}


