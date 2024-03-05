#### PreparedStatement、CallableStatement和Statement是Java JDBC API中用于执行数据库操作的三个关键接口。虽然它们都可以用来执行SQL语句，但它们之间存在一些重要的区别，这些区别主要体现在它们的用途、性能以及如何使用上。
### Statement
1. 基本用途：Statement接口用于执行静态SQL语句，没有预编译过程。
2. 性能和安全性：相比于PreparedStatement，Statement性能较低，特别是在执行相同SQL语句多次的场景下。此外，直接使用Statement执行SQL语句时，如果SQL参数是由外部输入构成的，可能会导致SQL注入攻击。
3. 使用场景：适用于只执行一次或执行动态SQL语句的场景。

### PreparedStatement的性能之所以高，主要是因为它采用了预编译机制和参数化查询的方式。这些特点使得PreparedStatement在多个方面相比于Statement有着显著的优势：
1. 预编译 当使用PreparedStatement时，SQL语句在第一次执行之前就已经被编译并缓存了。如果同一个PreparedStatement对象被多次用于执行相同的SQL语句（只是参数值不同），它可以直接利用已编译的SQL语句而无需重新编译。这种预编译机制减少了数据库编译SQL语句的次数，从而提高了执行效率。
2. 减少SQL解析时间由于SQL语句在PreparedStatement被创建时就已经编译，因此每次执行时数据库可以跳过解析和编译的步骤，直接执行。这样不仅减少了数据库的工作量，也加快了SQL语句的执行速度。
3. 参数化查询PreparedStatement使用参数化查询，即在SQL语句中使用占位符代替直接的值。这种方式不仅可以提高安全性（防止SQL注入攻击），而且还可以让数据库更好地重用已经编译的执行计划。参数化查询使得数据库能够识别出两次查询实质上是相同的，只是参数值不同，这进一步提高了查询效率。
4. 减少网络开销 在使用PreparedStatement时，由于SQL语句是预编译的，所以在多次执行相同的SQL语句（但参数不同）时，不需要每次都将完整的SQL语句发送到数据库服务器。相反，只需要发送参数值即可，这样可以减少网络传输的数据量，从而降低网络开销。
5. 数据库优化，数据库可以对预编译的SQL语句进行优化，例如生成更有效的执行计划。由于PreparedStatement允许数据库预先知道即将执行的SQL结构，数据库管理系统（DBMS）可以更好地优化查询执行，例如选择更有效的索引。

### CallableStatement
1. 基本用途：CallableStatement接口扩展了PreparedStatement，专门用于执行数据库存储过程和函数。
2. 性能和安全性：与PreparedStatement相似，CallableStatement也提供预编译功能，可以提高性能并保持应用程序的安全性。
3. 使用场景：专为调用数据库中的存储过程和函数设计，支持IN、OUT和INOUT参数。

在 MyBatis 中，jdbcType 是指定映射到数据库的 JDBC 类型的一种属性。在 MyBatis 的映射文件（Mapper XML 文件）中，当定义了一个结果映射时，可以使用 jdbcType 属性来明确指定 Java 对象属性与数据库表字段之间的数据类型映射关系。
``` java 
xml
<result property="age" column="AGE" jdbcType="INTEGER"/>
在上面的例子中，jdbcType 属性指定了"age"属性在映射到数据库字段"AGE"时所使用的 JDBC 数据类型为 INTEGER，这样 MyBatis 在进行数据类型转换时就会按照指定的类型进行映射，从而避免数据类型不匹配导致的错误或异常。
```
### type org.apache.ibatis.type实现java和jdbc中的类型之间转换
1. 类型处理器接口
2. setParameter 设置参数
3. getResult 获取结果  //取得结果,供普通select用
4. @Alias 注解 1)xml方式 <typeAlias alias="Author" type="domain.blog.Author"/>  2)annotation方式@Alias("author")
5. @MappedJdbcTypes  MappedJdbcTypes 注解可以用在参数映射和结果集映射的方法参数上，用于指定该参数或返回值在 JDBC 层面的数据类型
6. @MappedTypes注解
7.  BaseTypeHandler: 基础的类型转换器setParameter，getResult。setNonNullParameter，getNullableResult主要是四个方法

### 常见的类型处理器
``` java
public enum JdbcType {
  /*
   * This is added to enable basic support for the
   * ARRAY data type - but a custom type handler is still required
   */
  //就是包装一下java.sql.Types
  ARRAY(Types.ARRAY),
  BIT(Types.BIT),
  TINYINT(Types.TINYINT),
  SMALLINT(Types.SMALLINT),
  INTEGER(Types.INTEGER),
  BIGINT(Types.BIGINT),
  FLOAT(Types.FLOAT),
  REAL(Types.REAL),
  DOUBLE(Types.DOUBLE),
  NUMERIC(Types.NUMERIC),
  DECIMAL(Types.DECIMAL),
  CHAR(Types.CHAR),
  VARCHAR(Types.VARCHAR),
  LONGVARCHAR(Types.LONGVARCHAR),
  DATE(Types.DATE),
  TIME(Types.TIME),
  TIMESTAMP(Types.TIMESTAMP),
  BINARY(Types.BINARY),
  VARBINARY(Types.VARBINARY),
  LONGVARBINARY(Types.LONGVARBINARY),
  NULL(Types.NULL),
  OTHER(Types.OTHER),
  BLOB(Types.BLOB),
  CLOB(Types.CLOB),
  BOOLEAN(Types.BOOLEAN),
  CURSOR(-10), // Oracle
  UNDEFINED(Integer.MIN_VALUE + 1000),
  //太周到了，还考虑jdk5兼容性，jdk6的常量都不是直接引用
  NVARCHAR(Types.NVARCHAR), // JDK6
  NCHAR(Types.NCHAR), // JDK6
  NCLOB(Types.NCLOB), // JDK6
  STRUCT(Types.STRUCT);
```

### typeHandler 对类型的自动选择
1. 字段类型匹配：MyBatis Plus首先会检查实体类中声明的字段类型。根据Java字段的类型（比如String、Integer、Date等），MyBatis Plus能够识别需要进行数据库类型转换的字段。
2. 内置TypeHandler匹配：MyBatis Plus内置了大量常用类型的TypeHandler，包括处理基本数据类型、字符串、日期等的TypeHandler。当发现一个字段需要进行类型转换时，MyBatis Plus会尝试使用内置的TypeHandler来处理这个字段的类型转换。
3. 自定义TypeHandler匹配：如果没有找到合适的内置TypeHandler，或者你希望自定义字段类型与数据库类型之间的转换逻辑，你可以编写自己的TypeHandler，并将其注册到MyBatis的配置中。在处理字段类型转换时，MyBatis Plus会检查是否存在自定义的TypeHandler来处理这个特定类型的字段，如果存在则会优先使用自定义的TypeHandler。
4. 、默认TypeHandler：最后，如果以上步骤都没有找到合适的TypeHandler，MyBatis Plus会选择默认的TypeHandler来进行处理。默认的TypeHandler通常能够处理基本的数据类型转换，但可能无法满足特定需求。因此，建议尽量使用内置TypeHandler或自定义TypeHandler来确保类型转换的准确性和灵活性。


   


