package mont.gonzalo.phiuba.layout;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;
import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.listener.SimpleSwipeSwitchListener;

import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.layout.CoursesFragment.OnListFragmentInteractionListener;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.UserCourses;

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
        holder.courseName.setText(holder.mItem.getName());
        holder.courseDepartment.setText(holder.mItem.getDepto());
        holder.courseIcon.setImageResource(holder.mItem.getImageResource());
        holder.status.setBackgroundColor(ActivityContext.get().getResources().getColor(
                holder.mItem.getColorId(), null
        ));

        double calif = UserCourses.getInstance().getCalification(holder.mItem);
        holder.updateAward(calif);

        holder.sml.setSwipeListener(new CourseSwipeListener(holder));
        holder.sml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "click");
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
        return mCourses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public final View rv;
        public final TextView courseName;
        public final TextView courseDepartment;
        public final ImageView courseIcon;
        public final View status;
        public final View award;
        public final TextView awardCalification;
        public final ImageView awardIcon;
        public Course mItem;
        public SwipeMenuLayout sml;

        public CourseViewHolder(View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.course_card_view);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            courseDepartment = (TextView)itemView.findViewById(R.id.course_description);
            courseIcon = (ImageView)itemView.findViewById(R.id.course_icon);
            status = itemView.findViewById(R.id.status);
            award = itemView.findViewById(R.id.course_award_container);
            awardCalification = (TextView) itemView.findViewById(R.id.course_award_calif);
            awardIcon = (ImageView)itemView.findViewById(R.id.course_award);
            sml = (SwipeMenuLayout) itemView.findViewById(R.id.sml);
        }

        public void updateAward(double calif) {
            if (calif > 0) {
                if (calif  >= 4 && calif  < 6) {
                    awardIcon.setImageResource(R.drawable.bronce);
                } else if (calif >= 6 && calif < 8) {
                    awardIcon.setImageResource(R.drawable.plata);
                } else {
                    awardIcon.setImageResource(R.drawable.oro);
                }
                award.setVisibility(View.VISIBLE);
                awardCalification.setVisibility(View.VISIBLE);
                awardCalification.setText(String.valueOf(Math.round(calif)));
            } else {
                award.setVisibility(View.GONE);
                awardCalification.setVisibility(View.GONE);
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + courseName.getText() + "'";
        }
    }

    private class CourseSwipeListener extends SimpleSwipeSwitchListener {
        private Course course;
        private CourseViewHolder holder;

        public CourseSwipeListener(CourseViewHolder holder) {
            this.course = holder.mItem;
            this.holder = holder;
        }

        @Override
        public void endMenuOpened(SwipeMenuLayout swipeMenuLayout) {
            View v = swipeMenuLayout.findViewById(R.id.smMenuViewRight);
            ImageView  done = (ImageView) v.findViewById(R.id.sml_action_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.post(new Runnable() {
                        public void run() {
                            showCalifDialog();
                        }
                    });
                }
            });

            ImageView studying = (ImageView) v.findViewById(R.id.sml_action_studying);
            studying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserCourses.getInstance().addStudying(course);
                    holder.updateAward(-1);
                    Toast.makeText(ActivityContext.get(), "Agregando " + course.getName() + " como estudiando.", Toast.LENGTH_LONG).show();
                }
            });

            ImageView star = (ImageView) v.findViewById(R.id.sml_action_star);
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserCourses.getInstance().addFavourite(course);
                    holder.updateAward(-1);
                    Toast.makeText(ActivityContext.get(), "Agregando " + course.getName() + " como favorita.", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void showCalifDialog() {
            final Dialog d = new Dialog(ActivityContext.get());
            d.setContentView(R.layout.calification_dialog);

            ImageButton bDone = (ImageButton) d.findViewById(R.id.buttonDone);
            ImageButton bCancel = (ImageButton) d.findViewById(R.id.buttonCancel);

            final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker);
            bDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (course != null) {
                        holder.updateAward(np.getValue());
                        UserCourses.getInstance().addApproved(course, (double) np.getValue());
                        Toast.makeText(ActivityContext.get(),
                                "Agregando " + course.getName() + " como aprobada con " + np.getValue(),
                                Toast.LENGTH_LONG).show();
                    }
                    d.dismiss();
                }
            });
            bCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            d.show();
        }

    }
}
