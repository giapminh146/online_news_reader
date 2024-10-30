package vn.edu.usth.test.Models.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import vn.edu.usth.test.Models.Podcast;

public class PodcastResponse {
    @SerializedName("results")
    private List<Podcast> results;

    public List<Podcast> getResults() {
        return results;
    }

    public void setResults(List<Podcast> results) {
        this.results = results;
    }
}
