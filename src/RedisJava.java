import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/*
这个类 实测试redis的 以后打算用redis缓存歌单 所以这个类暂时没用
 */
public class RedisJava {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("127.0.0.1");
//        jedis.auth("123456");
//        System.out.println("连接成功");
//        List list=jedis.lrange("zyk1",0,jedis.llen("zyk1"));
//        for (Object s:list
//             ) {
//            System.out.println(s);
//        }
//        File file=new File("E://");
//        List<File> list= Arrays.asList(file.listFiles());
//        int i=0;
//        for (File f:list
//             ) {
//          if(!f.getName().endsWith("m4a")) continue;
//            System.out.println("{");
//            System.out.println("id:\""+i+"\",");
//            System.out.println("title:\""+f.getName()+"\",");
//            System.out.println("singer:\""+"你猜\",");
//            System.out.println("songUrl:\""+"./songs/"+f.getName()+"\",");
//            System.out.println("imageUrl:'./images/songs/c.jpg',");
//            System.out.println("}");
//            System.out.println(",");
//        i++;
//        }

    }
}