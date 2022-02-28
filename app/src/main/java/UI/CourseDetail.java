package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.c196_pa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    String dateFormat;
    SimpleDateFormat sdf;
    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;
    Calendar calendarDate = Calendar.getInstance();

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

        dateFormat = "MM/dd/yy";
        sdf = new SimpleDateFormat(dateFormat, Locale.US);
        this.setStartDatePicker();
        this.setEndDatePicker();
        this.setStartDate();
        this.setEndDate();
    }

    private void setStartDatePicker() {
        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringDate = editStart.getText().toString();

                if (stringDate == "") {
                    new DatePickerDialog(CourseDetail.this).show();
                }
                else {
                    try {
                        calendarDate.setTime(sdf.parse(stringDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(CourseDetail.this, startDateListener, calendarDate.get(Calendar.YEAR),
                            calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
    }

    private void setEndDatePicker() {
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringDate = editEnd.getText().toString();

                if (stringDate == "") {
                    new DatePickerDialog(CourseDetail.this).show();
                }
                else {
                    try {
                        calendarDate.setTime(sdf.parse(stringDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(CourseDetail.this, endDateListener, calendarDate.get(Calendar.YEAR),
                            calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
    }

    private void setStartDate() {
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editStart.setText(sdf.format(calendarDate.getTime()));
            }
        };
    }

    private void setEndDate() {
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editEnd.setText(sdf.format(calendarDate.getTime()));
            }
        };
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
        Date date = null;
        try {
            date = sdf.parse(notificationDate);
            Long trigger = date.getTime();
            Intent intent = new Intent(CourseDetail.this, MyReceiver.class);
            intent.putExtra("key", "Course: '" + title+ "' is starting today.");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setEndNotification() {
        String notificationDate = editEnd.getText().toString();
        Date date = null;
        try {
            date = sdf.parse(notificationDate);
            Long trigger = date.getTime();
            Intent intent = new Intent(CourseDetail.this, MyReceiver.class);
            intent.putExtra("key", "Course: '" + title+ "' is ending today.");
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, MainActivity.numAlert++, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}