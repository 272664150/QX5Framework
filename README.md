# Change Log

2020/4/21
##
1. 优化退出WebView的内存占用
2. X5内核升级到43903，支持64位机器
3. 若当前进程不是系统进程，则不对WebView做Hook处理
4. 解决androidx.appcompat:appcompat:1.1.0在Android 5.x上的崩溃问题
5. InitContentProvider先于Application初始化，由于AndPermission动态权限库拿不到Activity Context会崩溃，所以在MainActivity中初始化

2020/4/20
##
1. TBS内核首次加载 ”dex2oat优化方案“
2. 非wifi情况下, 主动下载x5内核
3. 监控内核下载状态