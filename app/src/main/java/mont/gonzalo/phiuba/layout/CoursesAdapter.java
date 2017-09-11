package mont.gonzalo.phiuba.layout;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private final List<Course> mCourses;
    private final OnListFragmentInteractionListener mListener;
    private int position;
    private boolean menuEnabled;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CoursesAdapter(List<Course> courses, OnListFragmentInteractionListener mListener) {
        mCourses = courses;
        menuEnabled = true;
        this.mListener = mListener;
    }

    public void updateItems(List<Course> newItems) {
        mCourses.clear();
        mCourses.addAll(newItems);
        this.notifyDataSetChanged();
    }

    public void disableMenu() {
        this.menuEnabled = false;
    }

    public void enableMenu() {
        this.menuEnabled = true;
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
        holder.sml.setSwipeEnable(menuEnabled);

        if (menuEnabled) {
            holder.sml.setSwipeListener(new CourseSwipeListener(holder));
            holder.sml.setOnClickListener(new View.OnClickListener() {
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
        } else {
            holder.sml.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                        animation1.setDuration(200);
                        v.startAnimation(animation1);
                        holder.itemView.setAlpha(0.7f);
                    }
                }
            });
        }
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

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void updateStatus(Course c) {
            status.setBackgroundColor(ActivityContext.get().getColor(
                    c.getColorId()));
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

                if (course.isAvailable()) {
                    showCalifDialog();
                    v.post(new Runnable() {
                        public void run() {
//                            showCalifDialog();
                        }
                    });
                } else {
                    Toast.makeText(ActivityContext.get(),
                            "Según tus materias aprobadas, no podrías cursar esta materia. Chequea que tengas aprobada/s: " + String.valueOf(course.getCorrelatives()),
                            Toast.LENGTH_LONG).show();
                }
                }
            });

            ImageView studying = (ImageView) v.findViewById(R.id.sml_action_studying);
            studying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserCourses.getInstance().isStudying(course)) {
                        Toast.makeText(ActivityContext.get(),
                                "Ya estás cursando " + course.getName(),
                                Toast.LENGTH_LONG).show();
                    } else if (course.isAvailable()) {
                        boolean success = UserCourses.getInstance().addStudying(course);
                        if (success) {
                            holder.updateAward(-1);
                            Toast.makeText(ActivityContext.get(),
                                    "Agregando " + course.getName() + " como estudiando.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ActivityContext.get(),
                                    ActivityContext.get().getString(R.string.cant_add_studying_1) +
                                            UserCourses.MAX_STUDYING_SIZE +
                                            ActivityContext.get().getString(R.string.cant_add_studying_2),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ActivityContext.get(),
                                ActivityContext.get().getString(R.string.add_not_available_course)
                                + String.valueOf(course.getCorrelatives()),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            ImageView star = (ImageView) v.findViewById(R.id.sml_action_remove);
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserCourses.getInstance().removeCourse(course);
                    holder.updateAward(-1);
                    holder.updateStatus(course);
                    Toast.makeText(ActivityContext.get(), "Removiendo " + course.getName(), Toast.LENGTH_LONG).show();
                }
            });
        }

        public void showCalifDialog() {
            final Dialog d = new Dialog(ActivityContext.get());
            d.setContentView(R.layout.calification_dialog);

            final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker);
            final CheckBox finalCheckBox = (CheckBox) d.findViewById(R.id.finalCheck);
            ImageButton bDone = (ImageButton) d.findViewById(R.id.buttonDone);
            ImageButton bCancel = (ImageButton) d.findViewById(R.id.buttonCancel);

            finalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        np.setVisibility(View.GONE);
                    } else {
                        np.setVisibility(View.VISIBLE);
                    }
                }
            });

            bDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (course != null) {
                        String successMessage;
                        if (finalCheckBox.isChecked()) {
                            successMessage = " como cursada aprobada, en final.";
                            UserCourses.getInstance().addApproved(course, null);
                        } else {
                            successMessage = " como aprobada con " + np.getValue();
                            holder.updateAward(np.getValue());
                            UserCourses.getInstance().addApproved(course, (double) np.getValue());
                        }
                        Toast.makeText(ActivityContext.get(),
                                "Agregando " + course.getName() + successMessage,
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
            try {
                d.show();
            } catch (WindowManager.BadTokenException e) {
                d.dismiss();
                Log.e("CoursesAdapter", "Bad token trying to show calification dialog.", e);
            }
        }
    }
}
