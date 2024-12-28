![image](https://github.com/user-attachments/assets/b08e976f-def8-4921-b15d-f3e4d5808786)


<div align="center">
  <h1>Music Road</h1>
  길 위에서 만나는 모두의 노래, MusicRoad는 위치 기반 노래 공유 및 플레이어 서비스입니다.
</div>

<br>

### 🔔 서비스 배경
> “지금 무슨 노래 듣고 계세요? 뉴진스의 하입보이요”

길거리에서 지나치는 수많은 사람들이 어떤 노래를 듣고 있는지 궁금한 적 있으신가요?

Music Road는 주변 사람들이 어떤 노래를 듣고 있는지, 더 나아가 내가 어떤 노래를 듣고 있는지 손쉽게 공유할 수 있으면 어떨까 라는 생각에서 출발했습니다.

내가 Pick한 음악을 길거리에 남기고, 지도의 Pick을 통해 다른 사람들은 이 장소에서 어떤 음악을 들었는지 둘러보세요.

### 🙆‍♂️ 서비스 대상
- 음악 취향을 공유하고 싶은 사람
- 다른 사람들은 지금 이 장소에서 어떤 노래를 들었는지 궁금한 사람
- 새로운 노래를 찾고 싶은 사람

<br>

## 🔮 핵심 기능
### 픽 탐색
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/a78aa629-cf19-4d71-89ac-c7dc6b019bd6" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/0b3cbc6d-42ad-4f72-9de2-c08b91bc98dc" width="200"></td>
    <td>
      <ul>
        <li>지도에서 등록된 픽을 마커로 확인할 수 있습니다.</li>
        <li>가까이 존재해서 겹쳐지는 픽은 클러스터링 되어 개수만 표시됩니다.</li>
        <li>마커를 누르면 인포윈도우에서 픽 정보를, <br>클러스터 마커를 누르면 여러 개의 픽들을 리스트로 보여줍니다.</li>
        <li>내 주변 반경 100M 내에 픽이 존재하면 배너에 개수가 표시되며, <br>클릭 시 노래가 랜덤으로 재생됩니다.</li>
        <li>다크모드를 지원합니다.</li>
      </ul>
    </td>
  </tr>
</table>

### 픽 등록
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/5605eb90-5c38-4b58-9c1e-f747d44dc7c6" width="200"></td>   
    <td>
      <ul>
        <li>등록할 노래를 검색할 수 있습니다.</li>
        <li>검색어를 입력하면 검색 결과가 자동으로 보여집니다.</li>
        <li>검색 결과를 페이지 하단까지 스크롤 시<br>다음 결과를 로드하여 모든 결과를 볼 수 있습니다.</li>
        <li>픽을 한마디와 함께 등록할 수 있습니다.</li>
      </ul>
    </td>
  </tr>
</table>

### 음악 감상
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/2fd2f06a-37f1-4447-bd73-dc3ccf294ce1" width="200"></td>   
    <td>
      <ul>
        <li>픽 정보 화면에서 마음에 드는 픽을 담을 수 있습니다.</li>
        <li>플레이어로 음악을 제어할 수 있습니다.</li>
        <li>음악 재생 시 소리에 따라 애니메이션이 나타납니다.</li>
        <li>Notification에서 백그라운드 재생을 제어할 수 있습니다.</li>
      </ul>
    </td>
  </tr>
</table>

### 뮤직비디오 감상
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/b32d2613-c1e4-4cc5-b3a6-e3fe1fc82db4" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/172d0b36-50db-4dc3-8950-94c16ddfdf07" width="250" height="448"></td>
    <td>
      <ul>
        <li>뮤직비디오가 존재하는 노래는 썸네일 미리보기 애니메이션이 나타납니다.<br>옆으로 스와이프하여 뮤직비디오를 볼 수 있습니다.</li>
        <li>뮤직비디오 화면을 탭해서 픽 정보를 확인할 수 있으며 <br>일시 정지, 재개, 다시보기를 컨트롤할 수 있습니다.</li>
        <li>기기 회전 시 영상의 가로/세로 모드가 전환되며 이어서 재생할 수 있습니다.</li>
      </ul>
    </td>
  </tr>
</table>

### 픽 모음
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/a671d424-6cd3-4af0-9a44-17693eded369" width="200"></td>
    <td>
      <ul>
        <li>나와 다른 사람들이 담은 Pick과 등록한 Pick을 한눈에 모아볼 수 있습니다.</li>
      </ul>
    </td>
  </tr>
</table>

<br>
<br>

## 📚 기술 스택
| 분류 |<div align="center">기술 스택</div>| <div align="center">관련 문서</div> |
|:---:|:---|:---|
| Architecture | <img src="https://img.shields.io/badge/Clean Architecture-FFFF7F?style=for-the-badge"> | |
| UI | <img src="https://img.shields.io/badge/Jetpack Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"> |  |
| DI | <img src="https://img.shields.io/badge/Hilt-34A853?style=for-the-badge"> |  |
| Map | <img src="https://img.shields.io/badge/Naver Map SDK-03C75A?style=for-the-badge"> | [지도 API 비교](https://vaulted-system-3ae.notion.site/Android-API-153f85098cd58065b54fe8a98f4569ec) |
| Network | <img src="https://img.shields.io/badge/OkHttp-000000?style=for-the-badge"> <img src="https://img.shields.io/badge/Retrofit-000000?style=for-the-badge"> <img src="https://img.shields.io/badge/Kotlinx.Serialization-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"> <img src="https://img.shields.io/badge/Paging3-34A853?style=for-the-badge"> |  |
| Image | <img src="https://img.shields.io/badge/Coil-3B6BB4?style=for-the-badge"> | [Coil vs Glide](https://www.notion.so/Coil-vs-Glide-136ea5e5fc1780b8a2efec5d56448f30?pvs=21)  |
| Media | <img src="https://img.shields.io/badge/ExoPlayer-34A853?style=for-the-badge"> <img src="https://img.shields.io/badge/Apple Music API-FA243C?style=for-the-badge&logo=applemusic&logoColor=white"> <img src="https://img.shields.io/badge/Visualizer-34A853?style=for-the-badge"> | [[음원 재생] 기술 검토 및 기획 ](https://www.notion.so/139ea5e5fc17805d86ccd288f434be9a?pvs=21) <br> [[영상 처리] 기술 검토 및 기획](https://www.notion.so/138ea5e5fc1780b2817ad722558936b4?pvs=21)  |
| Backend | <img src="https://img.shields.io/badge/Cloud Functions-DD2C00?style=for-the-badge&logo=firebase&logoColor=white"> |  |
| Data Storage | <img src="https://img.shields.io/badge/Cloud Firestore-DD2C00?style=for-the-badge&logo=firebase&logoColor=white"> <img src="https://img.shields.io/badge/DataStore-34A853?style=for-the-badge"> | [Firebase Firestore vs RealtimeDB](https://www.notion.so/Firebase-Firestore-vs-RealtimeDB-134ea5e5fc1780cb858dd2def297f16f?pvs=21)  |


<br>
<br>


## 🎯 기술적 도전
### 지도
- 디자인 요구사항에 맞게 마커 아이콘으로 사용할 커스텀 뷰를 만들었습니다.
- 지도를 축소했을 때 겹쳐지는 마커에 클러스터링 기능을 적용했습니다.
- 클러스터 마커를 클릭했을 때 포함된 항목을 보여주기 위해 클러스터 마커의 태그를 단말 마커의 태그를 모두 병합하여 지정하는 방식을 선택했습니다.

### 음원 재생
- ExoPlayer를 사용하여 컴포즈 UI에 대응되는 음원 플레이어를 구현했고, 메인 지도 화면에서 주변 반경 내의 음원을 셔플 재생할 수 있습니다.
- Configuration change가 일어나도 플레이어의 상태가 유지됩니다.
- 음원의 주파수 대역별 음량 데이터를 전처리 후 시각화하여 플레이어 UI에 반영했습니다.
- MediaSessionService를 이용해 포그라운드에서 동작하는 플레이어를 구현했습니다.

### 영상 재생
- 디자인 요구사항에 따라 영상 재생 화면을 커스텀 했습니다.
- 제스처와 애니메이션을 사용하여 화면 이동 및 오버레이 표시가 자연스럽게 연결되도록 했습니다.
- ExoPlayer를 사용하여 영상 재생 및 일시중지, 재시작 기능을 구현했으며 화면을 회전하여도 상태가 유지됩니다.


<br>
<br>

## 👨‍👩‍👧‍👦 팀원 소개
|K002 강민주|K009 김승규|K050 주윤겸|
|:---:|:---:|:---:|
|<img src="https://avatars.githubusercontent.com/u/88606886?v=4" width="150">|<img src="https://avatars.githubusercontent.com/u/31722615?v=4" width="150">|<img src="https://avatars.githubusercontent.com/u/30407907?v=4" width="150">|
|[@meanjoo](https://github.com/meanjoo)|[@miler198](https://github.com/miller198)|[@yuni-ju](https://github.com/yuni-ju)|

<br>

## 더 많은 정보는 [WIKI](https://github.com/boostcampwm-2024/and06-musicroad/wiki)를 방문해주세요!
