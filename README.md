# 一.登陆部分
   
   ## 1.思路
   
  #### 本项目是通过用户的github账号授权进行登陆的，登陆后使用github中的个人信息，如头像，用户名等对论坛进行访问以及其他操作。
   具体思路参考
[GitHub 第三方登录](https://www.jianshu.com/p/78d186aeb526)
[Github API调用,实现登陆功能](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)


   
   ## 2.实现
  

#### OAuth 协议的认证和授权的过程如下：
1.用户在点击导航栏的登陆按钮后，转跳到 GitHub 用户授权页面， client_id 必须传
2.用户点击同意后，github会自动按照redirectUrl回到服务端("/callback")
3.服务端将之前设置好的clientsecret等信息封装到accessTokenDto中，服务端通过accessTokenDto调用https://github.com/login/oauth/access_token 这个api获得accessToken
4.获取到 access_token 后，再通过getUser调用 https://api.github.com/user?access_token=access_token 这个API，就可以获取到基本的用户信息了。
5.登陆成功后，向user数据库中加入这一user，并将token写入cookie，放在浏览器中
   
   ## 3.页面持久化
   为了检测用户是否登陆，我们引用处理器拦截器（HandlerInterceptor），创建sessionInterceptor实现这个接口，重写其中的prehandle方法。prehandle方法遍历所有cookies，若找到其中有一项名称为token，则判定已登陆。获取到token后根据token在数据库中找到user，并且将user写入session，交给客户端进行判断。当必须要登陆的部分需要对用户登陆信息进行校验时，用
   ```java
User user=(User)request.getSession().getAttribute("user");
if(user==null){    return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);}
```
   
   [session与cookie](https://www.cnblogs.com/8023-CHD/p/11067141.html)
   
   # 二.问题列表部分
   问题列表共分为三种：

1.    index页面显示所有问题的列表
2.    profile页面的个人问题列表
3.    search查询得到的问题列表

为了在前端显示这些结果，我们需要先从数据库中按照不同的需求查出问题，然后将问题中的信息以及分页封装到PaginationDto。
前端点击page的时候，后端根据page得到相对应的PaginationDto。
PaginationDto分为两个部分，一部分是根据信息查询出来的问题列表List，另一部分是与当前page匹配的pages列表和像nextpage等元素。

#### 分页逻辑在setPagination方法中

   
   
   
   
   # 三.编写及提交问题

1. 当第一次点进publish或编辑已有问题时，使用的是
```java  
@GetMapping("/publish") @GetMapping("/publish/{id}")
```
GET方法渲染页面，POST方法处理请求
2.当点击发布时，使用的是
```java
@PostMapping("/publish")
```
此时后端拿到前端输入的参数后，先要对参数是否完整进行验证。随后无论是新问题发布还是旧问题编辑，都使用createOrupdate方法放入数据库。该方法通过id判断两种类型。

tips:标签校验用到了lamda表达式和stream
```java
public static String filterInvalid(String tags){    
String[] split=StringUtils.split(tags,",");   
List<TagDto>tagDtos=get();   
List<String>tagList=tagDtos.stream().flatMap(tag ->tag.getTags().stream()).collect(Collectors.toList());//二维转换成一维    
String  invalid=Arrays.stream(split).filter(t->!tagList.contains(t)).collect(Collectors.joining());   
return invalid;}

```
参考资料：[Java8 stream](https://www.runoob.com/java/java8-streams.html)

# 四.评论与通知
评论分为对问题评论以及对评论回复
两种评论在js文件中使用的是不同的函数，最终前端返回的commentCreatedto中type（QUESTION(1), COMMENT(2);）不同。
一级评论的parentId指的是问题Id，二级评论的则是commentId。

#### 评论完成后
1.向数据库中insert该条评论
2.增加相对应的问题的评论数
```
update question
set view_account = view_account+#{viewAccount,jdbcType=INTEGER}where id=#{id}
```
3.创建通知，将通知放入notification数据库，用户点击通知栏时可以查看到receiver为自己的通知

#### 显示评论
listByTargetId(Long id, CommentTypeEnum type) 
点击问题页面或者一级评论下的按钮可以查看对应的评论，由于点击时希望看到评论者的用户名以及头像，因此需要将查出的comment转换成commentDto。
1.根据type和id从数据库中查出评论
2.获取去重的评论人,并且把Id放到userIds里面
3.获取评论人并且转换成Id和user的Map
4.转换comment为commentDto（增加User）

