package vn.edu.usth.test;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.edu.usth.test.Models.Podcast;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    private List<Podcast> podcastList;
    private Context context;
    private OnPodcastClickListener listener;

    public interface OnPodcastClickListener {
        void onPodcastClick(Podcast podcast);
        void onPlayClick(Podcast podcast);
    }

    public PodcastAdapter(List<Podcast> podcastList, Context context) {
        this.podcastList = podcastList;
        this.context = context;
        if (context instanceof OnPodcastClickListener) {
            listener = (OnPodcastClickListener) context;
        }
    }

    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_podcast, parent, false);
        return new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder holder, int position) {
        Podcast podcast = podcastList.get(position);
        holder.title.setText(podcast.getTitle());
        holder.description.setText(Html.fromHtml(podcast.getDescription().replace("?", ""), Html.FROM_HTML_MODE_COMPACT));

        // Format publish date
        Date date = new Date(podcast.getPubDateMs());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        holder.publishDate.setText(sdf.format(date));

        Glide.with(context).load(podcast.getThumbnail()).placeholder(R.drawable.placeholder).into(holder.thumbnail);

        holder.playButton.setOnClickListener(v -> {
            if (listener != null) listener.onPlayClick(podcast);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onPodcastClick(podcast);
        });
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    public class PodcastViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, publishDate;
        ImageView thumbnail;
        ImageButton playButton;

        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.podcastTitle);
            description = itemView.findViewById(R.id.podcastDescription);
            publishDate = itemView.findViewById(R.id.podcastPublicDate);
            thumbnail = itemView.findViewById(R.id.podcastThumbnail);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}
