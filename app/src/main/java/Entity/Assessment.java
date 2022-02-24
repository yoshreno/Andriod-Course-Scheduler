package Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int assessmentId;
    private String title;
    private String startDate;
    private String endDate;
    private String type;
    private int courseId;

    public Assessment(String title, String startDate, String endDate, String type, int courseId) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "title='" + title + '\'' +
                '}';
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
