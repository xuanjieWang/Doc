### 编写systemd 服务单元文件的配置内容

``` shell
[Unit]
Description=yusu-hy4G

[Service]
User=root
WorkingDirectory=/home/test
ExecStart=/home/soft/idk/bin/java -jar /home/test/ruoyi-admin.jar
Environment=LD_LIBRARY_PATH=/home/sdklib/x64/

[Install]
WantedBy=multi-user.target
```


以下是对这个配置内容的详细解释：
[Unit] 部分：
Description=yusu-hy4G：这只是对该服务的简单描述，用于标识该服务的用途。
[Service] 部分：
User=root：指定以 root 用户身份运行这个服务。
WorkingDirectory=/home/test：定义了服务运行时的工作目录为 /home/test。
ExecStart=/home/soft/idk/bin/java -jar /home/test/ruoyi-admin.jar：这是服务启动时执行的具体命令，即使用指定路径的 Java 程序去运行 /home/test 目录下的 ruoyi-admin.jar 文件。
Environment=LD_LIBRARY_PATH=/home/sdklib/x64/：设置了一个环境变量 LD_LIBRARY_PATH，其值为 /home/sdklib/x64/，这通常用于指定共享库的搜索路径。
[Install] 部分：
WantedBy=multi-user.target：表示该服务被多用户运行级别所需要，即系统在进入多用户运行级别时会自动启动该服务。


1. 在上述示例中，你需要将 /path/to/your/script.sh 替换为实际脚本的路径。
2. 重新加载 systemd 配置：运行 sudo systemctl daemon-reload 命令，使 systemd 重新加载配置文件。
3. 启动服务：运行 sudo systemctl start my_script.service 命令，启动你的脚本服务。
4. 设置服务开机自启动（可选）：如果你希望在系统启动时自动启动该服务，可以运行 sudo systemctl enable my_script.service 命令。
