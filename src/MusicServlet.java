import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
/*
这个servlet是用来搜索歌曲的接口 返回json字符串 内容有歌曲名，歌手，和播放地址
 */
@WebServlet(name = "MusicServlet", urlPatterns = "/getmusic")
public class MusicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许请求的方法
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        response.setContentType("text/html,charset=utf-8");
        //设置编码 否则中文会乱码
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String song_name = request.getParameter("song_name");
        List<music_info_with_purl> list = Pmusic.get_music(5, song_name);
        JSONArray json=new JSONArray();
        //list转json
        for (music_info_with_purl m:list
             ) {
            JSONObject j=new JSONObject();
            j.put("song_name",m.songname);
            j.put("singer_name",m.singer);
            j.put("song_url",m.full_media_url);
            json.add(j);
        }
        out.println(json.toString());
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

    }
}
