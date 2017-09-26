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
import mont.gonzalo.phiuba.model.News;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewsFragment extends SearchableFragment implements Serializable {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private transient OnListFragmentInteractionListener mListListener;
    private transient NewsAdapter mAdapter;
    private transient RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {}

    public static NewsFragment newInstance(int columnCount, OnListFragmentInteractionListener listener) {
        NewsFragment fragment = new NewsFragment();
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
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DataFetcher.getInstance().getNews(new InsertCallback());
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
            position = ((NewsAdapter)mAdapter).getPosition();
        } catch (Exception e) {
            Log.d("NewsFragment", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.string.news_context_pin:
        }
        return super.onContextItemSelected(item);
    }

    public void updateResults(String query) {
        DataFetcher.getInstance().searchNews(query, mAdapter != null? new UpdateCallback() : new InsertCallback());
    }

    public void reset() {
        DataFetcher.getInstance().getNews(new UpdateCallback());
    }

    @Override
    public SearchableFragment getResultsFragment() {
        return this;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(News item);
    }

    private class UpdateCallback implements Callback<List<News>> {
        @Override
        public void success(List<News> newses, Response response) {
            if (mAdapter != null) {
                mAdapter.updateItems(newses);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(DataFetcher.TAG, error.getMessage());
        }
    }

    private class InsertCallback implements Callback<List<News>> {
        @Override
        public void success(List<News> newses, Response response) {
            if (newses.size() > 0) {
                mAdapter = new NewsAdapter(newses, mListListener);
                recyclerView.setAdapter(mAdapter);
                mListListener = (NewsFragment.OnListFragmentInteractionListener) getActivity();
                registerForContextMenu(recyclerView);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(DataFetcher.TAG, error.getMessage());
        }
    }
}
