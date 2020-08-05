### Tomcat

> Tomcat是Java Servlet, Jsp, Java webSocket的实现

#### 为什么说Tomcat是Servlet容器

因为Tomcat中持有多个Servlet，实现了Servlet规范（request，response，filter）

#### Servlet容器

web项目--->Context标签--->Context.class--->StandardContext--->loadOnStartup()  

Tomcat中的wrapper就是Servlet：

> 加载：ContextConfig.webConfig()—>getContextWebXmlSource()—>Constants.ApplicationWebXml
> 解析：ContextConfig.webConfig()—>configureContext(webXml)—>context.createWrapper()  

#### Tomcat架构图

![image-20200805131317875](Tomcat.assets/image-20200805131317875.png)



### Tomcat各组件的含义

* **Server**：In the Tomcat world, a Server represents the whole container.  （在Tomcat的世界中，一个Server表示整个容器）
* **Service**：A Service is an intermediate component which lives inside a Server and ties one or more Connectors to exactly one Engine  （Service是驻留在Server内部，将一个或多个连接器绑定到一个确切的Engine 的中间组件）
* **Connector**：A Connector handles communications with the client. There are multiple connectors available with Tomcat  （连接器用来处理与客户端的交互，Tomcat中有多个可用的连接器，包含Http连接器和AJP连接器）
* **Engine**：An Engine represents request processing pipeline for a specific Service. As a Service may have multiple Connectors, the Engine receives and processes all requests from these connectors, handing the response back to the appropriate connector for transmission to the client  （Engine代表一个特定服务的请求处理管道。一个服务可能有多个连接器，Engine从这些连接器接收并处理所有请求，将响应传递给适当的连接器以传输到客户端）
* **Host** ：A Host is an association of a network name, e.g. www.yourcompany.com, to the Tomcat server. An Engine may contain multiple hosts, and the Host element also supports network aliases such as yourcompany.com and abc.yourcompany.com  （Host是一个相关联的网络名称，一个Engine可以包含多个host，host支持网络别名）
* **Context**  ：A Context represents a web application. A Host may contain multiple contexts, each with a unique path  （context表示一个web应用，一个host可以包含多个唯一路径的context）





