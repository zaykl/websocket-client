[![Build Status](https://travis-ci.org/hashio/websocket-client.png)](https://travis-ci.org/hashio/websocket-client)

WebSocket Client
=================
Copyright 2011,2012 Takahiro Hashimoto

MIT license

Purpose
-------
portable,high-performance and more connectivity for internet

Support
-------

- JDK5 or higher
- WebSocket Specification RFC6455, Draft06,76
- wss:// SSL support
- http proxy support with authentication [Basic,Digest,Negotiate(Windows only)]


Tested server

- [RFC6455] netty
- [RFC6455 + Proxy] websocket.org echo application
- [Draft76] Grizzly2.0
- [Draft06] Grizzly2.1.1
- [Draft76] Jetty7.4.0
- [Draft06] Jetty7.4.0
- [Draft06 + Proxy] Apache(mod_proxy) + Jetty7.4.0


Requirements
-----------
- JDK5 or higher

Build And Install
=================

+ build with maven

```shell
git clone https://github.com/hashio/websocket-client.git
cd websocket-client
mvn clean install
```

+ add websocket-client dependency to the pom.xml of your application

```xml
<dependency>
  <groupId>jp.a840.websocket</groupId>
  <artifactId>websocket-client</artifactId>
  <version>0.8.5-SNAPSHOT</version>
</dependency>
```

Usage
=====

```java
// create RFC6455 protocol websocket client
WebSocket socket = WebSockets.create("ws://example.com/", new WebSocketHandler() {
    public void onOpen(WebSocket socket) {
         // TODO implement onOpen event
    }
    public void onMessage(WebSocket socket, Frame frame) {
         System.out.println(frame);
    }
    public void onError(WebSocket socket, WebSocketException e) {
         // TODO implement onError event
    }
    public void onClose(WebSocket socket) {
         // TODO implement onClose event
    }
}, null);

// connect
socket.connect();
...
socket.close();
```

#### Example: Sample of Jetty7 websocket chat servlet

```java
WebSocket socket = WebSockets.createDraft06("ws://localhost:8080/ws/", new WebSocketHandler() {
    public void onOpen(WebSocket socket) {
        System.err.println("Open");
        try {
            socket.send(System.getenv("USER") + ":has joined!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    public void onMessage(WebSocket socket, Frame frame) {
        if(!frame.toString().startsWith(System.getenv("USER"))){
            try {
                socket.send(System.getenv("USER") + ":(echo)" + frame.toString());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(frame);
    }
        
    public void onError(WebSocket socket, WebSocketException e) {
        e.printStackTrace();
    }
        
    public void onClose(WebSocket socket) {
        System.err.println("Closed");
    }
}, "chat");

socket.setBlockingMode(false);
socket.connect();
socket.awaitClose();
```
