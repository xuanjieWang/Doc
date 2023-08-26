## 1. 用户认证实现 UserDetailsServiceImpl implements UserDetailsService

#### 实现用户的登录认证；通过查询数据库
```java
package com.yusu.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.yusu.common.core.domain.entity.SysUser;
import com.yusu.common.core.domain.model.LoginUser;
import com.yusu.common.enums.UserStatus;
import com.yusu.common.exception.ServiceException;
import com.yusu.common.utils.MessageUtils;
import com.yusu.common.utils.StringUtils;
import com.yusu.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}

```


#### 1.1 密码验证逻辑
#### yml配置信息
``` yml
# 用户配置
user:
  password:
    # 密码最大错误次数
    maxRetryCount: 5
    # 密码锁定时间（默认10分钟）
    lockTime: 10
```
1. 判断用户存在，删除，停用
2. 进行密码校验，获取到用户的实体信息，获取到当前线程的threadlocal，，存储用户相关的信息。
3. 用户信息的存储是在登录的时候，从redis缓存中取出用户的信息

``` java
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", username);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        passwordService.validate(user);

        return createLoginUser(user);
    }
```
## 2. 认证失败的处理类        AuthenticationEntryPointImpl   implement     AuthenticationEntryPoint
1. 认证失败,封装统一的返回结果
2. AuthenticationEntryPoint接口是用户处理认证失败的方法，实现commence方法

```java
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}

```
## 3. 用户退出处理类        LogoutSuccessHandlerImpl     implements     LogoutSuccessHandler
1.  从token获取到用户的登录信息    tokenService.getLoginUser(request);
2.  从redis中删除用户的登录信息    tokenService.delLoginUser(loginUser.getToken());
3.  使用异步的方式记录退出的信息：   AsyncManager.me().execute

``` java
package com.ruoyi.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志，使用异步的方式实现
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success("退出成功")));
    }
}

```

## 4. token认证过滤器    JwtAuthenticationTokenFilter   extends OncePerRequestFilter    继承
1. 每次请求的时候，根据SecurityUtils.getAuthentication()判断当前用户是否已经登录
2. 验证token的有效性，权限，刷新用户的token过期时间。

SecurityContextHolder具有以下主要功能：

存储和访问安全上下文：SecurityContextHolder使用ThreadLocal来存储当前线程的安全上下文。ThreadLocal是一个线程私有的变量，保证了安全上下文的线程隔离性。通过SecurityContextHolder，可以方便地在应用程序中的任何位置访问当前用户的安全上下文。

获取当前认证的用户信息：SecurityContextHolder提供了静态方法来获取当前认证的用户信息。例如，可以使用SecurityContextHolder.getContext().getAuthentication()方法获取表示当前认证用户的Authentication对象。

设置安全上下文：SecurityContextHolder还提供了方法来设置安全上下文，以便在需要时手动更改当前用户的身份或权限信息。

