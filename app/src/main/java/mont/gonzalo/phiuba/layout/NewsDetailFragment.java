package mont.gonzalo.phiuba.layout;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.News;

public class NewsDetailFragment extends SearchableFragment implements ScaleGestureDetector.OnScaleGestureListener, Serializable {
    private static final String TAG = "NewsDetailFragment";
    private static final String NEWS_KEY = "news_attribute";
    private News mNews;
    private transient TextView titleTextView;
    private transient TextView descTextView;
    private transient ImageView thumbnailView;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(News news) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setNews(news);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NEWS_KEY, mNews);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNews = (News) savedInstanceState.getSerializable(NEWS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        titleTextView = (TextView) view.findViewById(R.id.news_title);
        descTextView = (TextView) view.findViewById(R.id.news_text);
        thumbnailView = (ImageView) view.findViewById(R.id.news_thumbnail);
        titleTextView.setText(mNews.getTitle());
        LayoutHelper.setTextViewHTML(descTextView, mNews.getText(), getActivity());
        LayoutHelper.addImageToView(view.getContext(), mNews.getImg(), thumbnailView);
        return view;
    }

    public void setNews(News news) {
        this.mNews = news;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float size = descTextView.getTextSize();
        Log.d("TextSizeStart", String.valueOf(size));

        float factor = detector.getScaleFactor();
        Log.d("Factor", String.valueOf(factor));


        float product = size*factor;
        Log.d("TextSize", String.valueOf(product));
        descTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);

        size = descTextView.getTextSize();
        Log.d("TextSizeEnd", String.valueOf(size));
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public void updateResults(String query) {

    }

    @Override
    public void reset() {

    }

    @Override
    public SearchableFragment getResultsFragment() {
        return NewsFragment.newInstance(1, (NewsFragment.OnListFragmentInteractionListener) getActivity());
    }
}
