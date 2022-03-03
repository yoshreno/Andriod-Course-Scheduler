package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.util.ArrayList;
import java.util.List;

import Database.Repository;
import Entity.Course;
import Entity.Term;

public class TermDetail extends AppCompatActivity {

    int id;
    String title;
    String start;
    String end;
    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = findViewById(R.id.titleEdit);
        editStart = findViewById(R.id.startEdit);
        editEnd = findViewById(R.id.endEdit);
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("startDate");
        end = getIntent().getStringExtra("endDate");
        editTitle.setText(title);
        editStart.setText(start);
        editEnd.setText(end);

        RecyclerView recyclerView = findViewById(R.id.rvAssociatedCourses);
        repo = new Repository(getApplication());
        ArrayList<Course> associatedCourses = new ArrayList<>();
        for(Course course: repo.getAllCourses()) {
            if(course.getTermId() == id)
                associatedCourses.add(course);
        }
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(associatedCourses);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termdetail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteTerm:
                this.deleteTerm();
                return true;
            case R.id.addCourse:
                this.addCourse();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {
        Term term = new Term(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
        repo.updateTerm(term);
        this.finish();
        //Intent intent = new Intent(TermDetail.this, TermList.class);
        //startActivity(intent);
    }

    public void deleteTerm() {
        Repository repo = new Repository(getApplication());

        boolean hasCourse = false;
        for(Course course: repo.getAllCourses()) {
            if(course.getTermId() == id) {
                hasCourse = true;
                break;
            }
        }

        if(!hasCourse) {
            Term term = new Term(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
            repo.deleteTerm(term);

            Toast.makeText(TermDetail.this, title  + " has been deleted.", Toast.LENGTH_LONG).show();

            this.finish();
            Intent intent = new Intent(TermDetail.this, TermList.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(TermDetail.this, "Unable to delete a term that has associated course(s). " +
                    "Please delete the associated course(s) before deleting a term.", Toast.LENGTH_LONG).show();
        }
    }

    public void addCourse() {
        this.finish();
        Intent intent = new Intent(TermDetail.this, AssociateCourse.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("startDate", start);
        intent.putExtra("endDate", end);
        startActivity(intent);
    }
}