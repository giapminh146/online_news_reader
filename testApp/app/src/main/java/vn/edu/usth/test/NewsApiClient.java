package vn.edu.usth.test;

import vn.edu.usth.test.Models.request.TopHeadlinesRequest;
import vn.edu.usth.test.Models.response.ArticleResponse;
import vn.edu.usth.test.Network.APIClient;
import vn.edu.usth.test.Network.APIService;

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

    private Map<String, String> createQuery(){
        query = new HashMap<>();
        query.put("apiKey", mApiKey);
        return query;
    }

    public void getTopHeadlines(TopHeadlinesRequest topHeadlinesRequest, final ArticlesResponseCallback callback){


        query = createQuery();
        query.put("country", topHeadlinesRequest.getCountry());
        query.put("language", topHeadlinesRequest.getLanguage());
        query.put("category", topHeadlinesRequest.getCategory());
        query.put("sources", topHeadlinesRequest.getSources());
        query.put("q", topHeadlinesRequest.getQ());
        query.put("pageSize", topHeadlinesRequest.getPageSize());
        query.put("page", topHeadlinesRequest.getPage());

        query.values().removeAll(Collections.singleton(null));
        query.values().removeAll(Collections.singleton("null"));


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