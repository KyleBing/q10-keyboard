# BlackBerry Q10 蓝牙键盘应用

> 边学习，边开发
> 开发语言：Kotlin

![image](https://github.com/KyleBing/q10-keyboard/assets/12215982/9c57e23f-3246-47e9-8f91-9d8eea512f0d)

## 键盘快捷键

| 键  | 助记 | 功能           |
|----|------------------|
| R  | Refresh | 刷新已配对设备列表，刷新蓝牙状态 |
| 空格 | | 开始搜索附近蓝牙设备       |
| C  | Cancel | 取消搜索             |
| O  | Open | 打开蓝牙             |
| F  | Faxian | 设置蓝牙可被发现         |
| T  | Top | 滚至蓝牙列表顶部         |
| B  | Bottom | 滚至蓝牙列表底部         |


## 注意事项
1. 蓝牙搜索附近设备，在任何操作之前都应该取消搜索的操作 `adapter.cancelDiscovery`，只因它耗费资源比较大。

## 开发日志
- 2024-02-25 添加键盘快捷键，使用 RecyclerView 更详尽的展示蓝牙设备列表，布局优化
- 2024-02-24 搜索附近蓝牙设备
- 2024-02-23 蓝牙控制
- 2024-02-22 app icon， init
