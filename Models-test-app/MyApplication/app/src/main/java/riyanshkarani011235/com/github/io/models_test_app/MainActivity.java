package riyanshkarani011235.com.github.io.models_test_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import com.comp.iitb.vialogue.models.ParseObjects.models.Author;
import com.comp.iitb.vialogue.models.ParseObjects.models.Category;
import com.comp.iitb.vialogue.models.ParseObjects.models.Language;
import com.comp.iitb.vialogue.models.ParseObjects.models.Project;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ParseObject p = new ParseObject("hello");
//        try {
//            p.pin();
//            p.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(p.getObjectId());

        Project p = new Project("hello world project", "100e3r");
        p.setName("ironstein's first project");
        p.setDescription("I am Iron Man (Batman Sucks!)");
        p.setCategory(new Category());
        p.setLanguage(new Language());
        p.setAuthor(new Author());
        ArrayList<String> tags = new ArrayList();
        tags.add("one");
        tags.add("two");
        p.setTags(tags);
        ArrayList<Integer> resolution = new ArrayList<>();
        resolution.add(10);
        resolution.add(20);
        p.setResolution(resolution);
        for(int i=0; i<10; i++) {
            p.addSlide();
        }

        p.saveParseObject();

    }
}
