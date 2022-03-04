package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.c196_pa.R;

import java.util.List;

import Database.Repository;
import Entity.Course;
import Entity.Term;

public class CourseList extends AppCompatActivity {

    public static CourseAdapter adapter;
    public static List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.rvCourses);
        Repository repo = new Repository(getApplication());
        courses = repo.getAllCourses();
        adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(courses);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddCourse(View view) {
        Intent intent = new Intent(CourseList.this, AddCourse.class);
        startActivity(intent);
    }
}