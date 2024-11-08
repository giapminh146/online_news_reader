package vn.edu.usth.test;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;

import vn.edu.usth.test.Models.Podcast;

public class FullScreenPodcastActivity extends AppCompatActivity {
    private TextView txtPodcastTitle, tvDescription;
    private ImageView imgThumbnail;
    private ImageButton btnPlayPause, btnNext, btnBack, arrowCollapse;
    private SeekBar audioProgressBar;
    private TextView tvCurrentTime, tvTotalTime;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_podcast);

        // Initialize UI elements
        txtPodcastTitle = findViewById(R.id.txtPodcastTitle);
        btnPlayPause = findViewById(R.id.btnPlayPauseFull);
        btnNext = findViewById(R.id.btnNextFull);
        btnBack = findViewById(R.id.btnBackFull);
        imgThumbnail = findViewById(R.id.imgPodcastThumbnail);
        audioProgressBar = findViewById(R.id.seekBarFull);
        arrowCollapse = findViewById(R.id.btnCollapse);
        tvCurrentTime = findViewById(R.id.txtCurrentTimeFull);
        tvTotalTime = findViewById(R.id.txtTotalTimeFull);
        tvDescription = findViewById(R.id.tvDescription);

        // Get the podcast details passed from PodcastActivity
        Intent intent = getIntent();
        String podcastTitle = intent.getStringExtra("podcastTitle");
        String thumbnailUrl = intent.getStringExtra("podcastThumbnail");
        String podcastDescription = intent.getStringExtra("podcastDescription");
        int currentPosition = intent.getIntExtra("currentPosition", 0);
        int totalDuration = intent.getIntExtra("totalDuration", 0);
        String cleanHTMLDescription = podcastDescription.replace("?", "");
        Spanned spannedText = Html.fromHtml(cleanHTMLDescription, Html.FROM_HTML_MODE_COMPACT);

        // Load the image using Picasso or any other image loading library
        Picasso.get().load(thumbnailUrl).into(imgThumbnail);
        txtPodcastTitle.setText(podcastTitle);
        audioProgressBar.setMax(totalDuration);
        tvTotalTime.setText(formatTime(totalDuration));
        audioProgressBar.setProgress(currentPosition);
        tvCurrentTime.setText(formatTime(currentPosition));
        tvDescription.setText(spannedText);

        mediaPlayer = PodcastActivity.getMediaPlayer();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.ic_pause);
            PodcastActivity.isPlaying = true;
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play);
            PodcastActivity.isPlaying = false;
        }

        btnPlayPause.setOnClickListener(view -> togglePlayPause());
        btnNext.setOnClickListener(view -> nextPodcast());
        btnBack.setOnClickListener(view -> previousPodcast());
        arrowCollapse.setOnClickListener(view ->  {// Broadcast the latest podcast title to PodcastActivity
            String updatedTitle = txtPodcastTitle.getText().toString();
            Intent intent1 = new Intent("PodcastTitleUpdate");
            intent1.putExtra("updatedTitle", updatedTitle);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            Intent intent2 = new Intent("PLAYBACK_STATE_CHANGED");
            intent2.putExtra("isPlaying", PodcastActivity.isPlaying);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
            // Log the action for debugging
            Log.d("FullScreenPodcast", "Broadcast sent with isPlaying: " + PodcastActivity.isPlaying);
            Log.d("FullScreenPodcast", "Broadcast sent with title: " + updatedTitle);
            // Finish the full-screen activity
            finish();
        });

        // Update SeekBar based on podcast progress
        updateProgressBar();

        audioProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (PodcastActivity.isPlaying) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);
                PodcastActivity.isPlaying = false;
                handler.removeCallbacksAndMessages(null);  // Stop progress updates
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
                PodcastActivity.isPlaying = true;
                updateProgressBar();
            }
        }
    }

    private void updateProgressBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && PodcastActivity.isPlaying) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    audioProgressBar.setProgress(currentPosition);
                    tvCurrentTime.setText(formatTime(currentPosition));

                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void nextPodcast() {
        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);
        if (PodcastActivity.getCurrentPodcastIndex() < PodcastActivity.getPodcastList().size() - 1) {
            int nextIndex = PodcastActivity.getCurrentPodcastIndex() + 1;
            PodcastActivity.setCurrentPodcastIndex(nextIndex);
            playPodcast(nextIndex);
        } else {
            PodcastActivity.setCurrentPodcastIndex(0); // Reset to first podcast

            playPodcast(PodcastActivity.getCurrentPodcastIndex());
        }
    }

    private void previousPodcast() {
        // Remove any pending callbacks to prevent accessing a released MediaPlayer
        handler.removeCallbacksAndMessages(null);
        if (PodcastActivity.getCurrentPodcastIndex() > 0) {
            int prevIndex = PodcastActivity.getCurrentPodcastIndex() - 1;
            PodcastActivity.setCurrentPodcastIndex(prevIndex);
            playPodcast(prevIndex);
        } else {
            // Optionally, loop back to the last podcast or show a message
            PodcastActivity.setCurrentPodcastIndex(PodcastActivity.getPodcastList().size() - 1); // Reset to last podcast
            Podcast lastPodcast = PodcastActivity.getPodcastList().get(PodcastActivity.getCurrentPodcastIndex());
            playPodcast(PodcastActivity.getCurrentPodcastIndex());
        }
    }
    private void broadcastPodcastInfo(String title) {
        Intent intent = new Intent("PodcastInfoUpdate");
        intent.putExtra("newTitle", title);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void playPodcast(int podcastIndex) {
        Podcast podcast = PodcastActivity.getPodcastList().get(podcastIndex);

        // Update UI with the new podcast details
        txtPodcastTitle.setText(podcast.getTitle());
        tvDescription.setText(Html.fromHtml(podcast.getDescription().replace("?", ""), Html.FROM_HTML_MODE_COMPACT));
        Picasso.get().load(podcast.getThumbnail()).into(imgThumbnail);
        // Broadcast updated podcast info to PodcastActivity
        broadcastPodcastInfo(podcast.getTitle());
        // Stop and reset MediaPlayer to avoid IllegalStateException
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(podcast.getAudio());
                mediaPlayer.prepare();
                mediaPlayer.start();

                // Update the play/pause button
                btnPlayPause.setImageResource(R.drawable.ic_pause);
                PodcastActivity.isPlaying = true;

                // Set SeekBar and times
                int totalDuration = mediaPlayer.getDuration();
                audioProgressBar.setMax(totalDuration);
                tvTotalTime.setText(formatTime(totalDuration));
                updateProgressBar();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error playing podcast", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendPlaybackUpdate() {
        Intent intent = new Intent("PLAYBACK_STATE_CHANGED");
        intent.putExtra("isPlaying", isPlaying);
        sendBroadcast(intent);
    }

    private String formatTime(int timeInMillis) {
        int minutes = (timeInMillis / 1000) / 60;
        int seconds = (timeInMillis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer = null;
        }
    }
}
