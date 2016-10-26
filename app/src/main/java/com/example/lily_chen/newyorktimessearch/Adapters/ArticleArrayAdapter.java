package com.example.lily_chen.newyorktimessearch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lily_chen.newyorktimessearch.Models.Article;
import com.example.lily_chen.newyorktimessearch.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lily_chen on 10/25/16.
 */
public class ArticleArrayAdapter extends
        RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder>{


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView image;
        @BindView(R.id.tvTitle) TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    Context context;
    List<Article> articles;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    private Context getContext() {
        return this.context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on your views and data model
        TextView tvTitle = viewHolder.title;
        tvTitle.setText(article.getHeadline());
        ImageView ivImage = viewHolder.image;

        // inflate the image
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return articles.size();
    }
}
