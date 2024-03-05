### 解析
1. MyBatis 中有两个重要的解析器，分别是 SQL 解析器和 XML 配置文件解析器。
2. SQL 解析器：MyBatis 中的 SQL 解析器负责解析 SQL 语句中的 #{}, ${} 等参数占位符，并将其替换为具体的数值或字符串值。SQL 解析器还可以对 SQL 语句进行预编译，防止 SQL 注入攻击，并将参数值安全地传递给数据库执行相应的操作。
3. XML 配置文件解析器：MyBatis 通过 XML 配置文件来配置 SQL 映射关系、参数映射等内容，XML 配置文件解析器负责解析这些 XML 文件，将其中的配置信息加载到内存中，并根据配置信息构建相应的对象，如 SqlSession、MappedStatement 等，以便后续的 SQL 执行操作。

### GenericTokenParser普通记号解析器，处理#{}和${}参数
2. #{}: 预编译使用占位符（解析为字符串）， ${}使用动态凭借sql语句，参数值直接替换到sql相应位置
3. 首先，代码创建了一个 StringBuilder 对象 builder，用于构建最终的结果字符串。
4. new GenericTokenParser("#{", "}", handler); parse(String text) ，转化为CharAyyay循环遍历，如果看到是\\转义字符将反斜杠去除变化为普通字符串
5. 然后，通过检查传入的文本字符串 text 是否为空且长度大于 0，来确定是否需要进行解析。
6. 接下来，将文本字符串转换为字符数组 src，并设置初始偏移量 offset 为 0。
7. 代码使用 indexOf 方法寻找开头占位符 openToken 在文本中的位置，如果找到了，则进入循环。
8. 在循环中，首先判断开头占位符前是否有反斜杠 \，如果有，则说明该占位符被转义了，需要将反斜杠去除，并将结果添加到 builder 中。
9. 如果没有反斜杠，则寻找结束占位符 closeToken 在文本中的位置。如果没有找到结束占位符，则将剩余的文本直接添加到 builder 中，并将偏移量 offset 设置为文本末尾的位置。
10. 如果找到了结束占位符，则将开头占位符和结束占位符之间的内容提取出来，并调用 handler.handleToken 方法进行处理。处理结果会添加到 builder 中，并将偏移量 offset 设置为结束占位符的位置。
11. 最后，再次检查偏移量 offset 是否小于字符数组 src 的长度，如果小于，则将剩余的文本添加到 builder 中。

### PropertyParser： 属性解析器
``` java
  public static String parse(String string, Properties variables) {
    // 使用属性解析器处理器
    VariableTokenHandler handler = new VariableTokenHandler(variables);
    // 解析${}的动态sql
    GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
    return parser.parse(string);
  }

  private static class VariableTokenHandler implements TokenHandler {
    private Properties variables;

    public VariableTokenHandler(Properties variables) {
      this.variables = variables;
    }

    //属性解析器的方法，属性如果包含文本就添加上${}
    @Override
    public String handleToken(String content) {
      if (variables != null && variables.containsKey(content)) {
        return variables.getProperty(content);
      }
      return "${" + content + "}";
    }
  }
}


```

### 在 MyBatis 中，PropertyParser 和 GenericTokenParser 都是用来解析占位符的工具类，但它们的作用和用法略有不同。

#### PropertyParser：
1. PropertyParser 用于解析属性占位符，例如 ${property} 形式的占位符。
2. 它的主要作用是将占位符中的属性名替换为实际的属性值。
3. PropertyParser 还支持默认值的设置，当属性值不存在时可以使用默认值进行替换。

#### GenericTokenParser：
1. GenericTokenParser 用于通用的占位符解析，支持自定义的占位符格式。
2. 它的主要作用是在字符串中解析自定义格式的占位符，例如 ${...}、#{...} 等。
3. GenericTokenParser 可以接受一个 TokenHandler 对象，用于处理解析后的占位符内容，可以根据具体需求进行自定义处理。

## xml解析器

### XNode
1. XNode 包含了节点的名称、属性、子节点等信息，并提供了一些方法来方便地获取和操作这些信息。通过 XNode，MyBatis 可以方便地遍历和解析 XML 配置文件，并将配置信息转换为相应的数据结构供框架内部使用。
2. 在 MyBatis 源代码中，XNode 类通常被用于解析和处理 XML 配置文件中的各种配置信息，例如 SQL 映射文件中的 SQL 语句、参数映射等。它提供了一些方便的方法来获取节点的名称、属性值、子节点等信息，以及对这些信息进行进一步处理。
3. 总的来说，XNode 在 MyBatis 中扮演着连接 XML 配置文件和框架内部逻辑的重要角色，它提供了一种方便的方式来处理和操作 XML 配置信息，为 MyBatis 的功能实现提供了基础支持。

### XPathParser
XPathParser 是 MyBatis 中的一个工具类，用于解析 XML 配置文件和 XML 字符串。它基于 XPath 技术，提供了一种方便的方式来定位和提取 XML 中的节点和属性。
1. XML 解析：XPathParser 可以将 XML 字符串或者 XML 文件解析成一个 DOM 树，方便后续的节点查询和操作。
2. 节点查询：XPathParser 支持使用 XPath 表达式来查询指定的节点。通过调用 evaluate 方法，并传入 XPath 表达式，可以获取匹配的节点集合或单个节点。
3. 属性获取：XPathParser 支持通过节点路径和属性名称来获取指定节点的属性值。通过调用 getNodeAttrValue 方法，并传入节点路径和属性名称，可以获取对应节点的属性值。
4. 动态 SQL 解析：XPathParser 在 MyBatis 中常被用于解析 XML 配置文件中的动态 SQL。它可以将包含占位符的 SQL 语句解析成一个完整的 SQL 字符串，替换掉占位符部分。
5. XPathParser 在 MyBatis 的内部使用中发挥着重要作用，它能够方便地解析和操作 XML 配置信息，为框架的功能实现提供基础支持

### XPathParser 和 XNode 在 MyBatis 中都是用于处理 XML 配置文件的工具类，但它们的作用和功能略有不同。
#### XPathParser：

1. XPathParser 是基于 XPath 技术的 XML 解析工具类，用于解析整个 XML 文档，并支持使用 XPath 表达式来查询和定位节点。
2. 主要功能包括将 XML 字符串或文件解析成 DOM 树，支持节点查询和属性获取，以及动态 SQL 解析等。
3. XPathParser 提供了一种通用的方式来解析 XML 配置信息，并可以通过 XPath 表达式精确地定位和提取需要的节点和属性。

#### XNode：
1. XNode 是表示 XML 配置文件中某个节点的对象，主要用于在 MyBatis 内部处理 XML 配置信息时使用。
2. XNode 包含了节点的名称、属性、子节点等信息，并提供了一些方法来方便地获取和操作这些信息。
3. XNode 通常在 MyBatis 内部处理 XML 配置文件时被使用，用于表示和操作配置文件中的具体节点信息。
4. 总的来说，XPathParser 更偏向于 XML 解析和查询的通用工具类，而 XNode 则更专注于表示和操作 XML 配置文件中的具体节点信息。在实际应用中，XPathParser 用于解析整个 XML 文档并进行高级定位和查询，而 XNode 则用于处理 XML 文件中具体的节点信息。
