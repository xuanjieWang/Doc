Feign是一个声明式的 Web Service 客户端。它的出现使开发 Web Service 客户端变得很简单。使用 Feign 只需要创建一个接口加上对应的注解，比如：@FeignClient 注解。 Feign 有可插拔的注解，包括 Feign 注解和 AX-RS 注解。Feign 也支持编码器和解码器，Spring Cloud Open Feign 对 Feign 进行增强支持 Spring Mvc 注解，可以像 Spring Web 一样使用 HttpMessageConverters 等。

Feign 是一种声明式、模板化的 HTTP 客户端。在 Spring Cloud 中使用 Feign，可以做到使用 HTTP 请求访问远程服务，就像调用本地方法一样的，开发者完全感知不到这是在调用远程方法，更感知不到在访问 HTTP 请求。接下来介绍一下 Feign 的特性，具体如下：

可插拔的注解支持，包括 Feign 注解和AX-RS注解。

支持可插拔的 HTTP 编码器和解码器。
支持 Hystrix 和它的 Fallback。
支持 Ribbon 的负载均衡。
支持 HTTP 请求和响应的压缩。
Feign整合了 Ribbon 和 Hystrix，从而不需要开发者针对 Feign 对其进行整合。Feign 还提供了 HTTP 请求的模板，通过编写简单的接口和注解，就可以定义好 HTTP 请求的参数、格式、地址等信息。Feign 会完全代理 HTTP 的请求，在使用过程中我们只需要依赖注入 Bean，然后调用对应的方法传递参数即可。

总的来说，通过Feign让微服务之间的调用变得更简单，就像controller调用service。
