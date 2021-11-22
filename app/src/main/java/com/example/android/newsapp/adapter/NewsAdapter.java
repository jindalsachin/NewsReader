package com.example.android.newsapp.adapter;

import com.example.android.newsapp.News;
import com.example.android.newsapp.R;
import android.content.Context;
import android.content.Intent;
import java.text.ParseException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.net.Uri;
import android.os.AsyncTask;
import java.util.Date;
import java.util.TimeZone;
import android.util.Log;
import android.view.LayoutInflater;
import android.text.format.DateUtils;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();
    private Context m_context;
    private ArrayList<News> m_newsList;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        this.m_context = context;
        this.m_newsList = newsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(m_context)
                        .inflate(R.layout.news_card, parent, false)
        );
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        News currentNews = m_newsList.get(pos);
        viewHolder.m_headlineTextView.setText(currentNews.headline());
        viewHolder.m_sectionTextView.setText(currentNews.section());

        if (currentNews.author() == null) {
            viewHolder.m_authorTextView.setVisibility(View.GONE);
        } else {
            viewHolder.m_authorTextView.setVisibility(View.VISIBLE);
            viewHolder.m_authorTextView.setText(currentNews.author());
        }
        viewHolder.m_timeTextView.setText(timeDifference(formatDate(currentNews.timeInMilliseconds())));
        viewHolder.m_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newsUri = Uri.parse(currentNews.url());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (websiteIntent.resolveActivity(m_context.getPackageManager()) != null) {
                    m_context.startActivity(websiteIntent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return m_newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView m_cardView;
        private TextView m_headlineTextView;
        private TextView m_sectionTextView;
        private TextView m_contentTextView;
        private TextView m_authorTextView;
        private TextView m_timeTextView;
        private ImageView m_thumbnailImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            m_headlineTextView = itemView.findViewById(R.id.headline_text_view);
            m_sectionTextView = itemView.findViewById(R.id.section_text_view);
            m_authorTextView = itemView.findViewById(R.id.author_text_view);
            m_timeTextView = itemView.findViewById(R.id.time_text_view);
            m_thumbnailImageView = itemView.findViewById(R.id.card_image_view);
            m_cardView = itemView.findViewById(R.id.card_view);
        }
    }
    private String formatDate(String dateStringUTC) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        Date dateObj = null;
        try {
            dateObj = simpleDateFormat.parse(dateStringUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("d MMM, yyyy  h:mm a", Locale.ENGLISH);
        String formattedDateUTC = df.format(dateObj);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(formattedDateUTC);
            df.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    private static long getDateInMillis(String formattedDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("d MMM, yyyy  h:mm a");
        long dateMilliseconds;
        Date dateObj;
        try {
            dateObj = simpleDateFormat.parse(formattedDate);
            dateMilliseconds = dateObj.getTime();
            return dateMilliseconds;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem parsing date error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    private CharSequence timeDifference(String formattedDate) {
        long currentTime = System.currentTimeMillis();
        long publicationTime = getDateInMillis(formattedDate);
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS);
    }
    public void clearAll() {
        m_newsList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<News> newsList) {
        m_newsList.clear();
        m_newsList.addAll(newsList);
        notifyDataSetChanged();
    }
    private class DownloadThumbnailImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadThumbnailImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
