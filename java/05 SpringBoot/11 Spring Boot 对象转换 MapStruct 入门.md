### 分层概念
DO（Data Object）：与数据库表结构一一对应，通过DAO层向上传输数据源对象。        数据库层
DTO（Data Transfer Object）：数据传输对象，Service或Manager向外传输的对象。    
BO（Business Object）：业务对象。由Service层输出的封装业务逻辑的对象。          包装层

那么，进行就需要这些对象的转换。例如说：
```java
// 从数据库中查询用户
UserDO userDO = userMapper.selectBy(id);

// 对象转换
UserBO userBO = new UserBO();
userBO.setId(userDO.getId());
userBO.setUsername(userDO.getUsername());
// ... 还有其它属性
```
####还可以使用  Spring BeanUtils    Apache BeanUtils 来实现
