package nio;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * okhttp.jar
 * okio.jar
 * kotlin-stdlib.jar
 */
public class MyOKHttlpClient {

    public static String getClientResponse(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        Request request = new Request.Builder()
                .url(url)//请求接口。如果需要传参拼接到接口后面
                .build();//创建Request 对象
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        }else{
            return "not successful";
        }
    }
}
