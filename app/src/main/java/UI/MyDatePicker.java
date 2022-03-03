package UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyDatePicker {

    private EditText startDatePicker;
    private EditText endDatePicker;
    private Context context;

    String dateFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;
    final Calendar calendarDate = Calendar.getInstance();

    public MyDatePicker(EditText startDatePicker, EditText endDatePicker, Context context) {
        this.startDatePicker = startDatePicker;
        this.endDatePicker = endDatePicker;
        this.context = context;
    }

    public void setStartDatePicker() {
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDatePicker.setText(sdf.format(calendarDate.getTime()));
            }
        };

        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringDate = startDatePicker.getText().toString();

                if (!stringDate.isEmpty()) {
                    try {
                        calendarDate.setTime(sdf.parse(stringDate));
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                new DatePickerDialog(context, startDateListener, calendarDate.get(Calendar.YEAR),
                        calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void setEndDatePicker() {
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDatePicker.setText(sdf.format(calendarDate.getTime()));
            }
        };

        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringDate = endDatePicker.getText().toString();

                if (!stringDate.isEmpty()) {
                    try {
                        calendarDate.setTime(sdf.parse(stringDate));
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                new DatePickerDialog(context, endDateListener, calendarDate.get(Calendar.YEAR),
                        calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
