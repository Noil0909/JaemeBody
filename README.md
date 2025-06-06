# JaemeBody

![JaemeBody그래픽이미지](https://github.com/user-attachments/assets/54f5d7fd-0a0b-46c5-9a22-bfc22a135d63)
운동 기록을 관리하는 피트니스 앱입니다. 사용자가 날짜별로 운동을 기록하여 시각적으로 편리하게 볼 수 있도록 도와줍니다. Kotlin과 Jetpack Compose를 기반으로 UI를 설계하고, Firebase를 이용해 사용자 정보를 안전하게 관리했습니다.
<hr/>

## 앱 주요 화면
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/53062775-473f-4861-b8d3-1af5d2ba5d38" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/8dc58351-59aa-44c3-904a-98b3730157c1" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/446e3285-7491-471c-88fb-1aece830a476" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/d62c3a63-7b88-45fe-b784-9c2f929adf1a" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/1726f8d2-0669-4754-bfec-338fa6fe5b21" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/9bd9f961-5e37-48d1-83b1-34d41fc96e5e" width="200"></td>
  </tr>
</table>
<div align="center">
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/b1decbfc-6676-4866-be5f-881f456fab6b" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/f2ac38e5-9a34-4dec-84e2-7f0adcaef874" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/d71793b2-6d7f-4389-a9bd-c6840ea7eedc" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/e6cab1fd-7441-4238-928b-0b9f25174b15" width="200"></td>
  </tr>
</table>
</div>

## 주요 기능
- Firebase Authentication 기반 로그인 / 회원가입
- 오늘의 운동 기록 확인 및 삭제
- 날짜별 운동 상태 색상 시각화
- 목표 칼로리에 따른 진행률 표시
- 프로필 정보(이름, 나이, 키, 몸무게) 확인 및 수정
- 프로필 이미지 설정 및 저장

## 기술 스택
- **언어**: Kotlin
- **UI**: Jetpack Compose
- **인증 및 DB**: Firebase Authentication / Firestore / Storage
- **이미지 로딩**: Coil (rememberAsyncImagePainter)
- **Splash**: SplashScreen API

## 설계 및 구현 요약
**MVVM 아키텍처 설계**

- UI와 로직을 분리하고 ViewModel에서 상태를 관리하는 구조로 설계했습니다.
- 운동 기록, 프로필 정보 등은 StateFlow를 사용해 실시간으로 Compose UI에 반영되도록 구현했습니다.

**운동 기록 관리**

- Firebase Firestore를 통해 운동 기록을 저장하고 조회합니다.
- 오늘 날짜의 운동 데이터는 홈 탭에 자동 표시되며, 기록이 없을 경우 안내 문구를 제공합니다.
- 기록 삭제 시 ViewModel을 통해 UI에 즉시 반영되도록 개선했습니다.

**캘린더 화면 날짜별 색상 시각화**

- 운동 상태에 따라 각 날짜에 색상을 적용해 한눈에 확인할 수 있도록 했습니다.
    - 회색: 기록 없음
    - 연두색: 일부 기록
    - 초록색: 목표 달성

**프로필 이미지 저장 및 캐싱 처리**

- 사용자가 선택한 이미지를 Firebase Storage에 업로드하고, Firestore에 URL을 저장하여 프로필 이미지 사용이 가능합니다.

**Splash 화면 및 디자인 요소**

- 앱 실행 시 Splash 화면을 통해 첫 로딩을 자연스럽게 처리했습니다.
- 전체 화면 구성은 다크모드 스타일에 맞춰 검정 배경과 어두운 색감을 적절히 사용해 안정적인 UI를 구현했습니다.

## 앞으로의 계획 및 개선 방향
운동 종목별 칼로리 자동 계산 기능
- 운동 기록 시, 사용자가 선택한 종목과 시간 정보를 기반으로 자동으로 칼로리를 계산하여 입력값을 최소화하고 정확도를 높일 수 있도록 하고자 합니다. ex) 달리기 30분 → 300Kcal 자동 계산

**일간/주간/월간 요약 통계 화면**
- 일간/주간/월간을 기준으로 누적 운동 시간, 총 소모 칼로리, 평균 운동 빈도 등을 시각적으로 확인할 수 있는 분석 및 통계 화면을 계획중입니다.

**구글 피트니스 API 연동**
- 구글 피트니스 기능을 사용하는 사용자의 실제 운동 데이터를 가져와 자동으로 기록할 수 있도록 구글 피트니스 API 연동을 고려중입니다.

**카카오 로그인 API 도입**
- 간편한 로그인을 위해 카카오 로그인 SDK를 도입할 계획입니다.
