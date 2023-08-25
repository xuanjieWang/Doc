### WebMvcConfigurer 接口，用于定制和扩展mvc的配置
1. **addInterceptors(InterceptorRegistry registry)**：用于注册拦截器，可以指定拦截器的路径和顺序。
2. configureViewResolvers(ViewResolverRegistry registry)：用于配置视图解析器，可以设置视图解析器的前缀、后缀等相关属性
3. **addResourceHandlers(ResourceHandlerRegistry registry)**：用于配置静态资源的处理和映射，可以指定静态资源的路径、缓存策略等。
4. addFormatters(FormatterRegistry registry)：用于添加格式化器和转换器，用于处理参数的格式转换，比如日期格式化、字符串转枚举等。
5. configureMessageConverters(List<HttpMessageConverter<?>> converters)：用于配置消息转换器，可以自定义响应的数据格式转换，如 JSON、XML 等。
6. **WebMvcConfigurer**接口可以处理跨域，一般的实现接口是：ResourcesConfig（拦截器），CorsConfig（处理跨域）

####  WebMvcConfigurer -> ResourcesConfig（repeatSubmitInterceptor）  添加拦截器
```java
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }
```
#### repeatSubmitInterceptor implements HandlerInterceptor 抽象类
1. java多态性当一个抽象类实现了接口的时候，子类继承这个抽象类的时候，默认也是认为实现了这个接口。
2. 因为子类也是默认继承父类的属性和方法
3. 注册抽象类作为拦截器的好处在于，通过抽象类可以定义一些共享的行为和逻辑，用于处理通用的拦截逻辑。而子类则可以根据具体的业务需求进行扩展和定制，实现其自己特定的拦截处理。
```java
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                //isRepeatSubmit 抽象类的子类实现，判断是否是重复提交
                if (this.isRepeatSubmit(request, annotation)) {
                    AjaxResult ajaxResult = AjaxResult.error(annotation.message());
                    ServletUtils.renderString(response, JSON.toJSONString(ajaxResult));
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

     // 验证是否重复提交由子类实现具体的防重复提交的规则，抽象方法
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);
```
#### SameUrlDataInterceptor extend repeatSubmitInterceptor抽象类    实现抽象方法isRepeatSubmit
1. 主要是实现父类抽象类中判断是否重复提交的方法
2. 防止重复提交的具体实现，url和数据和上一次是一样的10s之内
```java
  public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
  xxx
}
```


