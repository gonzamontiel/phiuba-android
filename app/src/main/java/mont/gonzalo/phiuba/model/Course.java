package mont.gonzalo.phiuba.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gonzalo Montiel on 10/10/16.
 */
public class Course implements Serializable {
    public static final String COURSE_DODE_TAG = "course_code";
    private String code;
    private String depCode;
    private String planCode;
    private String name;
    private String link;
    private String depto;
    private Boolean required;
    private List<String> correlatives;
    private List<Cathedra> cathedras;
    private boolean isComplete;

    public Course(String cCode, String cName) {
        this.planCode = User.get().getPlanCode();
        this.name = cName;
        this.code = cCode;
        this.isComplete = false;
    }

    public Course(String name, String depCode, String code, String depto) {
        this.name = name;
        this.code = code;
        this.depCode = depCode;
        this.depto = depto;
        this.isComplete = false;
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

    public boolean isAvailable(User user) {
        return true;
    }

    public int getColorId() {
        return isAvailable(User.get())? CourseStatus.getByStatus(CourseStatus.AVAILABLE) :
                CourseStatus.getByStatus(CourseStatus.NOT_AVAILABLE);
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
}
