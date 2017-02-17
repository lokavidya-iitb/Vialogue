package riyanshkarani011235.com.github.io.models_test_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseInstallation;

import riyanshkarani011235.com.github.io.models_test_app.models.BaseParseClass;
import riyanshkarani011235.com.github.io.models_test_app.models.Project;

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
