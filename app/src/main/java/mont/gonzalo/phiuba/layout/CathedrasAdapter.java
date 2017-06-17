package mont.gonzalo.phiuba.layout;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;

public class CathedrasAdapter extends RecyclerView.Adapter<CathedrasAdapter.CathedraViewHolder> {

    private final List<Cathedra> mCathedras;
    private final CourseDetailFragment.OnListFragmentInteractionListener mListener;
    private final Course mCourse;
    private int position;
    private Resources res;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CathedrasAdapter(List<Cathedra> cathedras, Course c, CourseDetailFragment.OnListFragmentInteractionListener mListener) {
        this.mCathedras = cathedras;
        this.mListener = mListener;
        this.mCourse = c;
    }

    public void updateItems(List<Cathedra> newItems) {
        mCathedras.clear();
        mCathedras.addAll(newItems);
    }

    @Override
    public CathedraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cathedra_row, parent, false);
        res = parent.getResources();
        return new CathedraViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final CathedraViewHolder holder, final int position) {
        holder.mItem = mCathedras.get(position);
        holder.teachers.setText(holder.mItem.getTeachers());
        holder.schedulesText.setText(holder.mItem.getSchedulesAsMultilineText(res));
        holder.seatsText.setText(res.getString(R.string.seats) + ": " + holder.mItem.getSeats());
//        holder.color.setBackgroundColor(res.getColor(holder.mItem.getColor(), null));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(200);
                    v.startAnimation(animation1);
                    mListener.onListFragmentInteraction(holder.mItem, mCourse.getName(), mCathedras.get(position).getTeachers());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCathedras.size();
    }

    public class CathedraViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View view;
        public final TextView teachers;
        public final TextView seatsText;
        public final TextView schedulesText;
        public final View color;
        public Cathedra mItem;

        public CathedraViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.cathedra_row);
            teachers = (TextView)itemView.findViewById(R.id.teachers);
            schedulesText = (TextView)itemView.findViewById(R.id.schedulesText);
            seatsText = (TextView)itemView.findViewById(R.id.seatsText);
            color = (View)itemView.findViewById(R.id.colorView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + teachers.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.news_context_title);
            menu.add(0, R.string.news_context_pin, 1, v.getResources().getString(R.string.news_context_pin));
        }
    }
}
