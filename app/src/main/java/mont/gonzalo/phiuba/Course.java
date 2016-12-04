package mont.gonzalo.phiuba;

import java.util.List;

/**
 * Created by gonzalo on 10/10/16.
 */
public class Course {
    public static final String KEY_COURSE_CODE = "key_course_code";
    public static final String KEY_COURSE_NAME = "key_course_name";
    public static final String KEY_COURSE_LINK = "key_course_link";

    private String code;
    private String depCode;
    private String planCode;
    private String name;
    private String indexableTokens;
    private String link;
    private String depto;
    private Boolean required;
    private List<String> correlatives;

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

    public String getIndexableTokens() {
        return indexableTokens;
    }

    public void setIndexableTokens(String indexableTokens) {
        this.indexableTokens = indexableTokens;
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
}
