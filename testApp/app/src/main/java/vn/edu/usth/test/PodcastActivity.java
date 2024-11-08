package vn.edu.usth.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.test.Models.Podcast;
import vn.edu.usth.test.Models.response.PodcastResponse;
import vn.edu.usth.test.Network.PodcastApiService;
import vn.edu.usth.test.Network.RetrofitClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class PodcastActivity extends AppCompatActivity implements PodcastAdapter.OnPodcastClickListener {

    private RecyclerView recyclerView;
    private PodcastAdapter adapter;
    public static List<Podcast> podcastList = new ArrayList<>();
    private ProgressBar progressBar;

    private LinearLayout playingBox;
    private TextView txtPlayingPodcast;
    private ImageButton btnPlayPause, btnNext, btnClose, btnBack;

    public static int currentPodcastIndex = 0; // Keep track of the currently playing podcast index
    private String API_KEY;

    private static MediaPlayer mediaPlayer;
    private SeekBar audioProgressBar;
    private TextView tvCurrentTime, tvTotalTime;
    private Handler handler = new Handler();  // To update the UI regularly
    private List<Podcast> filteredPodcastList = new ArrayList<>();  // For storing filtered results
    private SearchView searchView;  // SearchView for searching podcasts
    protected static boolean isPlaying = false;  // Boolean flag to track if podcast is playing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);

        // Initialize UI elements

        //Call the header
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_header, new HeaderFragment()).commit();

        API_KEY = getResources().getString(R.string.PODCAST_API_KEY);
        recyclerView = findViewById(R.id.podcastRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        playingBox = findViewById(R.id.playingBox);
        txtPlayingPodcast = findViewById(R.id.txtPlayingPodcast);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnClose = findViewById(R.id.btnClose);
        audioProgressBar = findViewById(R.id.audioProgressBar);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        searchView = findViewById(R.id.searchView);  // Initialize SearchView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PodcastAdapter(filteredPodcastList, this);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_podcast);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_podcast) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.navigation_more) {
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        fetchPodcast();

        playingBox.setOnClickListener(v -> openFullScreenPodcast());
        btnPlayPause.setOnClickListener(view -> togglePlayPause());
        btnNext.setOnClickListener(view -> nextPodcast());
        btnClose.setOnClickListener(view -> stopPlaying());
        btnBack.setOnClickListener(view -> previousPodcast());
        // Set up swipe-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchPodcast();
            Toast.makeText(PodcastActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);  // Stop the refresh animation once data is fetched
        });

        // Update SeekBar based on podcast progress
        audioProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optionally pause updates while the user is dragging
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optionally resume updates after the user finishes dragging
            }
        });

        // Add search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPodcasts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPodcasts(newText);
                return true;
            }
        });

        // Listener for the "X" button to clear search
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Clear the filter and show all podcasts
                filteredPodcastList.clear();
                filteredPodcastList.addAll(podcastList);
                adapter.notifyDataSetChanged();
                return false;  // false to perform the default behavior (clear the search text)
            }
        });


    }

    private void fetchPodcast() {
        progressBar.setVisibility(View.VISIBLE);
        PodcastApiService apiService = RetrofitClient.getClient(getResources().getString(R.string.PODCAST_BASE_URL))
                .create(PodcastApiService.class);

        Call<PodcastResponse> call = apiService.getSearchPodcasts(
                API_KEY, "breaking news", "episode", 10, 60, "English", 1);

        call.enqueue(new Callback<PodcastResponse>() {
            @Override
            public void onResponse(@NonNull Call<PodcastResponse> call, @NonNull Response<PodcastResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    podcastList.clear();
                    podcastList.addAll(response.body().getResults());
                    filteredPodcastList.clear();  // Clear filtered list
                    filteredPodcastList.addAll(podcastList);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(PodcastActivity.this, "Failed to retrieve podcasts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PodcastResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PodcastActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static List<Podcast> getPodcastList() {
        return podcastList;
    }

    public static int getCurrentPodcastIndex() {
        return currentPodcastIndex;
    }

    public static void setCurrentPodcastIndex(int currentPodcastIndex) {
        PodcastActivity.currentPodcastIndex = currentPodcastIndex;
    }

    private void openFullScreenPodcast() {
        if (mediaPlayer != null && !podcastList.isEmpty()) {
            Podcast currentPodcast = podcastList.get(currentPodcastIndex);

            Intent intent = new Intent(PodcastActivity.this, FullScreenPodcastActivity.class);
            intent.putExtra("podcastTitle", currentPodcast.getTitle());
            intent.putExtra("podcastThumbnail", currentPodcast.getThumbnail());
            intent.putExtra("podcastDescription", currentPodcast.getDescription());
            intent.putExtra("currentPosition", mediaPlayer.getCurrentPosition());
            intent.putExtra("totalDuration", mediaPlayer.getDuration());

            startActivity(intent);
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }



    // Method to filter podcasts based on search query
    private void filterPodcasts(String query) {
        filteredPodcastList.clear();  // Clear the filtered list before applying new filter

        if (query.isEmpty()) {
            filteredPodcastList.addAll(podcastList);  // If query is empty, show all podcasts
        } else {
            for (Podcast podcast : podcastList) {
                if (podcast.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredPodcastList.add(podcast);  // Add matching podcasts
                }
            }
        }

        if (filteredPodcastList.isEmpty()) {
            Toast.makeText(this, "No podcast found", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();  // Notify adapter of changes
    }

    @Override
    public void onPodcastClick(Podcast podcast) {
        int selectedIndex = podcastList.indexOf(podcast);  // Get the index of the clicked podcast
        playPodcast(selectedIndex);  // Play the selected podcast
    }

    @Override
    public void onPlayClick(Podcast podcast) {
        int selectedIndex = podcastList.indexOf(podcast);  // Get the index of the clicked podcast
        playPodcast(selectedIndex);  // Play the selected podcast
    }

    private BroadcastReceiver podcastInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the playing box with new title and thumbnail
            String newTitle = intent.getStringExtra("updatedTitle");
            Log.d("Podcast", "Broadcast received: " + newTitle);

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(podcastInfoReceiver, new IntentFilter("PodcastTitleUpdate"));
        IntentFilter filter = new IntentFilter("PLAYBACK_STATE_CHANGED");
        LocalBroadcastManager.getInstance(this).registerReceiver(playbackStateReceiver, filter);
        if (mediaPlayer != null) {
            int position = mediaPlayer.getCurrentPosition();  // Get the last known position
            mediaPlayer.seekTo(position);  // Resume from where it was paused
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();  // Start playing again
            }
            updateUI();
        }
    }
    private void updateUI() {
        String title = podcastList.get(currentPodcastIndex).getTitle();
        txtPlayingPodcast.setText("Playing: " + title);  // Update the title of the currently playing podcast

        togglePlayPause(isPlaying);
    }


    private void playPodcast(int podcastIndex) {
        Podcast podcast = podcastList.get(podcastIndex);

        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);

        // Release existing MediaPlayer if any
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        playingBox.setVisibility(View.VISIBLE);
        txtPlayingPodcast.setText("Playing: " + podcast.getTitle());
        Log.d("Podcast", "playPodcast() called for: " + podcast.getTitle());



        btnPlayPause.setBackgroundResource(R.drawable.ic_pause);

        // Update the current podcast index based on the clicked podcast
        currentPodcastIndex = podcastList.indexOf(podcast);

        // Do not set isPlaying to true here. It will be set after MediaPlayer starts.

        if (podcast.getAudio() != null && !podcast.getAudio().isEmpty()) {
            // If the audio URL is not null, start the media player
            new Thread(() -> {
                try {
                    String finalUrl = getFinalRedirectedUrl(podcast.getAudio()); // Resolve any redirects if needed
                    runOnUiThread(() -> startMediaPlayer(finalUrl)); // Call on the main thread
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error fetching audio URL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Podcast", "Error fetching: " + e.getMessage());
                    });
                }
            }).start();
        } else {
            // Handle the case where the audio URL is null or empty
            Toast.makeText(this, "Audio URL is not available for this podcast", Toast.LENGTH_SHORT).show();
            Log.e("Podcast", "Invalid URL: " + podcast.getAudio());
        }
    }

    private void startMediaPlayer(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            Log.d("PodcastActivity", "Playing audio from URL: " + audioUrl);
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                int totalDuration = mediaPlayer.getDuration();
                audioProgressBar.setMax(totalDuration);
                tvTotalTime.setText(formatTime(totalDuration));
                tvCurrentTime.setText(formatTime(0)); // Reset current time

                // Set isPlaying to true now that MediaPlayer has started
                isPlaying = true;
                btnPlayPause.setBackgroundResource(R.drawable.ic_pause);

                // Start updating the SeekBar and current time
                updateProgressBar();
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnCompletionListener(mp -> stopPlaying());
    }

    // Update the SeekBar and current time regularly
    private void updateProgressBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    try {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        audioProgressBar.setProgress(currentPosition);
                        tvCurrentTime.setText(formatTime(currentPosition));

                        // Continue updating every second
                        handler.postDelayed(this, 1000);
                    } catch (IllegalStateException e) {
                        // Handle the case where MediaPlayer is not in a valid state
                        Log.e("PodcastActivity", "MediaPlayer in invalid state: " + e.getMessage());
                    }
                }
            }
        }, 1000);
    }

    // Format time in milliseconds to mm:ss
    protected static String formatTime(int timeInMillis) {
        int minutes = (timeInMillis / 1000) / 60;
        int seconds = (timeInMillis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private BroadcastReceiver playbackStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPlaying = intent.getBooleanExtra("isPlaying", true);
            togglePlayPause(isPlaying);
            Log.d("Playback", "Received: current isplaying: " + isPlaying);
        }
    };

    // Updated togglePlayPause with state passed as a parameter
    private void togglePlayPause(boolean isPlaying) {
        if (mediaPlayer == null) return;

        if (!isPlaying) {
            mediaPlayer.pause();
            btnPlayPause.setBackgroundResource(R.drawable.ic_play); // Change icon to play
            handler.removeCallbacksAndMessages(null);  // Stop progress updates
        } else {
            mediaPlayer.start();
            btnPlayPause.setBackgroundResource(R.drawable.ic_pause); // Change icon to pause
            updateProgressBar();  // Resume progress updates
        }
    }

    private void togglePlayPause() {
        if (mediaPlayer == null) return;

        if (isPlaying) {
            mediaPlayer.pause();
            btnPlayPause.setBackgroundResource(R.drawable.ic_play); // Change icon to play
            isPlaying = false;
            handler.removeCallbacksAndMessages(null);  // Stop progress updates
        } else {
            mediaPlayer.start();
            btnPlayPause.setBackgroundResource(R.drawable.ic_pause); // Change icon to pause
            isPlaying = true;
            updateProgressBar();  // Resume progress updates
        }
    }


    private void nextPodcast() {
        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);

        if (currentPodcastIndex < podcastList.size() - 1) {
            int nextIndex = currentPodcastIndex + 1;
            currentPodcastIndex = nextIndex;
            playPodcast(nextIndex);
        } else {
            // Optionally, loop back to the first podcast or show a message
            currentPodcastIndex = 0; // Reset to first podcast

            playPodcast(currentPodcastIndex);
        }
    }

    private void previousPodcast() {
        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);

        if (currentPodcastIndex > 0) {
            int prevIndex = currentPodcastIndex - 1;
            currentPodcastIndex = prevIndex;
            playPodcast(prevIndex);
        } else {
            // Optionally, loop back to the last podcast or show a message
            currentPodcastIndex = podcastList.size() - 1; // Reset to last podcast
            Podcast lastPodcast = podcastList.get(currentPodcastIndex);
            playPodcast(currentPodcastIndex);
        }
    }

    private void stopPlaying() {
        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
        btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        playingBox.setVisibility(View.GONE);
    }

    // Function to get the final redirected URL
    private String getFinalRedirectedUrl(String audioUrl) throws IOException {
        String finalUrl = audioUrl;
        boolean redirect = true;

        while (redirect) {
            HttpURLConnection connection = (HttpURLConnection) new java.net.URL(finalUrl).openConnection();
            connection.setInstanceFollowRedirects(false);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                    responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                    responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                // Get redirect URL from "Location" header field
                String redirectUrl = connection.getHeaderField("Location");

                // If there's no redirect URL, stop redirecting
                if (redirectUrl == null) {
                    redirect = false;
                } else {
                    // Update finalUrl with the new location
                    finalUrl = redirectUrl;
                }
            } else {
                // No more redirects
                redirect = false;
            }

            connection.disconnect();
        }

        return finalUrl;
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(podcastInfoReceiver);
        Log.d("PodcastActivity", "BroadcastReceiver unregistered");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(playbackStateReceiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}