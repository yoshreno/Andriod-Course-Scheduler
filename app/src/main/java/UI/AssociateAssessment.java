package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.util.ArrayList;

import Database.Repository;
import Entity.Assessment;
import Entity.Course;
import Utility.AssessmentAdapter;
import Utility.CourseAdapter;

public class AssociateAssessment extends AppCompatActivity {

    public static int courseID;
    public static Assessment assessment;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseID = getIntent().getIntExtra("id", -1);

        RecyclerView recyclerView = findViewById(R.id.rvAssociateAssessment);
        repo = new Repository(getApplication());
        ArrayList<Assessment> notAssociated = new ArrayList<>();
        for(Assessment a: repo.getAllAssessments()) {
            if(a.getCourseId() != courseID)
                notAssociated.add(a);
        }

        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(notAssociated);
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

    public void onAddAssessment(View view) {
        if(assessment != null) {
            repo.updateAssessment(assessment);

            CourseDetail.associatedAssessments.add(assessment);
            CourseDetail.adapter.notifyItemInserted(CourseDetail.associatedAssessments.size() - 1);

            this.finish();
        }
        else {
            Toast.makeText(AssociateAssessment.this, "Please select an assessment to add.", Toast.LENGTH_LONG).show();
        }
    }
}