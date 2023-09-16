## cmd命令行设置utf-8
1. win 键 + R，输入 regedit，确定
2. 按顺序找到 HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Command Processor
3. 点击右键 - 新建，选择 “字符串值”
4. 命名为 “autorun”, 点击右击修改，数值数据填写 “chcp 65001”，确定
5. 这时候打开 cmd 命令窗口就会看到，和之前临时修改的窗口一样，编码已经修改成 UTF-8 了，而且每次打开 cmd 都是 UTF-8 编码。
