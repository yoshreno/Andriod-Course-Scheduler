package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.util.List;

import Database.Repository;
import Entity.Assessment;
import Entity.Course;
import Utility.MyDatePicker;

public class AddCourse extends AppCompatActivity {

    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    EditText editStatus;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;
    EditText editNotes;
    private int nextID;
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repo = new Repository(getApplication());
        nextID = this.getNextID();

        editTitle = findViewById(R.id.titleEdit);
        editStart = findViewById(R.id.startEdit);
        editEnd = findViewById(R.id.endEdit);
        editStatus = findViewById(R.id.statusEdit);
        editInstructorName = findViewById(R.id.instructorName);
        editInstructorPhone = findViewById(R.id.instructorPhone);
        editInstructorEmail = findViewById(R.id.instructorEmail);
        editNotes = findViewById(R.id.notes);

        MyDatePicker myDatePicker = new MyDatePicker(editStart, editEnd, AddCourse.this);
        myDatePicker.setStartDatePicker();
        myDatePicker.setEndDatePicker();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {
        if(editTitle.getText().toString().isEmpty()) {
            Toast.makeText(AddCourse.this, "Please enter the course title.", Toast.LENGTH_LONG).show();
        }
        else {
            Course course = new Course(nextID, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(),
                    editStatus.getText().toString(), editInstructorName.getText().toString(), editInstructorPhone.getText().toString(),
                    editInstructorEmail.getText().toString(), editNotes.getText().toString(), 0);
            repo.insertCourse(course);

            CourseList.courses.add(course);
            CourseList.adapter.notifyItemInserted(CourseList.courses.size());
            this.finish();
        }
    }

    private int getNextID(){
        List<Course> courses = repo.getAllCourses();
        if(courses.size() == 0)
            nextID = 1;
        else
            nextID = courses.get(courses.size() - 1).getCourseId() + 1;

        return nextID;
    }
}