##社区项目

##资料
#####[Bootstrap官网教程]( https://v3.bootcss.com)
#####[Github API调用,实现登陆功能](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
#####[springboot官网](https://spring.io/projects/spring-boot/)

###Github登陆
####1.请求用户的GitHub身份
将登陆按钮指向官方规定的授权页面
####2.GitHub将用户重定向回您的网站
如果用户接受您的请求，GitHub会使用临时code代码参数以及您在state参数的上一步中提供的状态重定向回您的站点，并且使用code交换到access_token。
####3.使用访问令牌访问API
访问令牌允许您代表用户向API发出请求。

###tips:
为了以后方便修改，将登陆参数信息可以放到properties文件中，在程序中则使用@Value引用(IOC)

###利用session和cookies保持登陆状态
