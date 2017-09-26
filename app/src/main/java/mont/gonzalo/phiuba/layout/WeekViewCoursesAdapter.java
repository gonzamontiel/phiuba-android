package mont.gonzalo.phiuba.layout;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

public class WeekViewCoursesAdapter extends RecyclerView.Adapter<WeekViewCoursesAdapter.CourseViewHolder> {

    private final List<Course> mCourses;
    private final OnListFragmentInteractionListener mListener;

    public WeekViewCoursesAdapter(List<Course> courses, OnListFragmentInteractionListener mListener) {
        mCourses = courses;
        this.mListener = mListener;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_row_simple, parent, false);
        return new CourseViewHolder(view);
    }

    public Course getCourse(int position) {
        return mCourses.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final CourseViewHolder holder, int position) {
        holder.mItem = mCourses.get(position);
        holder.selected = holder.mItem.isStudying();
        holder.changeSelectedColor(holder.selected ? R.color.course_added : R.color.course_default);
        holder.courseName.setText(holder.mItem.getName());
        holder.courseIcon.setImageResource(holder.mItem.getImageResource());
        holder.cardViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    holder.toggleSelection();
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public final TextView courseName;
        public final ImageView courseIcon;
        public final View selectedIndicator;
        public Course mItem;
        private boolean selected;
        public CardView cardViewItem;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public CourseViewHolder(View itemView) {
            super(itemView);
            cardViewItem = (CardView) itemView.findViewById(R.id.course_card_simple);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            courseIcon = (ImageView)itemView.findViewById(R.id.course_icon);
            selectedIndicator = itemView.findViewById(R.id.selected_indicator);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + courseName.getText() + "'";
        }

        private Animation  fadeIn() {
            Animation animation = new AlphaAnimation(0.3f, 0.7f);
            animation.setDuration(100);
            return animation;
        }

        private Animation fadeOut() {
            Animation animation = new AlphaAnimation(0.7f, 0.3f);
            animation.setDuration(100);
            return animation;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void changeSelectedColor(int colorId) {
            selectedIndicator.setBackgroundColor(ActivityContext.get().getColor(colorId));
        }

        public void toggleSelection() {
            if (selected) {
                itemView.startAnimation(fadeIn());
                changeSelectedColor(R.color.course_default);
            } else {
                itemView.startAnimation(fadeOut());
                changeSelectedColor(R.color.course_added);
            }
            selected = !selected;
        }
    }
}
