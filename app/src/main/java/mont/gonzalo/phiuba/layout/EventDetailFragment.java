package mont.gonzalo.phiuba.layout;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Event;

public class EventDetailFragment extends SearchableFragment implements Serializable {
    private static final String TAG = "EventDetailFragment";
    private static final String NEWS_KEY = "news_attribute";
    private Event mEvent;
    private TextView titleTextView;
    private TextView descTextView;
    private ImageView thumbnailView;

    public EventDetailFragment() {
    }

    public static EventDetailFragment newInstance(Event news) {
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setEvent(news);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NEWS_KEY, mEvent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mEvent = (Event) savedInstanceState.getSerializable(NEWS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        titleTextView = (TextView) view.findViewById(R.id.news_title);
        descTextView = (TextView) view.findViewById(R.id.news_text);
        thumbnailView = (ImageView) view.findViewById(R.id.news_thumbnail);
        titleTextView.setText(mEvent.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            descTextView.setText(Html.fromHtml(mEvent.getText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            descTextView.setText(Html.fromHtml(mEvent.getText()));
        }
        LayoutHelper.addImageToView(view.getContext(), mEvent.getImg(), thumbnailView);
        return view;
    }

    public void setEvent(Event event) {
        this.mEvent = event;
    }

    @Override
    public void updateResults(String query) {

    }

    @Override
    public void reset() {

    }

    @Override
    public SearchableFragment getResultsFragment() {
        return EventsFragment.newInstance(1, (EventsFragment.OnListFragmentInteractionListener) getActivity());
    }
}
