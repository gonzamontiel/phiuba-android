package mont.gonzalo.phiuba.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import me.grantland.widget.AutofitHelper;
import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.model.Cathedra;
import mont.gonzalo.phiuba.model.Course;
import mont.gonzalo.phiuba.model.Department;
import mont.gonzalo.phiuba.model.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DepartmentDetailFragment extends SearchableFragment implements Serializable {
    private static final String TAG = "DepartmentDetailFragment";
    private transient OnListFragmentInteractionListener mListListener;
    private transient TextView nameTextView;
    private transient ImageView deptoIcon;
    private transient RecyclerView coursesView;
    private transient TextView availableCoursesTextView;
    private transient TextView codeTextView;
    private transient TextView contactInfoTextView;
    private transient TextView mailtoTextView;
    private transient TextView caTeachersTextView;
    private transient TextView caAuxTextView;
    private transient TextView caGraduatedTextView;
    private transient TextView caStudentsTextView;
    private Department mDepartment;

    public DepartmentDetailFragment() {
    }

    public static DepartmentDetailFragment newInstance(OnListFragmentInteractionListener mListListener,
                                                       Department department) {
        DepartmentDetailFragment fragment = new DepartmentDetailFragment();
        fragment.setListListener(mListListener);
        fragment.setDepartment(department);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("department", mDepartment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDepartment = (Department) savedInstanceState.getSerializable("department");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_department_detail, container, false);

        nameTextView = (TextView) view.findViewById(R.id.dept_name);
        deptoIcon = (ImageView) view.findViewById(R.id.icon);
        deptoIcon.setImageResource(mDepartment.getImageResource());

        coursesView = (RecyclerView) view.findViewById(R.id.courses_view);
        availableCoursesTextView = (TextView) view.findViewById(R.id.availableCourses);

        codeTextView = (TextView) view.findViewById(R.id.code);
        contactInfoTextView = (TextView) view.findViewById(R.id.contactInfo);
        mailtoTextView = (TextView) view.findViewById(R.id.mailto);
        caTeachersTextView = (TextView) view.findViewById(R.id.teachers);
        caAuxTextView = (TextView) view.findViewById(R.id.aux);
        caGraduatedTextView = (TextView) view.findViewById(R.id.graduated);
        caStudentsTextView = (TextView) view.findViewById(R.id.students);

        nameTextView.setText(mDepartment.getName());
        AutofitHelper.create(nameTextView);
        codeTextView.setText(mDepartment.getCode() + (mDepartment.getAltCode().isEmpty()? "" : " (" + mDepartment.getAltCode()+ " para nuevos planes)"));
        LayoutHelper.setTextViewHTML(contactInfoTextView, mDepartment.getContacto(), getActivity());
        mailtoTextView.setText(mDepartment.getMailto());
        caTeachersTextView.setText(mDepartment.getDocentesConsejo());
        caAuxTextView.setText(mDepartment.getAuxiliaresConsejo());
        caGraduatedTextView.setText(mDepartment.getGraduadosConsejo());
        caStudentsTextView.setText(mDepartment.getAlumnosConsejo());

        DataFetcher.getInstance().getCoursesByDepartment(mDepartment.getCode(), User.get().getPlanCode(), new Callback<List<Course>>() {
            @Override
            public void success(List<Course> courses, Response response) {
                Set<Course> hs = new LinkedHashSet<Course>();
                hs.addAll(courses);
                courses.clear();
                courses.addAll(hs);
                RecyclerView.Adapter adapter = new CoursesAdapter(courses,
                        (CoursesFragment.OnListFragmentInteractionListener) getActivity());
                coursesView.setAdapter(adapter);
                availableCoursesTextView.setText(courses.size() + " materias");
                availableCoursesTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Retrofit error", (String) error.getBody());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListListener = null;
    }

    public void setListListener(OnListFragmentInteractionListener listListener) {
        this.mListListener = listListener;
    }

    public void setDepartment(Department department) {
        this.mDepartment = department;
    }

    @Override
    public void updateResults(String query) {}

    @Override
    public void reset() {}

    @Override
    public SearchableFragment getResultsFragment() {
        return DepartmentsFragment.newInstance(1, (DepartmentsFragment.OnListFragmentInteractionListener) getActivity());
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Department department);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Cathedra item, String courseName, String teachers);
    }
}
