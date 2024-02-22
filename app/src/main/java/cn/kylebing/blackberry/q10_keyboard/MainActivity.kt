package cn.kylebing.blackberry.q10_keyboard

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blue_tooth_feathers)

        // BLUETOOTH State
        val bluetoothAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        val bluetoothLEAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

        val textViewBluetooth = findViewById<TextView>(R.id.textViewBlueTooth)
        textViewBluetooth.text = "蓝牙：$bluetoothAvailable"

        val textViewBluetoothLow = findViewById<TextView>(R.id.textViewBlueToothLE)
        textViewBluetoothLow.text = "低功耗蓝牙：$bluetoothLEAvailable"

        refreshBlueToothInfo() // 刷新蓝牙信息

        // button click
        findViewById<Button>(R.id.button_refresh)
            .setOnClickListener{
                refreshBlueToothInfo()
            }
    }

    private fun refreshBlueToothInfo(){
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

        val textViewOther: TextView = findViewById<TextView>(R.id.textViewOther)
        textViewOther.text = mBluetoothManager.toString()


        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {
            textViewOther.text = "配对的蓝牙设备数量为 ${mBtAdapter.bondedDevices.size}"
            if (!mBtAdapter.isEnabled){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                val REQUEST_ENABLE_BT = 1 // 请求码，将会在结果中返回请求码
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                textViewOther.text = "蓝牙未开启，已请求开启"
            } else {
                var deviceSetString = ""
                for (device in mBtAdapter.bondedDevices){
                    deviceSetString = deviceSetString + device.toString() + ","
                }
                textViewOther.text = "配对的蓝牙设备数量为：${mBtAdapter.bondedDevices.size}个\n 设备信息为：$deviceSetString"
            }
        } else {
            textViewOther.text = "没有匹配的蓝牙"
        }
    }
}
