package cn.kylebing.blackberry.q10_keyboard

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BLUETOOTH State
        val bluetoothAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        val bluetoothLEAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

        setContentView(R.layout.blue_tooth_feathers)

        val textViewBluetooth = findViewById<TextView>(R.id.textViewBlueTooth)
        textViewBluetooth.text = "蓝牙：$bluetoothAvailable"

        val textViewBluetoothLow = findViewById<TextView>(R.id.textViewBlueToothLE)
        textViewBluetoothLow.text = "低功耗蓝牙：$bluetoothLEAvailable"


/*

        // LIST VIEW
        val deviceStringArray = arrayOf(
            "BlackBerry Q10",
            "Macbook Pro 14",
            "Windows (Win10)",
            "BlackBerry 9900",
            "BlackBerry 9800",
            "iPhone 15 Pro"
        )
        val adapter = ArrayAdapter(this, R.layout.list_cell, R.id.list_cell_text_view, deviceStringArray)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

        // Handle ListView Click
        val messageClickedHandler =
            OnItemClickListener { _, view, position, id ->
                Toast.makeText(
                    view.context,
                    "position: $position, id: $id\n你选择了 $deviceStringArray[position]",
                    Toast.LENGTH_SHORT
                ).show()
            }
        listView.onItemClickListener = messageClickedHandler

*/

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
