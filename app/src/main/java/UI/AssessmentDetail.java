package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Database.Repository;
import Entity.Assessment;
import Entity.Course;
import Entity.Term;
import Utility.AssessmentAdapter;
import Utility.CourseAdapter;
import Utility.MyDatePicker;
import Utility.MyReceiver;

public class AssessmentDetail extends AppCompatActivity {

    int id;
    String title;
    String start;
    String end;
    String type;
    int courseID;
    int index;
    RadioButton objective;
    RadioButton performance;
    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    Repository repo;
    String dateFormat;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        objective = findViewById(R.id.objectiveRadio);
        performance = findViewById(R.id.performanceRadio);
        editTitle = findViewById(R.id.titleEdit);
        editStart = findViewById(R.id.startEdit);
        editEnd = findViewById(R.id.endEdit);

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("startDate");
        end = getIntent().getStringExtra("endDate");
        type = getIntent().getStringExtra("type");
        courseID = getIntent().getIntExtra("courseID", -1);
        index = getIntent().getIntExtra("index", -1);

        if(type.equals("OA"))
            objective.setChecked(true);
        else
            performance.setChecked(true);
        editTitle.setText(title);
        editStart.setText(start);
        editEnd.setText(end);

        MyDatePicker myDatePicker = new MyDatePicker(editStart, editEnd, AssessmentDetail.this);
        myDatePicker.setStartDatePicker();
        myDatePicker.setEndDatePicker();

        repo = new Repository(getApplication());
        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessmentdetail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteAssessment:
                this.deleteAssessment();
                return true;
            case R.id.notify:
                if (editStart.getText().toString() != "")
                    this.setStartNotification();
                if (editEnd.getText().toString() != "")
                    this.setEndNotification();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {
        if(editTitle.getText().toString().isEmpty()) {
            Toast.makeText(AssessmentDetail.this, "Please enter the assessment title.", Toast.LENGTH_LONG).show();
        }
        else {
            Assessment assessment = null;
            if(objective.isChecked()){
                assessment = new Assessment(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), "OA", courseID);
                repo.updateAssessment(assessment);

                AssessmentList.assessments.get(index).setTitle(editTitle.getText().toString());
                AssessmentList.assessments.get(index).setStartDate(editStart.getText().toString());
                AssessmentList.assessments.get(index).setEndDate(editEnd.getText().toString());
                AssessmentList.assessments.get(index).setType("OA");
                AssessmentList.assessments.get(index).setCourseId(courseID);
            }
            else {
                assessment = new Assessment(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), "PA", courseID);
                repo.updateAssessment(assessment);

                AssessmentList.assessments.get(index).setTitle(editTitle.getText().toString());
                AssessmentList.assessments.get(index).setStartDate(editStart.getText().toString());
                AssessmentList.assessments.get(index).setEndDate(editEnd.getText().toString());
                AssessmentList.assessments.get(index).setType("PA");
                AssessmentList.assessments.get(index).setCourseId(courseID);
            }
            AssessmentList.adapter.notifyItemChanged(index);
            this.finish();
        }
    }

    public void deleteAssessment() {
        Assessment assessment = new Assessment(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(), type, courseID);

        repo.deleteAssessment(assessment);
        AssessmentList.assessments.remove(index);
        AssessmentList.adapter.notifyItemRemoved(index);

        Toast.makeText(AssessmentDetail.this, title  + " has been deleted.", Toast.LENGTH_LONG).show();

        this.finish();
    }

    private void setStartNotification() {
        String notificationDate = editStart.getText().toString();
        Date triggerDate = null;
        try {
            triggerDate = sdf.parse(notificationDate);
            Long trigger = triggerDate.getTime();
            Intent intent = new Intent(AssessmentDetail.this, MyReceiver.class);
            intent.putExtra("key", "Assessment: '" + title+ "' is starting today.");
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Date today = new Date();
            Long todayMilli = today.getTime();
            if(trigger > todayMilli)
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setEndNotification() {
        String notificationDate = editEnd.getText().toString();
        Date triggerDate = null;
        try {
            triggerDate = sdf.parse(notificationDate);
            Long trigger = triggerDate.getTime();
            Intent intent = new Intent(AssessmentDetail.this, MyReceiver.class);
            intent.putExtra("key", "Assessment: '" + title+ "' is ending today.");
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Date today = new Date();
            Long todayMilli = today.getTime();
            if(trigger > todayMilli)
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}