### "Remember Me" 
1. 功能通常是通过在用户登录时生成一个长期有效的身份验证令牌（token）来实现的。当用户勾选了 "Remember Me" 复选框并成功登录后，服务器会生成一个唯一的令牌
2. 并将其存储在用户的浏览器中，通常是通过在用户的浏览器中设置一个持久化的 Cookie。
3. 当用户重新访问网站时，浏览器会自动发送存储的身份验证令牌到服务器。服务器会检查这个令牌的有效性，并根据令牌中的信息来识别用户身份。
4. 如果令牌有效且未过期，服务器会自动将用户标记为已登录状态，而无需用户再次输入用户名和密码。

要实现 "Remember Me" 功能，需要在后端代码中进行以下步骤：
1. 在用户登录时生成一个唯一的身份验证令牌。
2. 将生成的令牌与用户相关的信息（如用户ID）存储在服务器端的数据库中。
3. 将令牌发送给用户的浏览器，并将其存储为持久化的 Cookie。
4. 当用户再次访问网站时，从浏览器的 Cookie 中获取令牌。
5. 将令牌发送到服务器进行验证。
6. 如果令牌有效且未过期，服务器将标记用户为已登录状态，并继续提供相关的功能和服务。

认证 -> usernamePassword过滤器 -> 成功判断携带参数remebe me -> 生成token(同户名,过期时间,密码)->cookie ->记住我读取cookie
方式不建议使用,cookie存放如果被盗取很容易破解
1. 基于简单token的方式
2. 基于持久化token的方式

### CSRF: 跨站伪造攻击
1. 用户点击链接访问恶意网站,获取到token
2. 进行敏感操作,可以限制操作,比如发送验证码, 

### 跨域
1. 协议不同
2. 主域名不同http https
3. 子域名不同
4. 端口号不同
5. CORS支持跨域,服务器允许跨域就可以了.

