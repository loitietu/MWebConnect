# MWebConnect : Websocket Server for Minecraft (Bedrock Edition)

## Document language
### [简体中文](README-zh.md)
### [English](README.md)

## How to use
1. Import `MWebConnect.jar` (You can download it in the Release page.).

2. Create a listening event for the server
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

3. Create a WebSocket server
```java
MyServer server = new MyServer();
server.newServer("127.0.0.1", 19132);
```

4. Running server
```java
server.run();
```

5. So you can connect to the server with the command /connect 127.0.0.1:19132 in Minecraft.

## Server event
1. Make sure to import two libraries, `com.loistudio.Session` and `com.loistudio.tools.EventEmitter`, before using this function.

### Currently owned events:
#### onMessage
This event will be triggered when the player sends a message.<br>
(String) message ; (String) sender
#### onMessageJSON
This event is triggered when the player sends a message, but it will send a json string.<br>
(String) json
#### onBlockPlaced
This event is triggered when the player places a box.<br>
(Player) player ; (Block) block
#### onBlockPlacedJSON
This event is triggered when the player places a box, but it will send a json string.<br>
(String) json
#### onBlockBroken
This event is triggered when the player breaks the box.<br>
(Player) player ; (Block) block
#### onBlockBrokenJSON
This event is triggered when the player breaks a box, but it will send a json string.<br>
(String) json
 - Note: These events must be monitored with the event variable in the Session.

### Event module usage indication
- Create variables before reuse `EventEmitter event = new EventEmitter();`.
#### Send event demonstration
```java
event.emit("message", "HelloWorld");
```
#### Monitor event demonstration
```java
event.on("message", new EventEmitter.Listener() {
    public void execute(Object... args) {
        System.out.println(args[0]);
    }
});
```
 - Note: the event module can only listen to the current variable. If it is created and sent in other classes, it cannot listen to the sent event in this variable unless it is sent in the current variable.


## File storage module
### JsonObject
 - Import the package `com.loistudio.file.JsonObject` before using it.
#### open(String path) -> void
Create a json file
- path {String} File path 
#### set(String key, Object value) -> void
Add or set a key value to a json file.
- key {String} Key of json file
- value {Object} Value of key
#### get(String key) -> Object
Gets the value of the specified key.
- key {String} Key of json file
#### delete(String key) -> void
Delete the specified key
- key {String} Key of json file

### MclStatic
 - Import the package `com.loistudio.file.MclStatic` before using it.
#### open(String path) -> void
Create a MclStatic file.
- path {String} File path
#### setKey(String key, String publicKey, String privateKey) -> void
Set encryption key
- key {String} encryption key
- publicKey {String} public key
- privateKey {String} private key
#### setArray(String arrayName, MclStaticArray array) -> void
Set array object
- arrayName {String} Array name
- array {MclStaticArray} Array object
#### getKey() -> String
Get the current encryption key
#### setConst(String key, String item) -> void
Set a constant value.
- key {String} Constant name
- item {String} Constant value
#### getConst(String key) -> String
Get constant value
- key {String} Constant name
#### getArray(String arrayName) -> MclStaticArray
Get an array object
- arrayName {String} Array name
#### deleteConst(String key) -> void
Delete the specified constant
- key {String} Constant name
#### addClass(String class) -> void
Create a storage class
- class {String} taxon
#### deleteClass(String class) -> void
Delete the specified storage class
- class {String} taxon
#### set(String class, String key, String item) -> void
Set a storage class variable
- class {String} taxon
- key {String} variable name
- item {item} variable value
#### get(String class, String key) -> void
Gets the variable value in the specified storage class
- class {String} taxon
- key {String} variable name
#### deleteKey(String class, String key) -> void
Deletes variables in the specified storage class
- class {String} taxon
- key {String} variable name
#### deleteArray(String arrayName) -> void
Delete array object
- arrayName {String} Array name