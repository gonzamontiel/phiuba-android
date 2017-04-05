package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Event;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.ContentValues.TAG;

public class EventsFragment extends SearchableFragment implements Serializable {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private transient OnListFragmentInteractionListener mListListener;
    private transient EventsRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventsFragment() {
    }

    public static EventsFragment newInstance(int columnCount, OnListFragmentInteractionListener listener) {
        EventsFragment fragment = new EventsFragment();
        fragment.setListener(listener);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mListListener = (OnListFragmentInteractionListener) getActivity();
            DataFetcher.getInstance().getEvents(new Callback<List<Event>>() {
                @Override
                public void success(List<Event> events, Response response) {
                    Log.d(TAG, String.valueOf(events.get(0).getParsedDate()));
                    if (events.size() > 0) {
                        mAdapter = new EventsRecyclerViewAdapter(events, mListListener);
                        recyclerView.setAdapter(mAdapter);
                        mListListener = (EventsFragment.OnListFragmentInteractionListener) getActivity();
                        registerForContextMenu(recyclerView);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListListener = null;
    }

    public void setListener(OnListFragmentInteractionListener listener) {
        this.mListListener = listener;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((EventsRecyclerViewAdapter)mAdapter).getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.string.news_context_pin:
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void updateResults(String query) {
        DataFetcher.getInstance().searchEvents(query, new UpdateCallback());
    }

    @Override
    public void reset() {
        DataFetcher.getInstance().getEvents(new UpdateCallback());
    }

    @Override
    public SearchableFragment getResultsFragment() {
        return this;
    }

    private class UpdateCallback implements Callback<List<Event>> {
        @Override
        public void success(List<Event> events, Response response) {
            if (mAdapter != null) {
                mAdapter.updateItems(events);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, error.getMessage());
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event item);
    }
}
