# MWebConnect : Websocket Server for Minecraft (Bedrock Edition)

## 文档语言
### [简体中文](README-zh.md)
### [English](README.md)

## 使用方法
1. 导入 `MWebConnect.jar` (这个包你可以在Release中下载).

2. 创建服务器监听事件
```java
class MyServer extends Server {
    @Override
    public void connect(Session client) {
        //Client connection
    }
    
    @Override
    public void close(Session client) {
        //Client disconnects.
    }
    
    @Override
    public void message(Session client, String msg) {
        //Client sends message
    }
    
    @Override
    public void create() {
        //Server creation
    }
}
```

3. 创建WebSocket服务器
```java
MyServer server = new MyServer();
server.newServer("127.0.0.1", 19132);
```

4. 运行服务器
```java
server.run();
```

5. 这样你就可以在Minecraft (Bedrock Edition)中通过/connect 127.0.0.1:19132进行连接服务器了

## 服务器事件功能
1. 再使用这个功能之前请确保你已经导入了`com.loistudio.Session` 和 `com.loistudio.tools.EventEmitter`这两个包

### 监听器事件:
#### onMessage
当玩家发送消息时将触发这个事件。<br>
(String) message ; (String) sender
#### onMessageJSON
当玩家发送消息时将触发这个事件，但是它会发送一个json字符串。<br>
(String) json
#### onBlockPlaced
当玩家放置方块时将触发这个事件。<br>
(Player) player ; (Block) block
#### onBlockPlacedJSON
当玩家放置方块时将触发这个事件，但是它会发送一个json字符串。<br>
(String) json
#### onBlockBroken
当玩家破坏方块时将触发这个事件。<br>
(Player) player ; (Block) block
#### onBlockBrokenJSON
当玩家破坏方块时将触发这个事件，但是它会发送一个json字符串。<br>
(String) json
 - 注意: 必须使用Session中的event变量来监听这些事件

### 事件模块使用示范
- 使用前先创建对象 `EventEmitter event = new EventEmitter();`.
#### 发送事件示范
```java
event.emit("message", "HelloWorld");
```
#### 监听事件示范
```java
event.on("message", new EventEmitter.Listener() {
    public void execute(Object... args) {
        System.out.println(args[0]);
    }
});
```
 - 注意:事件模块只能监听当前变量。如果它是在其他类中创建并发送的，它就不能监听这个变量中的event事件，除非它是在当前变量中发送的