## Git lab是一个源码托工具，使用git为代码管理工具，在基础上搭建web服务。

## 1.docker 安装gitlab： docker pull gitlab/gitlab-ce
docker exec -it gitlab bash
查看dockerlab密码： cat /etc/gitlab/initial_root_password

## 2.创建一个文件夹执行dockercompos文件： 编写dockercompose
```yaml
services:
  gitlab:
    image: gitlab/gitlab-ce    #镜像名称
    container_name: xj-gitlab  
    restart: always
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://172.27.173.253:9999'
        gitlab_rails['gitlab_shell_ssh_port']=2222
    ports:
      - 9999:9999
      - 2222:2222
    volumes:
      - ./config:/ect/gitlab
      - ./logs:/var/log/gitlab
      - ./data:/var/opt/gitlab
  ```
  ## 3.执行docker-compose up -d
  
  ## 4.进入容器查看密码：docker exec -it gitlab bash
## 查看dockerlab密码： cat /etc/gitlab/initial_root_password

## gitlab密码找回
```php
<root@gitlab ~># cd /opt/gitlab/bin
<root@gitlab bin># gitlab-rails console
--------------------------------------------------------------------------------
 Ruby:         ruby 2.7.4p191 (2021-07-07 revision a21a3b7d23) [x86_64-linux]
 GitLab:       14.4.1 (1a23d731c9f) FOSS
 GitLab Shell: 13.21.1
 PostgreSQL:   12.7
--------------------------------------------------------------------------------
Loading production environment (Rails 6.1.4.1)
irb(main):001:0> u=User.where(id:1).first
=> #<User id:1 @root>
irb(main):002:0> User.all
=> #<ActiveRecord::Relation [#<User id:1 @root>]>
irb(main):003:0> u.password='12345678'
=> "12345678"
irb(main):004:0> u.password_confirmation='12345678'
=> "12345678"
irb(main):005:0> u.save
Enqueued ActionMailer::MailDeliveryJob (Job ID: 2e04113b-4441-4b96-b85d-b6d8f4adc582) to Sidekiq(mailers) with arguments: "DeviseMailer", "password_change", "deliver_now", {:args=>[#<GlobalID:0x00007f31cb855aa8 @uri=#<URI::GID gid://gitlab/User/1>>]}
=> true
irb(main):006:0> exit

再去登录就可以成功

```
