package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_pa.R;

import java.util.List;

import Database.Repository;
import Entity.Assessment;
import Entity.Term;
import Utility.MyDatePicker;

public class AddTerm extends AppCompatActivity {

    EditText editTitle;
    EditText editStart;
    EditText editEnd;
    private int nextID;
    Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repo = new Repository(getApplication());
        nextID = this.getNextID();

        editTitle = findViewById(R.id.AddTerm_titleEdit);
        editStart = findViewById(R.id.AddTerm_startEdit);
        editEnd = findViewById(R.id.AddTerm_endEdit);

        MyDatePicker myDatePicker = new MyDatePicker(editStart, editEnd, AddTerm.this);
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
            Toast.makeText(AddTerm.this, "Please enter the term title.", Toast.LENGTH_LONG).show();
        }
        else {
            Term term = new Term(nextID, editTitle.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
            repo.insertTerm(term);
            TermList.terms.add(term);
            TermList.adapter.notifyItemInserted(TermList.terms.size());
            this.finish();
        }
    }

    private int getNextID(){
        List<Term> terms = repo.getAllTerms();
        if(terms.size() == 0)
            nextID = 1;
        else
            nextID = terms.get(terms.size() - 1).getTermId() + 1;

        return nextID;
    }
}