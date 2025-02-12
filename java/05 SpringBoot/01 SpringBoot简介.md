SpringCloud 启动时间，10个优化案例！

1.    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MyApplication.class);
        app.setLazyInitialization(true);  // 启用懒加载
        app.run(args);
    }
启动懒加载

2， 禁用自动配置

3. 日志加载

4. 组件扫描

5. 数据源懒加载

6. 减少JPA实体类扫描

7. 使用配置中心会加时间，使用本地缓存配置中心

8. 使用JVm参数优化

9. 优化健康检查机制

10. gateway路由精简

### 约定大于配置
在Spring Boot框架中，也采用了"约定大于配置"的原则。Spring Boot通过一系列的约定和默认配置来简化开发过程，让开发人员能够更加专注于业务逻辑的实现，而无需过多关注繁琐的配置。
Spring Boot在项目启动时会自动加载并应用一些默认的配置，例如自动配置数据源、Web服务器等。通过这些默认配置，开发人员可以快速搭建一个可用的基础项目，而无需手动配置大量的细节。
同时，Spring Boot也提供了一些约定，方便开发人员按照约定的规则组织代码和资源文件。例如，约定了特定的包结构、命名规范，使得开发人员能够更容易地理解和维护代码。
当然，Spring Boot也提供了灵活的配置选项，允许开发人员根据实际需求进行个性化配置。但是相对于传统的Spring框架，Spring Boot的目标是通过约定来减少配置的复杂性和开发人员的工作量。
总之，Spring Boot的"约定大于配置"的原则可以帮助开发人员快速构建可用的项目，并提高开发效率。

