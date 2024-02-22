package cn.kylebing.blackberry.q10_keyboard

import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)


        // LIST VIEW
        val deviceStringArray = arrayOf(
            "BlackBerry Q10",
            "Macbook Pro 14",
            "Windows (Win10)",
            "BlackBerry 9900",
            "BlackBerry 9800",
            "iPhone 15 Pro"
        )
        val adapter = ArrayAdapter(this, R.layout.list_cell, R.id.list_cell, deviceStringArray)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

        // Handle Click
        val messageClickedHandler =
            OnItemClickListener { parent, view, position, id ->
                Toast.makeText(
                    view.context, String.format(
                        "position: %d, id: %d\n你选择了 %s", position, id,
                        deviceStringArray[position]
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        listView.onItemClickListener = messageClickedHandler


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
