## org.apache.ibatis.io  通过类加载器在jar包中寻找一个package下满足条件(比如某个接口的子类)的所有类

$### org.apache.ibatis.io包是 MyBatis 框架中用于处理输入输出的工具包。在这个包中，主要包含了一些文件操作和资源加载等功能的工具类。
1. 这个包中的类提供了一些方法来帮助 MyBatis 在进行配置文件加载、资源定位等方面更加灵活和便捷。以下是一些常用的类和功能：
2. Resources：Resources 类提供了加载资源文件的方法，比如加载 XML 配置文件、属性文件等。通过 Resources 类，MyBatis 可以方便地加载和读取配置文件中的信息。
3. ClassLoaderWrapper：ClassLoaderWrapper 类封装了对类加载器的操作，可以帮助 MyBatis 加载指定位置的类或资源。
4. VFS (Virtual File System)：VFS 类用于处理虚拟文件系统，可以用于扫描指定路径下的文件资源，比如 Mapper XML 文件等。
5. ExternalResources：ExternalResources 类提供了一些外部资源的加载方法，用于加载外部的配置文件或其他资源。
6. 这些类和功能都是为了让 MyBatis 框架能够更好地处理文件操作和资源加载，使得配置和使用 MyBatis 更加便捷和灵活。通过 org.apache.ibatis.io 包中提供的工具类，
7. 开发者可以更方便地管理和加载配置文件、资源文件等，从而简化了 MyBatis 的配置和集成过程。

## Resources： 加载配置文件信息
加载资源文件：Resources 类提供了 getResourceAsFile() 和 getResourceAsReader() 等方法，用于加载指定位置的文件资源并返回对应的 File 对象或 Reader 对象。
获取类加载器：Resources 类中的 getDefaultClassLoader() 方法可以获取默认的类加载器，用于加载资源文件。
获取资源流：Resources 类中的 getResourceAsStream() 方法可以将资源文件转换为输入流，方便后续进行读取操作。
解析 XML 文件：Resources 类还提供了 getInputStreamForString() 方法，用于将字符串转换为输入流，通常用于解析 XML 文件。
使用 Resources 类可以简化外部资源的加载和处理过程，特别是在 MyBatis 配置和映射文件的加载过程中经常用到。通过 Resources 类加载外部资源，可以更方便地管理和操作配置文件、映射文件等，提高开发效率。

## ClassLoaderWrapper： 类加载器的操作
启动类加载器（Bootstrap Class Loader）：它是 JVM 内置的类加载器，负责加载 Java 核心类库，比如 rt.jar 中的类。它是最顶层的类加载器，由 C++ 实现，无法通过 Java 代码直接获取。
扩展类加载器（Extension Class Loader）：它是由 ExtClassLoader 类实现的，负责加载 Java 的扩展类库，位于 jre/lib/ext 目录下的 jar 包中的类。开发者也可以自定义扩展类库，并将其添加到扩展类路径中。
应用程序类加载器（Application Class Loader）：它是由 AppClassLoader 类实现的，负责加载应用程序类路径（Classpath）下的类。应用程序类加载器是开发者最常接触和使用的类加载器，它会加载项目中的自定义类和第三方库等。
用户自定义类加载器：开发者可以通过继承 java.lang.ClassLoader 类创建自定义的类加载器。用户自定义类加载器可以根据自己的需求实现类加载的逻辑，比如从非标准的位置加载类文件、实现类似热部署的功能等。

## VFS 类用于处理虚拟文件系统

## ExternalResources: 加载外部
