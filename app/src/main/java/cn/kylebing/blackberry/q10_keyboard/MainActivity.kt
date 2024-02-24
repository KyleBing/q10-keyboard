package cn.kylebing.blackberry.q10_keyboard

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        textViewBluetooth.text = if (bluetoothAvailable) "蓝牙:有" else "蓝牙:无"

        val textViewBluetoothLow = findViewById<TextView>(R.id.textViewBlueToothLE)
        textViewBluetoothLow.text = if (bluetoothAvailable) "低功耗蓝牙:有" else "低功耗蓝牙:无"

        refreshBlueToothInfo() // 刷新蓝牙信息

        /**
         * 按钮事件
         */

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

        // bluetooth receive
        findViewById<Button>(R.id.button_receive_connection)
            .setOnClickListener {
                receiveBTDeviceConnection()
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }


    /**
     * 开始发现设备
     */

    // Create a BroadcastReceiver for ACTION_FOUND.
    // 创建一个广播接收器： ACTION_FOUND
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val textViewInfo: TextView = findViewById<TextView>(R.id.text_view_info)
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address
                    textViewInfo.text = "${textViewInfo.text}\n新设备名: ${deviceName}, 地址: ${deviceHardwareAddress}"
                }
            }
        }
    }

    private fun receiveBTDeviceConnection(){
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {

            // 发现设备后创建一个接收器
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)

            // 开始扫描
            if (mBtAdapter.startDiscovery()){
                Toast
                    .makeText(applicationContext, "开始发现设备", Toast.LENGTH_SHORT)
                    .show()
            }
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

        Toast
            .makeText(applicationContext, "已开启可被发现", Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * 开启蓝牙
     */
    private fun openBluetooth() {
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val textViewInfo: TextView = findViewById<TextView>(R.id.text_view_info)
        val textViewDeviceCount: TextView = findViewById<TextView>(R.id.text_view_device_count)
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {

            if (!mBtAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                val REQUEST_ENABLE_BT = 1 // 请求码，将会在结果中返回请求码
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                textViewInfo.text = "蓝牙未开启，已请求开启"
                object : CountDownTimer(3000,1000){
                    override fun onTick(p0: Long) {
                        textViewInfo.text = "${p0/1000}s 后刷新蓝牙列表"
                    }
                    override fun onFinish() {
                        textViewInfo.text = "正在刷新蓝牙列表..."
                        refreshBlueToothInfo()
                    }
                }.start()
            } else {
                textViewDeviceCount.text = "数量：${mBtAdapter.bondedDevices.size}"
                var deviceSetString = ""
                mBtAdapter.bondedDevices.forEach { device ->
                    deviceSetString = deviceSetString + device.toString() + ",\n"
                }
                textViewInfo.text = "设备信息为：\n$deviceSetString"
            }
        }
        Toast
            .makeText(applicationContext, "已开启蓝牙", Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * 刷新蓝牙设备列表
     * 只关注刷新列表，不管开没开启
     */
    private fun refreshBlueToothInfo() {
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val textViewInfo: TextView = findViewById<TextView>(R.id.text_view_info)
        val textViewDeviceCount: TextView = findViewById<TextView>(R.id.text_view_device_count)
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {
            if (mBtAdapter.isEnabled) {
                var deviceSetString = ""
                mBtAdapter.bondedDevices.forEach { device ->
                    deviceSetString = deviceSetString + device.toString() + ",\n"
                }
                textViewDeviceCount.text = "数量： ${mBtAdapter.bondedDevices.size}"
                textViewInfo.text = "设备信息为：\n$deviceSetString"
            }
        }
        Toast
            .makeText(applicationContext, "已刷新", Toast.LENGTH_SHORT)
            .show()
    }
}
