package mont.gonzalo.phiuba.layout;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.model.Course;

public class MiniCoursesAdapter extends RecyclerView.Adapter<MiniCoursesAdapter.ViewHolder> {
    private final List<Course> mCourses;
    private final OnCorrelativeInteractionListener  mListener;

    public MiniCoursesAdapter(List<Course> courses, OnCorrelativeInteractionListener  mListener) {
        mCourses = courses;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_row_mini, parent, false);
        return new ViewHolder(listItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MiniCoursesAdapter.ViewHolder holder, int position) {
        Course course = mCourses.get(position);
        holder.name.setText(course.getLongName());
        holder.icon.setImageResource(course.getApprovedOrNotIcon());
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.course_name);
            icon = (ImageView) itemView.findViewById(R.id.course_icon);
        }
    }

    public interface OnCorrelativeInteractionListener {
        void onCorrelativeInteraction(Course item);
    }
}