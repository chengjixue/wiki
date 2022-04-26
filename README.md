# wiki项目搭建问题总结

<h2>热部署3步骤</h2>

```java
1.添加 spring-boot-devtols(在pom.xml文件中引入)
2.打开顶部工具栏 File -> Settings -> Default Settings -> Build -> Compiler 然后勾选 Build project automatically 。
3.Ctrl + Shift + Alt + / 然后进入Registry ，勾选自动编译并调整延时参数。compiler.automake.allow.when.app.running -> 自动编译
4.compile.document.save.trigger.delay -> 自动更新文件
```

## Long类型精度丢失问题

在传值到前端的过程中，因为id为雪花算法生成的id传到前端的时候long类型的精度出现丢失问题，导致页面刷新后想删除改数据删除不了（原因：ID对不上）

解决办法

```java
//在resp包中在类型为Long类型的全部加上标明序列化方式（代码量不大采用）
@JsonSerialize(using = ToStringSerializer.class)
```

## CommonResp类的作用

在返回前端数据的时候，当多个对象都要返回时造成返回值混乱，

所以创建CommonResp<T>类的

```java
public class CommonResp<T> {
    /*
     * 业务上的登录成功或失败
     * */
    private boolean success = true;
    //    返回信息
    private String message;
    //    返回泛型数据，自定义数据类型
    private T content;
```



## CopUtil工具的作用



用来复制实体类的信息到Resp类的一个工具类

分别有单体复制和列表复制

```java
 /**
     * 单体复制
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BeanUtils.copyProperties(source, obj);
        return obj;
    }

    /**
     * 列表复制
     */
    public static <T> List<T> copyList(List source, Class<T> clazz) {
        List<T> target = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source)){
            for (Object c: source) {
                T obj = copy(c, clazz);
                target.add(obj);
            }
        }
        return target;
    }
```

## LogApsect的作用

就是在Controller层增加一个环绕通知在执行前打印请求参数，接口耗时，增加日志流水

```java
@Aspect
@Component
public class LogAspect {

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /** 定义一个切点 */
    @Pointcut("execution(public * edu.xuecj.*.controller..*Controller.*(..))")
    public void controllerPointcut() {}
	 @Autowired
    private  SnowFlake snowFlake;
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        //        增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印请求信息
        LOG.info("------------- 开始 -------------");
        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址: {}", request.getRemoteAddr());


        // 打印请求参数
        Object[] args = joinPoint.getArgs();
		// LOG.info("请求参数: {}", JSONObject.toJSONString(args));

		Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("请求参数: {}", JSONObject.toJSONString(arguments, excludefilter));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("返回结果: {}", JSONObject.toJSONString(result, excludefilter));
        LOG.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }
}

```

## 更改日志

日志级别有**trace<debug<info<warn<error<fatal**

设置方法为在application文件添加

```java
logging.level.edu.xuecj.wiki.mapper=trace
```

## PageReq和PageResp的作用

用来对前端请求的分页参数进行接受和返回分页后的数据

```java
public class PageReq {
    private int page;
    private int size;}


public class PageResp<T> {
    private long total;
    private List<T> list;}
```

## 自定义异常类

针对一些常见的故障进行了自定义异常

```java
 /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(BindException e) {
        CommonResp commonResp = new CommonResp();
        LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResp;
    }

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(BusinessException e) {
        CommonResp commonResp = new CommonResp();
        LOG.warn("业务异常：{}", e.getCode().getDesc());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getCode().getDesc());
        return commonResp;
    }

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp validExceptionHandler(Exception e) {
        CommonResp commonResp = new CommonResp();
        LOG.error("系统异常：", e);
        commonResp.setSuccess(false);
        commonResp.setMessage("系统出现异常，请联系管理员");
        return commonResp;
    }
```

## 集成redis（把实体信息放入redis）

