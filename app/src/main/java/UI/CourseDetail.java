package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.c196_pa.R;

import Database.Repository;
import Entity.Course;
import Entity.Term;

public class CourseDetail extends AppCompatActivity {

    int id;
    String title;
    String start;
    String end;
    String status;
    String instructorName;
    String instructorPhone;
    String instructorEmail;
    String notes;
    int termId;
    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    EditText editStatus;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;
    EditText editNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        editTitle = findViewById(R.id.titleEdit);
        editStart = findViewById(R.id.startEdit);
        editEnd = findViewById(R.id.endEdit);
        editStatus = findViewById(R.id.statusEdit);
        editInstructorName = findViewById(R.id.instructorName);
        editInstructorPhone = findViewById(R.id.instructorPhone);
        editInstructorEmail = findViewById(R.id.instructorEmail);
        editNotes = findViewById(R.id.notes);

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("startDate");
        end = getIntent().getStringExtra("endDate");
        status = getIntent().getStringExtra("status");
        instructorName = getIntent().getStringExtra("instructorName");
        instructorPhone = getIntent().getStringExtra("instructorPhone");
        instructorEmail = getIntent().getStringExtra("instructorEmail");
        notes = getIntent().getStringExtra("notes");
        termId = getIntent().getIntExtra("termId", -1);

        editTitle.setText(title);
        editStart.setText(start);
        editEnd.setText(end);
        editStatus.setText(status);
        editInstructorName.setText(instructorName);
        editInstructorPhone.setText(instructorPhone);
        editInstructorEmail.setText(instructorEmail);
        editNotes.setText(notes);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();;
                return true;
            case R.id.addAssessment:
                return true;
            case R.id.notify:
                return true;
            case R.id.share:
                this.shareNotes();
                return true;
            case R.id.deleteCourse:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {
        Repository repo = new Repository(getApplication());
        Course course = new Course(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(),
                editStatus.getText().toString(), editInstructorName.getText().toString(), editInstructorPhone.getText().toString(),
                editInstructorEmail.getText().toString(), editNotes.getText().toString(), termId);
        repo.updateCourse(course);

        Intent intent = new Intent(CourseDetail.this, CourseList.class);
        startActivity(intent);
    }

    public void shareNotes() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, editNotes.getText().toString());
        sendIntent.putExtra(Intent.EXTRA_TITLE, editTitle.getText().toString() + " - Course Notes");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}