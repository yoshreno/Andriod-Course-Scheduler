package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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

public class CourseDetail extends AppCompatActivity {

    int courseId;
    String title;
    String start;
    String end;
    String status;
    String instructorName;
    String instructorPhone;
    String instructorEmail;
    String notes;
    int termId;
    int index;

    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    EditText editStatus;
    EditText editInstructorName;
    EditText editInstructorPhone;
    EditText editInstructorEmail;
    EditText editNotes;

    String dateFormat;
    SimpleDateFormat sdf;
    Repository repo;
    public static AssessmentAdapter adapter;
    public static Assessment selectedAssessment;
    public static ArrayList<Assessment> associatedAssessments;

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

        courseId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        start = getIntent().getStringExtra("startDate");
        end = getIntent().getStringExtra("endDate");
        status = getIntent().getStringExtra("status");
        instructorName = getIntent().getStringExtra("instructorName");
        instructorPhone = getIntent().getStringExtra("instructorPhone");
        instructorEmail = getIntent().getStringExtra("instructorEmail");
        notes = getIntent().getStringExtra("notes");
        termId = getIntent().getIntExtra("termId", -1);
        index = getIntent().getIntExtra("index", -1);

        editTitle.setText(title);
        editStart.setText(start);
        editEnd.setText(end);
        editStatus.setText(status);
        editInstructorName.setText(instructorName);
        editInstructorPhone.setText(instructorPhone);
        editInstructorEmail.setText(instructorEmail);
        editNotes.setText(notes);

        associatedAssessments = new ArrayList<>();
        repo = new Repository(getApplication());
        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);

        MyDatePicker myDatePicker = new MyDatePicker(editStart, editEnd, CourseDetail.this);
        myDatePicker.setStartDatePicker();
        myDatePicker.setEndDatePicker();

        RecyclerView recyclerView = findViewById(R.id.rvAssociatedAssessments);
        for(Assessment assessment: repo.getAllAssessments()) {
            if(assessment.getCourseId() == courseId)
                associatedAssessments.add(assessment);
        }
        adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(associatedAssessments);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursedetail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.addAssessment:
                this.addAssessment();
                return true;
            case R.id.removeAssessment:
                this.removeAssessment();
                return true;
            case R.id.notify:
                if (editStart.getText().toString() != "")
                    this.setStartNotification();
                if (editEnd.getText().toString() != "")
                    this.setEndNotification();
                return true;
            case R.id.share:
                this.shareNotes();
                return true;
            case R.id.deleteCourse:
                deleteCourse();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {
        if(editTitle.getText().toString().isEmpty()) {
            Toast.makeText(CourseDetail.this, "Please enter the course title.", Toast.LENGTH_LONG).show();
        }
        else {
            Repository repo = new Repository(getApplication());
            Course course = new Course(courseId, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString(),
                    editStatus.getText().toString(), editInstructorName.getText().toString(), editInstructorPhone.getText().toString(),
                    editInstructorEmail.getText().toString(), editNotes.getText().toString(), termId);
            repo.updateCourse(course);
            
            CourseList.courses.get(index).setTitle(editTitle.getText().toString());
            CourseList.courses.get(index).setStartDate(editStart.getText().toString());
            CourseList.courses.get(index).setEndDate(editEnd.getText().toString());
            CourseList.courses.get(index).setStatus(editStatus.getText().toString());
            CourseList.courses.get(index).setInstructorName(editInstructorName.getText().toString());
            CourseList.courses.get(index).setInstructorPhone(editInstructorPhone.getText().toString());
            CourseList.courses.get(index).setInstructorEmail(editInstructorEmail.getText().toString());
            CourseList.courses.get(index).setNotes(editNotes.getText().toString());

            CourseList.adapter.notifyItemChanged(index);
            this.finish();
        }
    }

    public void deleteCourse() {
        Course course = new Course(courseId, title, start, end, status, instructorName, instructorPhone, instructorEmail, notes, termId);
        repo.deleteCourse(course);

        CourseList.courses.remove(index);
        CourseList.adapter.notifyItemRemoved(index);

        Toast.makeText(CourseDetail.this, title  + " has been deleted.", Toast.LENGTH_LONG).show();

        this.finish();
    }

    public void addAssessment() {
        Intent intent = new Intent(CourseDetail.this, AssociateAssessment.class);
        intent.putExtra("id", courseId);
        startActivity(intent);
    }

    private void removeAssessment() {
        if (selectedAssessment != null) {
            repo.updateAssessment(selectedAssessment);
            int i = associatedAssessments.indexOf(selectedAssessment);
            associatedAssessments.remove(selectedAssessment);
            adapter.notifyItemRemoved(i);
        }
        else
            Toast.makeText(CourseDetail.this, "Please select an assessment to remove.", Toast.LENGTH_LONG).show();
    }

    private void shareNotes() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, editNotes.getText().toString());
        sendIntent.putExtra(Intent.EXTRA_TITLE, title + " - Course Notes");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void setStartNotification() {
        String notificationDate = editStart.getText().toString();
        Date triggerDate = null;
        try {
            triggerDate = sdf.parse(notificationDate);
            Long trigger = triggerDate.getTime();
            Intent intent = new Intent(CourseDetail.this, MyReceiver.class);
            intent.putExtra("key", "Course: '" + title+ "' is starting today.");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            LocalDate today = LocalDate.now();
            String todayString = today.getMonthValue() + "/" + today.getDayOfMonth() + "/" + today.getYear();
            Date dateToday = sdf.parse(todayString);
            if(triggerDate.equals(dateToday) || triggerDate.after(dateToday))
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
            Intent intent = new Intent(CourseDetail.this, MyReceiver.class);
            intent.putExtra("key", "Course: '" + title+ "' is ending today.");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            LocalDate today = LocalDate.now();
            String todayString = today.getMonthValue() + "/" + today.getDayOfMonth() + "/" + today.getYear();
            Date dateToday = sdf.parse(todayString);
            if(triggerDate.equals(dateToday) || triggerDate.after(dateToday))
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}