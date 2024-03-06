## 事务
1. Transaction： getConnection（），commit，rollback。close
2. Jdbc事务。直接利用JDBC的commit,rollback。它依赖于从数据源得 到的连接来管理事务范围。

### JdbcTransaction和ManagedTransaction
## JDBC Transaction：
1. JDBC Transaction是通过JDBC API来手动管理事务的过程。在使用JDBC连接数据库时，你可以通过调用setAutoCommit(false)方法将连接设置为手动提交模式，然后使用commit()方法来提交事务或rollback()方法来回滚事务。
在JDBC Transaction中，开发人员需要显式地编写代码来控制事务的开始、提交和回滚，以确保数据操作的一致性和完整性。
2. Managed Transaction：
Managed Transaction是一种由容器（如应用服务器）管理的事务方式，通常与JTA（Java Transaction API）结合使用。在Java EE环境中，应用程序可以通过注解或配置来指定事务的范围和特性，由容器自动处理事务的管理。
Managed Transaction通常与容器管理的连接（Managed Connection）结合使用，应用程序不需要关心连接的打开、关闭和事务提交，而是由容器负责管理，从而简化了开发过程

## springboot事务处理
1. 使用的是JDBC Transaction（基于 JdbcTemplate）：底层是使用的
