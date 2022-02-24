package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.c196_pa.R;

import Database.Repository;
import Entity.Term;

public class TermDetail extends AppCompatActivity {

    String title;
    String start;
    String end;
    int id;
    EditText editTitle;
    EditText editStart;
    EditText editEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

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
    }

    public void onSave(View view) {
        Repository repo = new Repository(getApplication());
        Term term = new Term(id, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
        repo.update(term);

        Intent intent = new Intent(TermDetail.this, TermList.class);
        startActivity(intent);
    }
}