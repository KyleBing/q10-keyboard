package cn.kylebing.blackberry.q10_keyboard

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
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
        val bluetoothLEAvailable =
            packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

        val textViewBluetooth = findViewById<TextView>(R.id.textViewBlueTooth)
        textViewBluetooth.text = "蓝牙：$bluetoothAvailable"

        val textViewBluetoothLow = findViewById<TextView>(R.id.textViewBlueToothLE)
        textViewBluetoothLow.text = "低功耗蓝牙：$bluetoothLEAvailable"

        refreshBlueToothInfo() // 刷新蓝牙信息

        // bluetooth on
        findViewById<Button>(R.id.button_turn_on_bluetooth)
            .setOnClickListener {
                openBluetooth()
            }

        // bluetooth on
        findViewById<Button>(R.id.button_refresh)
            .setOnClickListener {
                refreshBlueToothInfo()
            }

        // bluetooth found
        findViewById<Button>(R.id.button_be_found)
            .setOnClickListener {
                setBluetoothCanBeFound()
            }
    }

    /**
     * 设置蓝牙可被发现
     */
    private fun setBluetoothCanBeFound() {
        val requestCode = 1;
        val discoverableIntent: Intent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100)
            }
        startActivityForResult(discoverableIntent, requestCode)
    }

    /**
     * 开启蓝牙
     */
    private fun openBluetooth() {
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val textViewOther: TextView = findViewById<TextView>(R.id.textViewOther)
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {

            if (!mBtAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                val REQUEST_ENABLE_BT = 1 // 请求码，将会在结果中返回请求码
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                textViewOther.text = "蓝牙未开启，已请求开启"
                object : CountDownTimer(3000,1000){
                    override fun onTick(p0: Long) {
                        textViewOther.text = "${p0/1000}s 后刷新蓝牙列表"
                    }
                    override fun onFinish() {
                        textViewOther.text = "正在刷新蓝牙列表..."
                        refreshBlueToothInfo()
                    }
                }.start()
            } else {
                textViewOther.text = "配对的蓝牙设备数量为 ${mBtAdapter.bondedDevices.size}"
                var deviceSetString = ""
                mBtAdapter.bondedDevices.forEach { device ->
                    deviceSetString = deviceSetString + device.toString() + ","
                }
                textViewOther.text =
                    "配对的蓝牙设备数量为：${mBtAdapter.bondedDevices.size}个\n 设备信息为：$deviceSetString"
            }
        }
    }

    /**
     * 刷新蓝牙设备列表
     * 只关注刷新列表，不管开没开启
     */
    private fun refreshBlueToothInfo() {
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val textViewOther: TextView = findViewById<TextView>(R.id.textViewOther)
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {
            if (mBtAdapter.isEnabled) {
                var deviceSetString = ""
                mBtAdapter.bondedDevices.forEach { device ->
                    deviceSetString = deviceSetString + device.toString() + ",\n"
                }
                textViewOther.text =
                    "配对的蓝牙设备数量为：${mBtAdapter.bondedDevices.size}个\n设备信息为：\n$deviceSetString"
            }
        }
    }
}
