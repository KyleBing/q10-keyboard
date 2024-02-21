package cn.kylebing.blackberry.demo_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        // LIST VIEW
        final String[] deviceStringArray = {"BlackBerryQ10", "MacbookPro 14", "Windows(Win10)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_cell, R.id.list_cell, deviceStringArray);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Handle Click
        AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        view.getContext(),
                        String.format("position: %d, id: %d\n你选择了 %s", position, id, deviceStringArray[position]),
                        Toast.LENGTH_SHORT
                ).show();
            }
        };

        listView.setOnItemClickListener(messageClickedHandler);



/*
        // button click event
        final Button button = findViewById(R.id.button_1);]
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

