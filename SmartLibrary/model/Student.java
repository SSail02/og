package SmartLibrary.model;

/**
 * Student inherits the common properties of Person.
 */
public class Student extends Person {
    private int studentId;
    private String course;

    public Student(int studentId, String name, String course) {
        super(name);
        this.studentId = studentId;
        this.course = course;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return studentId + " - " + name + " (" + course + ")";
    }
}
