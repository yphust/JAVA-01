package Week_02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * okhttp.jar
 * okio.jar
 * kotlin-stdlib.jar
 */
public class OKHttlpClientTest {

    public static void main(String[] args){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url("http://localhost:8801")//请求接口。如果需要传参拼接到接口后面
                        .build();//创建Request 对象
                Response response = null;

                try {
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        System.out.println("response.code()=="+response.code());
                        System.out.println("response.message()=="+response.message());
                        System.out.println("res=="+response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
