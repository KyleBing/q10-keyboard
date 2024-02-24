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
    var deviceIndex = 1 // 发现设备的序号

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blue_tooth_feathers)

        // 蓝牙功能展示
        val bluetoothAbility = mutableListOf<String>()
        val bluetoothAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
        val bluetoothLEAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        if (bluetoothAvailable) bluetoothAbility.add("BR/EDR")
        if (bluetoothLEAvailable) bluetoothAbility.add("LE")

        findViewById<TextView>(R.id.textview_bluetooth_status)
            .text = "蓝牙:${bluetoothAbility.joinToString("/")}"


        // 获取蓝牙状态
        getBluetoothState()

        // 刷新蓝牙设备信息
        refreshBluetoothDeviceList()

        // 添加按钮事件
        addClickListenerToButtons()
    }


    /**
     * 添加按钮事件
     */
    private fun addClickListenerToButtons(){
        // 蓝牙开启
        findViewById<Button>(R.id.button_turn_on_bluetooth)
            .setOnClickListener {
                openBluetooth()
            }

        // 蓝牙刷新
        findViewById<Button>(R.id.button_refresh)
            .setOnClickListener {
                refreshBluetoothDeviceList()
            }

        // 蓝牙可被发现
        findViewById<Button>(R.id.button_be_found)
            .setOnClickListener {
                setBluetoothCanBeFound()
            }

        // 蓝牙查找附近设备
        findViewById<Button>(R.id.button_search_device)
            .setOnClickListener {
                startSearchBluetoothDevice()
            }

        // 蓝牙查找 - 取消
        findViewById<Button>(R.id.button_search_cancel)
            .setOnClickListener {
                val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
                mBluetoothManager.adapter.cancelDiscovery()
                Toast.makeText(applicationContext, "取消查找操作", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }

    /**
     * 获取蓝牙状态
     */
    private fun getBluetoothState(){
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {

            var bluetoothState = ""
            when(mBtAdapter.state){
                BluetoothAdapter.STATE_ON -> { bluetoothState = "开启" }
                BluetoothAdapter.STATE_OFF -> { bluetoothState = "关闭" }
                BluetoothAdapter.STATE_CONNECTED -> { bluetoothState = "已连接" }
                BluetoothAdapter.STATE_CONNECTING -> { bluetoothState = "连接中.." }
                BluetoothAdapter.STATE_TURNING_OFF -> { bluetoothState = "关闭中.." }
                BluetoothAdapter.STATE_TURNING_ON -> { bluetoothState = "开启中.." }
            }

            findViewById<TextView>(R.id.textview_bluetooth_capable).text = "状态:$bluetoothState"
        }
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
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!

                    textViewInfo.text = "${textViewInfo.text}${String.format("%2d.", deviceIndex)} ${getBluetoothDeviceStringInfo(device)}\n"
                    deviceIndex++
                }
            }
        }
    }

    private fun startSearchBluetoothDevice(){
        deviceIndex = 1 // 初始化序号

        findViewById<TextView>(R.id.text_view_info).text = "" // 清空显示内容
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
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val mBtAdapter = mBluetoothManager.adapter
        // 取消蓝牙搜索操作
        mBtAdapter.cancelDiscovery() // 除 STATE_ON 之外的状态，操作该操作都会返回 false
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
                        refreshBluetoothDeviceList()
                    }
                }.start()
            } else {
                refreshBluetoothDeviceList()
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
    private fun refreshBluetoothDeviceList() {
        // blue tooth
        val mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val textViewInfo: TextView = findViewById<TextView>(R.id.text_view_info)
        textViewInfo.text = "" // 初始的时候清空内容

        val textViewDeviceCount: TextView = findViewById<TextView>(R.id.text_view_device_count)
        val mBtAdapter = mBluetoothManager.adapter
        if (mBtAdapter != null) {
            // 取消蓝牙搜索操作
            mBtAdapter.cancelDiscovery() // 除 STATE_ON 之外的状态，操作该操作都会返回 false
            if (mBtAdapter.isEnabled){
                textViewDeviceCount.text = "数量:${mBtAdapter.bondedDevices.size}"
                textViewInfo.text = "已配对设备为：\n${mBtAdapter.bondedDevices.joinToString(",\n")}"
            }
        }
        Toast
            .makeText(applicationContext, "已刷新", Toast.LENGTH_SHORT)
            .show()

        // 更新蓝牙状态
        getBluetoothState()
    }

    /**
     * Utility functions
     * 返回蓝牙设备的字符串信息
     */
    private fun getBluetoothDeviceStringInfo(device: BluetoothDevice): String{
        val deviceName = device.name
        val deviceHardwareAddress = device.address // MAC address
        val deviceAlias = device.alias // MAC address
        var deviceType = ""
        when (device.type){
            BluetoothDevice.DEVICE_TYPE_CLASSIC -> deviceType = "常规"
            BluetoothDevice.DEVICE_TYPE_DUAL -> deviceType = "全段"
            BluetoothDevice.DEVICE_TYPE_LE -> deviceType = "低耗"
            BluetoothDevice.DEVICE_TYPE_UNKNOWN -> deviceType = "不明"
        }
        return "$deviceHardwareAddress $deviceType $deviceName"
    }
}
