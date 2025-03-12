
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

server {
        #listen       80;
	listen		443	ssl;

        server_name	life520.top;
	
	ssl_certificate		C:\\life520.top_bundle.crt;
	ssl_certificate_key	C:\\life520.top.key;

	ssl_session_timeout 5m;
	ssl_protocols TLSv1.2 TLSv1.3;
	ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
	ssl_prefer_server_ciphers on;
	
        location / {
            root   html;

            index  index.html index.htm;
				try_files $uri $uri/ /index.html;
        }
location /prod-api/ {
   proxy_set_header Host $http_host;
   proxy_set_header X-Real-IP $remote_addr;
   proxy_set_header REMOTE-HOST $remote_addr;
   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   proxy_pass http://localhost:8887/;
  }
}

server {
        listen       80;

        server_name	life520.top;
	
        location / {
            root   html;
            index  index.html index.htm;
				try_files $uri $uri/ /index.html;
        }
location /prod-api/ {
   proxy_set_header Host $http_host;
   proxy_set_header X-Real-IP $remote_addr;
   proxy_set_header REMOTE-HOST $remote_addr;
   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   proxy_pass http://localhost:8887/;
  }
}


# 后端地址
server {
        #listen       80;
	listen		8888	ssl;

        server_name	life520.top;
	
	ssl_certificate		C:\\life520.top_bundle.crt;
	ssl_certificate_key	C:\\life520.top.key;

	ssl_protocols TLSv1.2 TLSv1.3;
	ssl_ciphers HIGH:!aNULL:!MD5;
	ssl_prefer_server_ciphers on;
	
location / {
   proxy_pass http://localhost:8887;
   proxy_set_header Host $host;
   proxy_set_header X-Real-IP $remote_addr;
   proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   
  }
}


}
