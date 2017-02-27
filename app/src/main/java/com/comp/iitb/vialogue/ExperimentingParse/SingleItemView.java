package com.comp.iitb.vialogue.ExperimentingParse;

/**
 * Created by jeffrey on 16/2/17.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.comp.iitb.vialogue.R;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtnum;
    String num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);
        // Retrieve data from ListViewAdapter on click event
        Intent i = getIntent();
        // Get the result of num
        num = i.getStringExtra("num");
        // Locate the TextView in singleitemview.xml
        txtnum = (TextView) findViewById(R.id.num);
        // Set the results into TextView
        txtnum.setText(num);

    }
}