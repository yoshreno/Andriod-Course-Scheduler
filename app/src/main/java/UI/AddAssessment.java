package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.util.List;

import Database.Repository;
import Entity.Assessment;
import Entity.Term;
import Utility.MyDatePicker;

public class AddAssessment extends AppCompatActivity {

    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    RadioButton objective;
    RadioButton performance;

    private int nextID;
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repo = new Repository(getApplication());
        nextID = this.getNextID();

        editTitle = findViewById(R.id.AddAssessment_titleEdit);
        editStart = findViewById(R.id.AddAssessment_startEdit);
        editEnd = findViewById(R.id.AddAssessment_endEdit);
        objective = findViewById(R.id.AddAssessment_objectiveRadio);
        performance = findViewById(R.id.AddAssessment_performanceRadio);

        MyDatePicker myDatePicker = new MyDatePicker(editStart, editEnd, AddAssessment.this);
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
            Toast.makeText(AddAssessment.this, "Please enter the assessment title.", Toast.LENGTH_LONG).show();
        }
        else {
            Assessment assessment = null;
            if(objective.isChecked())
                assessment = new Assessment(nextID, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), "OA", 0);
            else if(performance.isChecked())
                assessment = new Assessment(nextID, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), "PA", 0);

            repo.insertAssessment(assessment);
            AssessmentList.assessments.add(assessment);
            AssessmentList.adapter.notifyItemInserted(AssessmentList.assessments.size());
            this.finish();
        }
    }

    private int getNextID(){
        List<Assessment> assessments = repo.getAllAssessments();
        if(assessments.size() == 0)
            nextID = 1;
        else
            nextID = assessments.get(assessments.size() - 1).getId() + 1;

        return nextID;
    }
}