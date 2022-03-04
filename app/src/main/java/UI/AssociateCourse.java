package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Database.Repository;
import Entity.Course;

public class AssociateCourse extends AppCompatActivity {

    public static int termID;
    public static String termTitle;
    public static String termStart;
    public static String termEnd;
    public static Course course;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termID = getIntent().getIntExtra("id", -1);
        termTitle = getIntent().getStringExtra("title");
        termStart = getIntent().getStringExtra("startDate");
        termEnd = getIntent().getStringExtra("endDate");

        RecyclerView recyclerView = findViewById(R.id.rvAssociateCourse);
        repo = new Repository(getApplication());
        ArrayList<Course> notAssociated = new ArrayList<>();
        for(Course c: repo.getAllCourses()) {
            if(c.getTermId() != termID)
                notAssociated.add(c);
        }

        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(notAssociated);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_termlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                /**
                Intent intent = new Intent(this, TermDetail.class);
                intent.putExtra("id", AssociateCourse.termID);
                intent.putExtra("title", AssociateCourse.termTitle);
                intent.putExtra("startDate", AssociateCourse.termStart);
                intent.putExtra("endDate", AssociateCourse.termEnd);
                startActivity(intent);
                 */

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddCourse(View view) {
        if(course != null) {
            repo.updateCourse(course);

            TermDetail.associatedCourses.add(course);
            TermDetail.adapter.notifyItemInserted(TermDetail.associatedCourses.size() - 1);

            this.finish();

            /**
            Intent intent = new Intent(this, TermDetail.class);
            intent.putExtra("id", AssociateCourse.termID);
            intent.putExtra("title", AssociateCourse.termTitle);
            intent.putExtra("startDate", AssociateCourse.termStart);
            intent.putExtra("endDate", AssociateCourse.termEnd);
            startActivity(intent);
             */
        }
        else {
            Toast.makeText(AssociateCourse.this, "Please select a course to add.", Toast.LENGTH_LONG).show();
        }
    }
}