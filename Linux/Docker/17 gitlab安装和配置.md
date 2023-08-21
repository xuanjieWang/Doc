## Git lab是一个源码托工具，使用git为代码管理工具，在基础上搭建web服务。

--- txt
1.docker 安装gitlab： docker pull gitlab/gitlab-ce
docker exec -it gitlab bash
查看dockerlab密码： cat /etc/gitlab/initial_root_password

2.创建一个文件夹执行dockercompos文件：
services:
  gitlab:
    image: gitlab/gitlab-ce
    container_name: xj-gitlab
    restart: always
    environment:
      GitLAB_OMNIBUS_CONFIG: |
        external_url 'http:/172.27.173.253:9999'
        gitlab_rails['gitlab_shell_ssh_port']=2222
    ports:
      - 9999:9999
      - 2222:2222
    volumes:
      - ./config:/ect/gitlab
      - ./logs:/var/log/gitlab
      - ./data:/var/opt/gitlab
  
  3.执行docker-compose up -d
  
  4.进入容器查看密码：docker exec -it gitlab bash
查看dockerlab密码： cat /etc/gitlab/initial_root_password
```
