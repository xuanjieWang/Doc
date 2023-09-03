## bean的加载
1. doGetBean方法的实现
2. 检查缓存中或者实例工厂中是否有对应的实例，不等创建完成就将bean的objectfactory提前曝光
3. 在循环依赖的时候，当使用到的时候才会，通过ObjectFactory的getObject获取到实例
4. 转换对应 beanName，别名
5. 尝试从缓存中加载单例，bean 的实例化
6. 原型模式的依赖检查
7. parentBeanFactory
8. 将存储 XML 配置文件的 Gerne「icBeanDefinition 转换为 RootBeanDefinition
9. 寻找依赖
10. 针对不同的 scope 进行 bean 的创建
11. 类型转换

### FactorvBean：实例化bean的逻辑
1. 获取到bean.getObject()获取到bean实例
2. 

#### 缓存中获取单例 bean
1. singl etonObjects：用于保存 Bea1训ame 和创建 bean 实例之间的关系， bean name 一＞ bean mstance。
2. singleton.Factories ：用于保存 BeanName 和创建 bean 的工厂之间的关系， bean name 一＞ ObjectFactory 。
3. earlySingletonObjects ：也是保存 BeanName 和创建 bean 实例之间的关系，与 singletonO均ects 的不同之处在于，当一个单例 bean 被放到这里面后，那么当 bean 还
在创建过程中，就可以通过 getBean 方法获取到了，其目的是用来检测循环引用
4. registeredSingletons：用来保存当前所有巳注册的 bean

#### 从 bean目的实例中获取对象
1. 假如我们需要对工厂 bean 进行处理，那 么这里得到的其实是工厂 bean 的初始状态，但是我们真正需要的是工厂 bean 中定义的
factory-method 方法中退回的 bean ，而 getObjectForBeaninstance 方法就是完成这个工作的。
2. 对 FactoryBean 正确性的验证。
3. 对非 FactoryBean 不做任何处理
4. 对 bean 进行转换。
5. 将从 Factory 中解析 bean 的工作委托给 getObjectFromFactoryBean o

#### AbstractAutowireCapableBeanFactory的postProcessObj ectF romF actory Bean
1. 尽可能保证所有 bean 初始化后都会调用注册的 BeanPostProc巳ssor 的 postProcessA负erlnitialization 方法进行处理，在实际开发过程中大可以针
对此特性设计自己的业务逻辑。

#### 获取单例
1. 检查缓存是否已经力［J载过
2. 若没有加载，则记录 beanName 的正在加载状态。
3. 加载单例前记录加载状态。可以对循环依赖进行监测
4. 通过调用参数传入的 ObjectFactory 的个体 0均ect 方法实例化 bean 。
5. 将结果记录至缓存并删除加载 bean 过程中所记录的各种辅助状态。


#### 准备创建 bean createBean函数
1. 根据设置的 class 属性或者根据 classl'如ne 来解析 Class。
2. 对 ovetTide 属性进行标记及验证。
   处理 override 属性
3. 应用初始化前的后处理器，解析指定 bean 是否存在初始化前的短路操作。
   实例化的前置处理  resolveBeforelnstantiation将AbsractBeanDefinition转化为BeanWrapper

### 创建bean
1. 如果是单例则需要首先清除缓存。
2. 实例化 bean ，将 Bean.Definition 转换为 BeanWrapper。
   1. 如果存在工厂方法则使用工厂方法进行初始化。 \,) 一个类有多个构造函数，每个构造函数都有不同的参数，所以需要根据参数锁定构造 函数并进行初始化。
   2. 如果既不存在工厂方法也不存在带有参数的构造函数，则使用默认的构造函数进行bean 的实例f匕。
3. MergedBean.Defin itionPostProcessor 的应用。 bean 合并后的处理， Autowired 注解正是通过此方法实现诸如类型的预解析。
4. 依赖处理。通过放入缓存中的 ObjectFactory 来创建实例，这样就解决了循环依赖的问题
5. 属性填充。 将所有属性填充至 bean 的实例中。
6. 注册 DisposableBean。 如果配置了 destroy-method，这里需要注册以便于在销毁时候调用。
7. 完成创建井返回。

### 创建 bean 的实例
1. 如果在 RootBeanDefinition 中存在 factoryMethodName 属性
2. autowireConstructor

### 属性注入
1. InstantiationAwareBeanPostProcessor 处理器的 postProcessAfterinstantiation 函数的应用 ， 此函数可以控制程序是否继续进行属性填充。
2. 根据注人类型（ byName/byType ），提取依赖的 bean，并统一存入 PropertyValues 中
3. 应用 InstantiationAwareBeanPostProcessor 处理器的 postProcessPrope町Values 方法， 对属性 获取完毕填充前对属性的再次处理，典型应用是 RequiredAnnotationBeanPostProcessor 类中对
属性的验证。
4. 将所有 PropertyValues 中的属性填充至 BeanWrapper 中

### 初始化 bean
1. 激活 Aware 方法，BeanFactory Aware 、 ApplicationContextAware 、 ResourceLoaderAware 、 ServletContextAware
2. 注册 DisposableBean
3. 

   


