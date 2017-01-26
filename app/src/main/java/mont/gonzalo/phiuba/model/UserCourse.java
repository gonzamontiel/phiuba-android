package mont.gonzalo.phiuba.model;

public class UserCourse {
    private Course course;
    private User user;
    private final CourseStatus status;

    public UserCourse(Course course, User user, CourseStatus status) {
        this.course = course;
        this.user = user;
        this.status = status;
    }
}
