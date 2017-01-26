package mont.gonzalo.phiuba.UI;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mont.gonzalo.phiuba.UI.NewsFragment.OnListFragmentInteractionListener;
import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.News;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private final List<News> mNews;
    private final OnListFragmentInteractionListener mListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public NewsRecyclerViewAdapter(List<News> news, OnListFragmentInteractionListener mListener) {
        mNews = news;
        this.mListener = mListener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_news_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        holder.mItem = mNews.get(position);
        holder.newsTitle.setText(mNews.get(position).getTitle());
        holder.newsText.setText(mNews.get(position).getText());

        Picasso.with(holder.rv.getContext()).load(mNews.get(position).getImg()).into(holder.newsThumbnail);

        holder.rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(200);
                    v.startAnimation(animation1);
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View rv;
        public final TextView newsTitle;
        public final TextView newsText;
        public final ImageView newsThumbnail;
        public News mItem;

        public NewsViewHolder(View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.news_card_view);
            rv.setOnCreateContextMenuListener(this);
            newsThumbnail = (ImageView)itemView.findViewById(R.id.news_thumbnail);
            newsTitle = (TextView)itemView.findViewById(R.id.news_title);
            newsText = (TextView)itemView.findViewById(R.id.news_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + newsTitle.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.news_context_title);
            menu.add(0, R.string.news_context_pin, 1, v.getResources().getString(R.string.news_context_pin));
        }
    }
}
