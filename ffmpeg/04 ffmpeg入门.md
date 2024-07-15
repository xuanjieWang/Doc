## ffmpeg
1. 录制，转换，流话音视频的完整解决方案
2. ffmpeg.exe,ffplay.exe,ffserver.exe
3. 视频采集，屏幕录制，支持RTP流传送支持的RTSP的流媒体服务器
4. 视频格式转换，转换为flv
5. 视频截图功能，添加水映功能
6. 解协议：http,rtmp 解封装格式(音频压缩数据，视频压缩数据)，音频解码，视频解码，音频采样数据pcm，视频采样数据yuv，音视频同步
7. 同步策略： 确定一个时钟，判断差异，


## 常用命令
搜索本地的所有设备： ffmpeg -list_devices true -f dshow -i dummy
打开本地的摄像头： ffplay -f dshow -i video="Integrated Camera"

本地文件推流
ffmpeg -re -i ande10.mp4 -rtsp_transport tcp -vcodec libx264 -f rtsp rtsp://localhost/test1

提取本地音频  ac音频通道数，ar采样率
ffmpeg -i test.mp4 -vn -aframes 180 -acodec libmp3lame -ac 2 -ar 48K -y test01.mp3

添加字幕
  
-a音频 -v视频 -s字幕


字幕： srt-ass
添加字幕流： ffmpeg -i test.mp4 -vf subtitles=0.srt -y stest.mp4
字幕转化： ffmpeg -i 0.srt 1.ass
添加字幕流： ffmpeg -i test.mp4 -i 1.ass -c copy -y s1test.mkv

ffmpeg -i test.mp4 -i 0.srt -c copy -c:s mov_text output3.mp4

## 主要参数
1. -i设置输出流
2. -f 设置输出格式
3. -ss 开始时间
4. -b 设置视频流量 200kb/s
5. -r 设置帧速率 25
6. -s 设置画面的宽和高
7. -aspect 设置画面的比例
8. -vn 不处理视频
9. -vcode 设置视频编码器，设置输入流相同的编解码器，

## 音频
1. ar采样率
2. an音频通道数
3. -acodec 设置声音编码器
4. -an 不出来音频

## 转换，视频之间的转换和音频之间的转换
1. ffmpeg -i input_test.mp4 -vn -acodec copy output test.flv
2. ffmpeg -i input_test.aac -vn -acodec copy output_test.mp3

## 抽取视频中的音频
1. ffmpeg -i input_test.mp4 -vn -y -acodec copy output_test.aac
2. ffmpeg -i input_test.mp4 -vn -y -acodec copy output_test.mp4

## 抽取视频中的视频
1. ffmpeg -i input.mp4 -vcodec copy -an output.avi
2. ffmpeg -i input.mp4 -vcodec copy -an output.mp3

 
