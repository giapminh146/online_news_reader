package vn.edu.usth.test.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import vn.edu.usth.test.Models.response.PodcastResponse;

public interface PodcastApiService {
    @GET("search")
    Call<PodcastResponse> getSearchPodcasts(
            @Header("X-ListenAPI-Key") String apiKey,
            @Query("q") String query,
            @Query("type") String type,
            @Query("len_min") int lenMin,
            @Query("len_max") int lenMax,
            @Query("language") String language,
            @Query("sort_by_date") int sort_by_date
    );
}
