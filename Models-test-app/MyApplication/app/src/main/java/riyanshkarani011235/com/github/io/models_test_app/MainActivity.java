package riyanshkarani011235.com.github.io.models_test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp.iitb.vialogue.models.ParseObjects.models.BaseParseClass;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new LoadFromJson();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
