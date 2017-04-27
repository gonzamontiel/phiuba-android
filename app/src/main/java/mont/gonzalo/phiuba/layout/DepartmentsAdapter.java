package mont.gonzalo.phiuba.layout;

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
import mont.gonzalo.phiuba.layout.DepartmentsFragment.OnListFragmentInteractionListener;
import mont.gonzalo.phiuba.model.Department;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentViewHolder> {

    private final List<Department> mDepartments;
    private final OnListFragmentInteractionListener mListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public DepartmentsAdapter(List<Department> Departments, OnListFragmentInteractionListener mListener) {
        mDepartments = Departments;
        this.mListener = mListener;
    }

    public void updateItems(List<Department> newItems) {
        mDepartments.clear();
        mDepartments.addAll(newItems);
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_department_row, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DepartmentViewHolder holder, int position) {
        holder.mItem = mDepartments.get(position);
        holder.departmentName.setText(mDepartments.get(position).getName());
        holder.departmentDesc.setText(mDepartments.get(position).getDescription());
        holder.departmentIcon.setImageResource(mDepartments.get(position).getImageResource());

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
        return mDepartments.size();
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        public final View rv;
        public final TextView departmentName;
        public final TextView departmentDesc;
        public final ImageView departmentIcon;
        public Department mItem;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.department_card_view);
            departmentName = (TextView)itemView.findViewById(R.id.department_name);
            departmentDesc = (TextView)itemView.findViewById(R.id.department_description);
            departmentIcon = (ImageView)itemView.findViewById(R.id.department_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + departmentName.getText() + "'";
        }
    }
}
