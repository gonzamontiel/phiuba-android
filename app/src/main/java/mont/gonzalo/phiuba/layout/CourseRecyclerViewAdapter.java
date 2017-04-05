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
import mont.gonzalo.phiuba.layout.CoursesFragment.OnListFragmentInteractionListener;
import mont.gonzalo.phiuba.model.Course;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.CourseViewHolder> {

    private final List<Course> mCourses;
    private final OnListFragmentInteractionListener mListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CourseRecyclerViewAdapter(List<Course> courses, OnListFragmentInteractionListener mListener) {
        mCourses = courses;
        this.mListener = mListener;
    }

    public void updateItems(List<Course> newItems) {
        mCourses.clear();
        mCourses.addAll(newItems);
        this.notifyDataSetChanged();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_course_row, parent, false);
        return new CourseViewHolder(view);
    }

    public Course getCourse(int position) {
        return mCourses.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final CourseViewHolder holder, int position) {
        holder.mItem = mCourses.get(position);
        holder.courseName.setText(mCourses.get(position).getName());
        holder.courseDepartment.setText(mCourses.get(position).getDepto());
        holder.courseIcon.setImageResource(mCourses.get(position).getImageResource());
        holder.status.setBackgroundColor(ActivityContext.get().getResources().getColor(
                mCourses.get(position).getColorId(), null
        ));

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
        return mCourses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View rv;
        public final TextView courseName;
        public final TextView courseDepartment;
        public final ImageView courseIcon;
        public final View status;
        public Course mItem;

        public CourseViewHolder(View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.course_card_view);
            rv.setOnCreateContextMenuListener(this);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            courseDepartment = (TextView)itemView.findViewById(R.id.course_description);
            courseIcon = (ImageView)itemView.findViewById(R.id.course_icon);
            status = itemView.findViewById(R.id.status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + courseName.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.course_context_title);
            menu.add(0, R.string.course_context_favourite, 1, v.getResources().getString(R.string.course_context_favourite));
            menu.add(0, R.string.course_context_approved, 1, v.getResources().getString(R.string.course_context_approved));
            menu.add(0, R.string.course_context_studying, 1, v.getResources().getString(R.string.course_context_studying));
            menu.add(0, R.string.course_context_schedule, 2, v.getResources().getString(R.string.course_context_schedule));
            menu.add(0, R.string.course_context_department, 3, v.getResources().getString(R.string.course_context_department));
        }
    }
}
