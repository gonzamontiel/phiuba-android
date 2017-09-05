package mont.gonzalo.phiuba.layout;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.News;

public class NewsDetailFragment extends SearchableFragment implements View.OnTouchListener, Serializable {
    private static final String TAG = "NewsDetailFragment";
    private static final String NEWS_KEY = "news_attribute";
    private News mNews;
    private transient TextView titleTextView;
    private transient TextView descTextView;
    private transient ImageView thumbnailView;
    private transient ScaleGestureDetector scaleGestureDetector;

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
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new NewsOnScaleGestureListener());
        if (savedInstanceState != null) {
            mNews = (News) savedInstanceState.getSerializable(NEWS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, "PARENT TOUCH");
                v.findViewById(R.id.news_text).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        titleTextView = (TextView) view.findViewById(R.id.news_title);
        descTextView = (TextView) view.findViewById(R.id.news_text);
        descTextView.setOnTouchListener(this);
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
    public void updateResults(String query) {

    }

    @Override
    public void reset() {

    }

    @Override
    public SearchableFragment getResultsFragment() {
        return NewsFragment.newInstance(1, (NewsFragment.OnListFragmentInteractionListener) getActivity());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.scaleGestureDetector.onTouchEvent(event);
    }

    public class NewsOnScaleGestureListener extends SimpleOnScaleGestureListener {
        private float MIN = 40;
        private float MAX = 90;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float size = descTextView.getTextSize();
            float factor = detector.getScaleFactor();
            float product = size * factor;
            if (product > MIN && product < MAX) {
                descTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
            }
            return true;
        }
    }
}
