package cn.kylebing.blackberry.demo_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);


        // LIST VIEW
        final String[] list = {"BlackBerryQ10", "MacbookPro 14", "Windows(Win10)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.device_list, list);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);



/*
        // button click event
        final Button button = findViewById(R.id.button_1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence text = "我的第一个 Toast!";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(v.getContext(), text, duration).show();
            }
        });
*/

    }
}

