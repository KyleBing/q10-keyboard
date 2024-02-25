package cn.kylebing.blackberry.q10_keyboard

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BluetoothDeviceRecycleListAdapter(private val dataSet: MutableList<BluetoothDevice>) :
    RecyclerView.Adapter<BluetoothDeviceRecycleListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val index: TextView
        val deviceName: TextView
        val deviceMac: TextView
        val deviceType: TextView

        init {
            index = view.findViewById(R.id.list_cell_bt_index)
            deviceName = view.findViewById(R.id.list_cell_bt_name)
            deviceMac = view.findViewById(R.id.list_cell_bt_mac)
            deviceType = view.findViewById(R.id.list_cell_bt_type)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_cell, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.index.text = (position + 1).toString()
        viewHolder.deviceName.text = dataSet.get(position).name
        viewHolder.deviceMac.text = dataSet.get(position).address
        var deviceType = ""
        var color = ContextCompat.getColor(viewHolder.deviceName.context,R.color.btCyan)
        when (dataSet.get(position).type){
            BluetoothDevice.DEVICE_TYPE_CLASSIC -> {
                deviceType = "常规"
                color = ContextCompat.getColor(viewHolder.deviceName.context,R.color.btCyan)
            }
            BluetoothDevice.DEVICE_TYPE_DUAL -> {
                deviceType = "全段"
                color = ContextCompat.getColor(viewHolder.deviceName.context,R.color.btBlue)
            }
            BluetoothDevice.DEVICE_TYPE_LE -> {
                deviceType = "低耗"
                color = ContextCompat.getColor(viewHolder.deviceName.context,R.color.btGreen)
            }
            BluetoothDevice.DEVICE_TYPE_UNKNOWN -> {
                deviceType = "不明"
                color = ContextCompat.getColor(viewHolder.deviceName.context,R.color.btGreen)
            }
        }
        viewHolder.deviceType.text = deviceType
        viewHolder.deviceType.setTextColor(color)
    }

    override fun getItemCount() = dataSet.size
}
