package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.c196_pa.R;

import Database.Repository;
import Entity.Term;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTerms(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class);
        startActivity(intent);

        //Repository repo = new Repository(getApplication());
        //Term term = new Term("new", "blah", "2/19/2022");
        //repo.insert(term);
        //repo.getAllTerms();
    }
}