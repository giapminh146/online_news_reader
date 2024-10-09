package vn.edu.usth.test.Network;

import retrofit2.http.Query;
import vn.edu.usth.test.Models.response.ArticleResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIService {
    @GET("/v2/top-headlines")
    Call<ArticleResponse> getTopHeadlines(@QueryMap Map<String, String> options);

}

