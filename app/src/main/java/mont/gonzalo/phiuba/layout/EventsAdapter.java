package mont.gonzalo.phiuba.layout;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.layout.EventsFragment.OnListFragmentInteractionListener;
import mont.gonzalo.phiuba.model.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private final List<Event> mEvents;
    private final OnListFragmentInteractionListener mListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public EventsAdapter(List<Event> event, OnListFragmentInteractionListener mListener) {
        mEvents = event;
        this.mListener = mListener;
    }

    public void updateItems(List<Event> newItems) {
        mEvents.clear();
        mEvents.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events_row, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        holder.mItem = mEvents.get(position);
        holder.eventTitle.setText(mEvents.get(position).getTitle());
        holder.eventText.setText(mEvents.get(position).getText());

        LayoutHelper.addImageToView(holder.rv.getContext(), mEvents.get(position).getImg(), holder.eventThumbnail);

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
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View rv;
        public final TextView eventTitle;
        public final TextView eventText;
        public final TextView eventDate;
        public final ImageView eventThumbnail;
        public Event mItem;

        public EventViewHolder(View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.events_card_view);
            rv.setOnCreateContextMenuListener(this);
            eventThumbnail = (ImageView)itemView.findViewById(R.id.events_thumbnail);
            eventTitle = (TextView)itemView.findViewById(R.id.events_title);
            eventText = (TextView)itemView.findViewById(R.id.events_text);
            eventDate = (TextView)itemView.findViewById(R.id.events_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventTitle.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
