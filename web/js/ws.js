//作为客户端的唯一标志
var id="";
//请求得到uuid
$.ajax({
    url:"http://127.0.0.1:8080/web1_war_exploded/getid",
    datatype:"text",
    type:"post",
    async:false,
    success:function (d) {
        id=d;
    }
});
document.title=id;
var websocket = null;
//得到播放器这个实例 在player.js里面
var player=Player.instance;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost:8080/web1_war_exploded/websocket/"+id);
}
else {
    alert('当前浏览器 Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    setMessageInnerHTML("WebSocket连接发生错误");
    alert("连接失败");
};

//连接成功建立的回调方法
websocket.onopen = function () {
    setMessageInnerHTML("WebSocket连接成功");
}

//接收到消息的回调方法
websocket.onmessage = function (event) {
    setMessageInnerHTML("收到"+event.data);
    //如果返回的是json
    if(event.data.startsWith("{")){
        //切歌触发 实现切歌同步
       if(event.data.search("songUrl")==-1){
           let json_object=JSON.parse(event.data);
           player.my_change(json_object.song_id);
           if(json_object.song_status) player.my_pause();
           else player.my_play();
           player.audio.currentTime=json_object.song_time;
           return;
       }
       //触发添加歌曲 实现添加歌曲同步
       else if(event.data.search("songUrl")!=-1){
           let json_object=JSON.parse(event.data);
           let id=player.musics.songs.length;
           let song_name=json_object.title;
           let singer= json_object.singer;
           let song_url=json_object.songUrl;
           let ts={
               id: id,
               title:song_name ,
               singer:singer,
               songUrl: song_url,
               imageUrl: './images/songs/c.jpg'
           };
           player.musics.songs.push(
               ts
           );
           player.renderSongList();
           return;
       }
    }
    //播放 或者 暂停 的信号 第三个是服务端请求得到当前的播放状态 包括播放的索引 当前播放的时间 以及 是否正在播放
    switch (event.data) {
        case "play":player.my_play();break;
        case "pause":player.my_pause();break;
        case "get_now_info": let info=player.get_now_info();
        // console.log("send"+JSON.stringify(info));
        websocket.send(JSON.stringify(info));break;

    }
}

//连接关闭的回调方法
websocket.onclose = function () {
    setMessageInnerHTML("WebSocket连接关闭");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    console.log(innerHTML);
}

//关闭WebSocket连接
function closeWebSocket() {
    alert("发生错误");
    websocket.close();
}

//发送消息
function send() {
    // var message = document.getElementById('text').value;
    websocket.send(message);
}

