import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * 这个servlet客户端请求会得到一个字符串 这个字符串就作为每个客户端的唯一标志
 */
@WebServlet(name = "Servlet",urlPatterns = "/getid")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        在线人数大于2直接返回 就不会连接到websocket
         */
        if(WebSocketTest.getOnlineCount()>=2){
            return;
        }
        //设置允许跨域
        response.setHeader("Access-Control-Allow-Origin","*");
        //允许请求的方法
        response.setHeader("Access-Control-Allow-Methods","GET,POST");
        PrintWriter out = response.getWriter();
        String id=UUID.randomUUID().toString().replaceAll("-","");
        //第一个连接 让他的唯一标志 大一个长度 便于区分
        if(WebSocketTest.getOnlineCount()==0){
            id+="0";
        }
        //向前端返回这个字符串
        out.println(id);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
}