1. ```java
   //增加返回字段对实体进行序列化
   implements Serializable 
   
   private String token;
   
       public String getToken() {
           return token;
       }
   
       public void setToken(String token) {
           this.token = token;
       }
   //注入
   @Resource
       private RedisTemplate redisTemplate;
   
   //过程
   //        生成单点登录token，并放入redis redis转化为String类型
           Long token = snowFlake.nextId();
           System.out.println(token);
           userLoginResp.setToken(token.toString());
           redisTemplate.opsForValue().set(token.toString, userLoginResp, 60 * 60 * 24, TimeUnit.SECONDS);
   
   //配置redis
   # Redis服务器地址
   spring.redis.host=127.0.0.1
   # Redis服务器连接端口
   spring.redis.port=6379
   # Redis服务器连接密码（默认为空）
   spring.redis.password=
   ```

## 获取ip和对点赞进行限制

```
在拦截器中使用
/**
 * 使用nginx做反向代理，需要用该方法才能取到真实的远程IP
 * @param request
 * @return
 */
public String getRemoteIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    }
    return ip;
}

 public void vote(Long id) {
//        远程ip+doc。id作为key，24小时不能重赴
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("doc_vote" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }
```

## 定时任务

```java
在启动类中加入注解
@EnableScheduling

@Component
public class TestJob {
    private static final Logger LOG= LoggerFactory.getLogger(TestJob.class);

    /*
    * 固定时间间隔，fixedRate单位毫秒
    * */
    @Scheduled(fixedRate = 1000)
    public void simple(){
        SimpleDateFormat formatter=new SimpleDateFormat("mm:ss");
        String dateString=formatter.format(new Date());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("每隔5秒执行一次 {}",dateString);
    }

    /*
    * 自定义cron表达式执行
    * 只有等上一次的执行难完成才会执行下一次，错过就错过
    * */
    @Scheduled(cron = "0/2 * * * * ?")
    public void cron(){
        SimpleDateFormat formatter=new SimpleDateFormat("mm:ss SSS");
        String dateString=formatter.format(new Date());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("每隔2秒执行一次 {}",dateString);
    }
}
```

## 集成WebSocket进行信息推送通知

```java
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}


@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG= LoggerFactory.getLogger(WebSocketServer.class);
    /*
    * 每个客户端一个token
    * */
    private String token="";
    private static HashMap<String, Session> map=new HashMap<>();

    /*
    * 链接成功
    * */
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token){
        map.put(token,session);
        this.token=token;
        LOG.info("链接成功:token:{},session_id{},当前连接数{}",token,session.getId(),map.size());
    }
    /*
    * 连接关闭
    * */
    @OnClose
    public void onClose(Session session){
        map.remove(this.token);
        LOG.info("断开连接:token:{},session_id:{},当前连接数{}",this.token,session.getId(),map.size());
    }
    /*
    * 收到消息
    * */
    @OnMessage
    public void onMessage(String message,Session session){
        LOG.info("收到消息:{},内容：{}",token,message);
    }
    /*
    * 链接错误
    * */
    @OnError
    public void onError(Session session,Throwable error){
        LOG.error("错误:{}",error.getMessage());
    }
    /*
    * 群发消息
    * */
    public void sendInfo(String message){
        for(String token:map.keySet()){
            Session session=map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("推送消息失败:{}，内容{}",token,message);
            }
            LOG.info("推送消息成功:{}，内容{}",token,message);
        }
}

}


在server层注入和完成推送信息的组装
  @Resource
    private WebSocketServer webSocketServer;
//        推送消息
        Doc docDB=docMapper.selectByPrimaryKey(id);
        webSocketServer.sendInfo("【"+docDB.getName()+"】被点赞了");
        System.out.println("【"+docDB.getName()+"】被点赞了");
```



## SpringBoot实现异步化

在启动类添加注解@EnableScheduling

```java


@Service
public class WsService {

    @Resource
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message){
        webSocketServer.sendInfo(message);
    }
}

    public void vote(Long id) {
//        远程ip+doc。id作为key，24小时不能重赴
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("doc_vote" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
//        推送消息

        Doc docDB=docMapper.selectByPrimaryKey(id);
        wsService.sendInfo("【"+docDB.getName()+"】被点赞了");
     
    }

```

