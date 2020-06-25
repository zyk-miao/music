import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
这个类是用爬虫爬取qq音乐的歌曲信息
 */
public class Pmusic {
    public static void main(String[] args) {
        try {
//            Pmusic.get_music(10,"坏女孩");
            System.out.println( Pmusic.get_music(5,"坏女孩"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List get_music_info(int j,String name) throws IOException {
        List music_list = new ArrayList();
//        String name = "什么都可以";
        String page = "0";
        String num = Integer.toString(j);
        String url = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=" + page + "&n=" + num + "&w=" + name;
        Connection.Response res = Jsoup.connect(url)
                .header("Origin", "https://y.qq.com")
                .header("Referer", "https://y.qq.com/portal/search.html")
                .header("Sec-Fetch-Mode", "cors")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                .timeout(10000).ignoreContentType(true).execute();
        String body = res.body();
        //截取返回字符串 将其转变为json格式
        body = body.substring(9, body.length() - 1);
        JSONObject json = JSONObject.fromObject(body);
        JSONArray json_music_list = json.getJSONObject("data").getJSONObject("song").getJSONArray("list");
//        System.out.println(json_music_list);
        for (int i = 0; i < json_music_list.size(); i++) {
//            System.out.println(i);
//            System.out.println(json_music_list.getJSONObject(i).getString("songname"));
//            System.out.println(json_music_list.getJSONObject(i).getJSONArray("singer").getJSONObject(0).getString("name"));
//            System.out.println(json_music_list.getJSONObject(i).getString("songmid"));
//            System.out.println(json_music_list.getJSONObject(i).getString("media_mid"));
            String songname = json_music_list.getJSONObject(i).getString("songname");
            String singer = json_music_list.getJSONObject(i).getJSONArray("singer").getJSONObject(0).getString("name");
            String songmid = json_music_list.getJSONObject(i).getString("songmid");
            String media_mid = json_music_list.getJSONObject(i).getString("media_mid");
            music_info music_info = new music_info(songname, singer, songmid, media_mid);
            music_list.add(music_info);
//            System.out.println(music_list.toString());
        }
        return music_list;

    }

    public static List get_purl(List<music_info> music_list) throws IOException {
        List music_data_list = new ArrayList();
        for (int i = 0; i < music_list.size(); i++) {
            String songname = music_list.get(i).songname;
            String singer = music_list.get(i).singer;
            String songmid = music_list.get(i).songmid;
            String url= "https://u.y.qq.com/cgi-bin/musicu.fcg?data={\"req\":{\"module\":\"CDN.SrfCdnDispatchServer\",\"method\":\"GetCdnDispatch\",\"param\":{\"guid\":\"703417739\",\"calltype\":0,\"userip\":\"\"}},\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"703417739\",\"songmid\":[\""+songmid+"\"],\"songtype\":[0],\"uin\":\"\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":\"\",\"format\":\"json\",\"ct\":24,\"cv\":0}}";
//            System.out.println(url);
            Connection.Response res = Jsoup.connect(url)
                    .header("Origin", "https://y.qq.com")
                    .header("Referer", "https://y.qq.com/portal/search.html")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                    .timeout(10000).ignoreContentType(true).execute();
            String body = res.body();
            JSONObject json = JSONObject.fromObject(body);
            String purl=json.getJSONObject("req_0").getJSONObject("data").getJSONArray("midurlinfo").getJSONObject(0).getString("purl");
            String full_purl=  "http://dl.stream.qqmusic.qq.com/" + purl;
            music_info_with_purl m=new music_info_with_purl(songname,singer,full_purl);
            if(full_purl.endsWith("/")) continue;
            music_data_list.add(m);
        }
        return music_data_list;

    }
public static List get_music(int i,String name) throws IOException {
   return get_purl(get_music_info(i,name));
}

}

class music_info {
    String songname;
    String singer;
    String songmid;
    String media_mid;

    public music_info(String songname, String singer, String songmid, String media_mid) {
        this.songname = songname;
        this.singer = singer;
        this.songmid = songmid;
        this.media_mid = media_mid;
    }

    @Override
    public String toString() {
        return "music_info{" +
                "songname='" + songname + '\'' +
                ", singer='" + singer + '\'' +
                ", songmid='" + songmid + '\'' +
                ", media_mid='" + media_mid + '\'' +
                '}';
    }
}

class music_info_with_purl {
    String songname;
    String singer;
    String full_media_url;

    public music_info_with_purl(String songname, String singer, String full_media_url) {
        this.songname = songname;
        this.singer = singer;
        this.full_media_url = full_media_url;
    }
}
