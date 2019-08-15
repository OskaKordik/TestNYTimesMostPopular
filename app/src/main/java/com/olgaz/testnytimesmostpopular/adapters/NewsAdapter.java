package com.olgaz.testnytimesmostpopular.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olgaz.testnytimesmostpopular.R;
import com.olgaz.testnytimesmostpopular.model.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsNews;
    private OnNewsClickListener onNewsClickListener;

    public interface OnNewsClickListener {
        void onNewsClick(int position);
    }

    public void setOnNewsClickListener(OnNewsClickListener onNewsClickListener) {
        this.onNewsClickListener = onNewsClickListener;
    }

    public List<News> getNewsNews() {
        return newsNews;
    }

    public void setNewsNews(List<News> newsNews) {
        this.newsNews = newsNews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newsNews.size();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        News news = newsNews.get(i);
        newsViewHolder.title.setText(news.getTitle());
        newsViewHolder.description.setText(news.getDescription());
        newsViewHolder.publishedDate.setText(news.getPublishedDate());
        newsViewHolder.section.setText(news.getSection());

        //получение url изображения и ширины (1 - формат "mediumThreeByTwo210")
        String urlImage = news.getMedia().get(0).getMediaMetadata().get(1).getUrl();
        int urlImageWidth = news.getMedia().get(0).getMediaMetadata().get(1).getWidth();

        newsViewHolder.imageNews.setMaxWidth(urlImageWidth);

        //загрузка изображения с использованием библиотеки Picasso
        Picasso.get().load(urlImage).into(newsViewHolder.imageNews);
    }


    class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView publishedDate;
        private TextView section;
        private ImageView imageNews;

        public NewsViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            publishedDate = itemView.findViewById(R.id.published_date);
            section = itemView.findViewById(R.id.textViewSection);
            imageNews = itemView.findViewById(R.id.imageNews);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNewsClickListener != null) onNewsClickListener.onNewsClick(getAdapterPosition());
                }
            });
        }
    }
}
