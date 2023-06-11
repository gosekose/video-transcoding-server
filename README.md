동영상 플랫폼 서비스를 위한 서버
======================
<br/><br/>

# 1. 주어진 동영상의 화질 및 포멧을 변경하여 저장소에 저장하기 위한 변환 로직 구현
- 대규모 시스템 설계에서 동영상 파일은 트랜스 코딩 서버에서 다양한 포멧으로 바꿀 필요성이 있습니다.
  - 클라이언트의 네트워크 상황에 따라 화질이 다른 영상을 제공하기 위해 미리 포멧팅하여 원본 저장소에 저장하도록 하였습니다. 
- 변화 이미지 결과 
- ![스크린샷 2023-06-09 22-52-37](https://github.com/gosekose/video-transcoding-server/assets/88478829/ac3a177d-8aeb-4e60-9668-c7caac5f4f57)


# 2. 서버 시나리오
- api 서버: Transcode 요청 메세지 보낸 서버
- transCode 서버: Transcode 메세지를 받고 동영상 트랜스코드(포멧 변경 등) 진행, 완료 큐 보내는 서버
- transCode handler 서버: Transcode 완료 메세지를 받고 캐시 및 메타 데이터 처리하는 서버

# 3. 고려해야할 사항 (완성해야할 로직)
- rabbitMQ로 메세지를 받은 후 트랜스코드를 진행할 때, 예외가 발생하는 경우 해당 메세지는 유실되는 문제 발생 함
- 트랜스코드 결과에 대한 메세지를 메세지를 api 서버에 보내고, 트랜스코드를 성공하는 경우 transcode handler에 메세지를 보내야 함
