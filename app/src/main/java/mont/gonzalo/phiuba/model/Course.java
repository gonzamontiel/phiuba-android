package mont.gonzalo.phiuba.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mont.gonzalo.phiuba.R;
import mont.gonzalo.phiuba.api.DataFetcher;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public class Course implements Serializable {
    private static final int DEFAULT_CREDITS = 6;
    private String code;
    private String depCode;
    private String planCode;
    private String name;
    private String link;
    private String depto;
    private Boolean required;
    private int credits;
    private List<String> correlatives;
    private List<Cathedra> cathedras;

    public Course(String cCode, String cName) {
        this.planCode = User.get().getPlanCode();
        this.name = cName;
        this.code = cCode;
    }

    public Course(String name, String depCode, String code, String depto) {
        this.name = name;
        this.code = code;
        this.depCode = depCode;
        this.depto = depto;
    }

    public List<Cathedra> getCathedras() {
        return cathedras;
    }

    public void setCathedras(List<Cathedra> cathedras) {
        this.cathedras = cathedras;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<String> getCorrelatives() {
        return correlatives;
    }

    public void setCorrelatives(List<String> correlatives) {
        this.correlatives = correlatives;
    }

    public int getImageResource() {
        return Department.getIconByDepartmentCode(this.getDepCode());
    }

    public boolean isAvailable() {
        return UserCourses.getInstance().isAvailable(this);
    }

    public boolean isApproved() {
        return UserCourses.getInstance().isApproved(this);
    }

    private boolean isStudying() {
        return UserCourses.getInstance().isStudying(this);
    }

    private boolean isFinalExamPending() {
        return UserCourses.getInstance().isFinalExamPending(this);
    }

    public int getColorId() {
        CourseStatus status = CourseStatus.NOT_AVAILABLE;
        if (isApproved()) {
            status = CourseStatus.APPROVED;
        } else if (isStudying()) {
            status = CourseStatus.STUDYING;
        } else if (isFinalExamPending()) {
            status = CourseStatus.EXAM_PENDING;
        } else if (isAvailable()) {
            status = CourseStatus.AVAILABLE;
        }
        return CourseStatus.getByStatus(status);
    }

    @Override
    public boolean equals(Object obj) {
        Course course = (Course) obj;
        return this.getCode() == course.getCode() && this.getPlanCode() == course.getPlanCode();
    }

    static private List<Course> sample_data;

    public static List<Course> getSampleData() {
        if (sample_data == null) {
            List<Course> cc = new ArrayList<>();
            cc.add(new Course("Analisis Matematico II A", "61", "61.03", "Matemática"));
            cc.add(new Course("Algoritmos y Programacion I", "75", "75.40", "Computación"));
            cc.add(new Course("Algebra II A", "61", "61.08", "Matemática"));
            cc.add(new Course("Fisica II A", "62", "62.03", "Física"));
            cc.add(new Course("Algoritmos y Programacion II", "75", "75.41", "Computación"));
            cc.add(new Course("Fisica III D", "62", "62.15", "Física"));
            cc.add(new Course("Laboratorio", "66", "66.02", "Electrónica"));
            cc.add(new Course("Estructura del Computador", "66", "66.70", "Electrónica"));
            cc.add(new Course("Algoritmos y Programacion III", "75", "75.07", "Computación"));
            cc.add(new Course("Analisis Numerico I", "75", "75.12", "Computación"));
            cc.add(new Course("Probabilidad y Estadistica B", "61", "61.09", "Matemática"));
            cc.add(new Course("Analisis Matematico III A", "61", "61.10", "Matemática"));
            cc.add(new Course("Organizacion de Computadoras", "66", "66.20", "Electrónica"));
            cc.add(new Course("Organizacion de Datos", "75", "75.06", "Computación"));
            cc.add(new Course("Taller de Programacion I", "75", "75.42", "Computación"));
            cc.add(new Course("Estructura de las Organizaciones", "71", "71.12", "Gestión"));
            cc.add(new Course("Modelos y Optimizacion I", "71", "71.14", "Gestión"));
            cc.add(new Course("Sistemas Operativos", "75", "75.08", "Computación"));
            cc.add(new Course("Analisis de la Informacion", "75", "75.09", "Computación"));
            cc.add(new Course("Tecnicas de Diseño", "75", "75.10", "Computación"));
            sample_data = cc;
        }
        return sample_data;
    }

    public int getCredits() {
        return credits > 0 ? credits : DEFAULT_CREDITS;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void loadCathedrasAsync() {
        DataFetcher.getInstance().getCathedras(getCode(), new Callback<List<Cathedra>>() {
            @Override
            public void success(List<Cathedra> cathedrasList, Response response) {
                Log.d("cathedras list for " +  getName(), String.valueOf(cathedrasList));
                if (cathedrasList.size() > 0) {
                    cathedras = cathedrasList;
                }
                UserCourses.getInstance().saveToSharedPrefs();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public int getApprovedOrNotIcon() {
        return isApproved() ? R.drawable.tick : R.drawable.cross;
    }

    public String getLongName() {
        return getCode() + " - " + getName();
    }

    public static class ComparatorByName implements Comparator<Course> {
        @Override
        public int compare(Course o1, Course o2) {
            return o1.getName().compareTo(o2.getName());
        }
   }
}
