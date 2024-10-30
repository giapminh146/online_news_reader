package vn.edu.usth.test;

import vn.edu.usth.test.Models.request.TopHeadlinesRequest;
import vn.edu.usth.test.Models.response.ArticleResponse;
import vn.edu.usth.test.Network.APIClient;
import vn.edu.usth.test.Network.APIService;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class NewsApiClient {
    private String mApiKey;
    private Map<String, String> query;
    private APIService mAPIService;

    public NewsApiClient(String apiKey){
        mApiKey = apiKey;
        mAPIService = APIClient.getAPIService();
        query = new HashMap<>();
        query.put("apiKey", apiKey);
    }

    public interface ArticlesResponseCallback{
        void onSuccess(ArticleResponse response);
        void onFailure(Throwable throwable);
    }


    private Throwable errMsg(String str) {
        Throwable throwable = null;
        try {
            JSONObject obj = new JSONObject(str);
            throwable = new Throwable(obj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (throwable == null){
            throwable = new Throwable("An error occured");
        }


        return throwable;
    }

    private Map<String, String> createQuery(TopHeadlinesRequest topHeadlinesRequest){
        query = new HashMap<>();
        query.put("apiKey", mApiKey);
        if (topHeadlinesRequest.getCountry() != null && !topHeadlinesRequest.getCountry().equals("All")) {
            query.put("country", topHeadlinesRequest.getCountry());
        }
        if (topHeadlinesRequest.getLanguage() != null && !topHeadlinesRequest.getLanguage().equals("All")) {
            query.put("language", topHeadlinesRequest.getLanguage());
        }
        if (topHeadlinesRequest.getCategory() != null && !topHeadlinesRequest.getCategory().equals("All")) {
            query.put("category", topHeadlinesRequest.getCategory());
        }
        query.put("sources", topHeadlinesRequest.getSources());
        query.put("q", topHeadlinesRequest.getQ());
        query.put("pageSize", topHeadlinesRequest.getPageSize());
        query.put("page", topHeadlinesRequest.getPage());

        query.values().removeAll(Collections.singleton(null));
        query.values().removeAll(Collections.singleton("null"));
        query.values().removeAll(Collections.singleton(""));
        return query;
    }

    public void getTopHeadlines(TopHeadlinesRequest topHeadlinesRequest, final ArticlesResponseCallback callback){
        Map<String, String> query = createQuery(topHeadlinesRequest);
        query.values().removeAll(Collections.singleton(null));

        //For check the link
        StringBuilder url = new StringBuilder("https://newsapi.org/v2/top-headlines?");
        for (Map.Entry<String, String> entry : query.entrySet()) {
            url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String urlString = url.toString().replaceAll("&$", "");
        Log.d("NewsApiClient", "Request URL: " + url.toString());

        mAPIService.getTopHeadlines(query)
                .enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            callback.onSuccess(response.body());
                        }

                        else{
                            try {
                                callback.onFailure(errMsg(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticleResponse> call, Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });
    }
}