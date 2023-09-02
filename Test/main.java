import com.loistudio.Server;
import com.loistudio.Session;
import com.loistudio.tools.EventEmitter;
import com.loistudio.file.MclStatic;

public class main extends Server {
    @Override
    public void connect(Session client) {
        //客户端连接
        client.tellraw("MWebConnect");
        Session.event.on("onMessage", new EventEmitter.Listener() {
            public void execute(Object... args) {
                System.out.println(args[0].toString() + " " + args[1].toString());
            }
        });
    }
    
    @Override
    public void close(Session client) {
        //客户端断开连接
    }
    
    @Override
    public void message(Session client, String msg) {
        //客户端发送消息
    }
    
    @Override
    public void create() {
        //服务器创建
    }
    
    //主要逻辑
    public static void main(String[] args) throws Exception {
        main server = new main();
        server.setModel(LOGGER_RELEASE);
        server.newServer("127.0.0.1", 24455);
        server.run();
        MclStatic mcl = new MclStatic();
        mcl.open("Test.mcl");
        mcl.addClass("Test");
        mcl.set("Test", "Test", "Test");
        mcl.close();
    }
}