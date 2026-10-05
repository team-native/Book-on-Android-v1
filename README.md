# Book-on-Android-v1

BookOn의 Android 앱입니다. 도서 검색과 도서관 정보, 대출 기록, 독서마라톤 연동을 위한 화면과 서버 연동 코드를 포함합니다.

## 주요 기능

아래는 저장소에 존재하는 화면과 구현입니다. 실제 서버·기기에서 정상 동작하는지는 기능별 검증이 필요합니다.

| 영역 | 화면·기능 |
|---|---|
| 계정 | 로그인, 회원가입, 이메일 인증, 비밀번호 설정·재설정 |
| 도서 | 홈, 검색, 도서 상세, 신간 도서 |
| 독서 활동 | 독서마라톤 연동, 독서 랭킹 |
| 도서관 | 도서관 화면 |
| 마이페이지 | 프로필, 대출 기록, 즐겨찾기, 로그아웃, 알림 설정 |
| 알림 | Firebase Messaging 수신 및 토큰 등록 처리 |

PRD는 저장소에서 확인되지 않았습니다. 제품의 확정 요구사항과 구현 완료 범위는 확인 필요입니다.

## 기술 스택

| 영역 | 기술 |
|---|---|
| 언어·빌드 | Kotlin 2.3.21, Gradle 9.4.1, Android Gradle Plugin 9.2.1, Kotlin DSL, Version Catalog |
| UI·화면 전환 | Jetpack Compose, Material 3, Navigation 3 |
| 상태·비동기 | ViewModel, StateFlow, Kotlin Coroutines |
| 의존성 주입 | Hilt, KSP |
| 서버 통신 | Retrofit 2.11.0, OkHttp 4.12.0, kotlinx.serialization |
| 저장·이미지 | Preferences DataStore, Coil |
| 알림·백그라운드 작업 | Firebase Messaging, WorkManager |
| 테스트 | JUnit 4, kotlinx-coroutines-test, Compose UI Test, AndroidX Test |

버전의 기준은 [Version Catalog](gradle/libs.versions.toml)와 [앱 빌드 설정](app/build.gradle.kts)입니다.

## 시작하기

### 요구사항

- JDK 21: Gradle daemon 설정과 CI에서 사용하는 버전입니다. 앱의 Java 소스·타깃 호환성 설정은 Java 11입니다.
- Android SDK: compileSdk 37.0, CI 설치 기준 Build Tools 37.0.0.
- 실행 기기: Android 14(API 34) 이상. `minSdk`는 34입니다.
- Android Studio: 현재 AGP·Gradle·SDK와 호환되는 버전이 필요하며, 최소 버전은 확인 필요입니다.
- `targetSdk`는 앱 빌드 파일에 명시되어 있지 않습니다. 실제 적용 값은 확인 필요입니다.

### 환경 준비 및 실행

1. Android Studio에서 저장소 루트를 열고 위 SDK와 JDK를 준비합니다.
2. Android Studio에서 로컬 Android SDK 경로를 설정합니다.
3. 프로젝트 담당자에게 앱 패키지 `com.teamnative.bookon`에 맞는 Firebase 설정 파일과 배치 방법을 확인합니다.
4. Gradle Sync 후 기기 또는 에뮬레이터를 선택해 `app` 실행 구성을 실행합니다.

비밀번호, 토큰, 테스트 계정 정보를 저장소나 테스트 결과에 기록하지 않습니다.

서버 주소와 타임아웃은 `app/build.gradle.kts`의 `BuildConfig.BASE_URL`, `NETWORK_TIMEOUT_SECONDS`에서 설정합니다. debug 빌드는 네트워크 로그를 활성화하고 release 빌드는 비활성화합니다. 서버의 운영·테스트 환경 구분과 가용성은 확인 필요입니다.

## 프로젝트 구조

Gradle 모듈은 `:app` 하나이며 공통 코드와 기능별 코드는 앱 모듈 내부 패키지로 구분합니다.

```text
app/src/main/java/com/teamnative/bookon/
├── app/                 # 앱 최상위 Compose 구성
├── core/
│   ├── designsystem/    # 테마와 디자인 토큰
│   ├── network/         # 서버 통신, 결과 처리, 인증 세션
│   ├── notification/    # FCM 및 백그라운드 등록 처리
│   └── ui/              # 공통 UI 컴포넌트와 모델
├── feature/             # auth, book, home, library, my 등 기능 패키지
└── navigation/          # 목적지, back stack, 하단 탭
```

기능별로 `presentation`의 Route·Screen·ViewModel·UiState, `domain`의 UseCase·Repository, `data`의 Repository 구현·RemoteDataSource·ApiService를 배치합니다. 기능에 따라 존재하는 계층은 다릅니다. 공통 네트워크 결과와 실행 처리는 `core/network`에서 관리합니다.

단위 테스트는 `app/src/test`, 기기에서 실행하는 계측 테스트는 `app/src/androidTest`에 있습니다.

## 테스트와 품질 검사

다음 명령은 [CI Build & Test](.github/workflows/ci-build-test.yml)에 정의되어 있습니다. 명령을 문서화한 것이며 이 README 작성 과정에서 실행 성공을 확인한 것은 아닙니다.

| 목적 | 명령 |
|---|---|
| debug Kotlin 컴파일 | `./gradlew :app:compileDebugKotlin --stacktrace` |
| debug 단위 테스트 | `./gradlew :app:testDebugUnitTest --stacktrace` |
| debug Android Lint | `./gradlew :app:lintDebug --stacktrace` |

CI는 `dev`, `main` 대상 PR에서 실행되며 테스트·Lint 보고서를 artifact로 업로드합니다. Firebase 설정이 없는 CI에서는 더미 설정을 사용하므로 CI 통과만으로 실제 FCM 동작을 확인할 수 없습니다.

기능 결함 조사 시에는 다음 순서를 따릅니다.

1. 전체 화면과 사용자 흐름의 테스트 목록을 만들고 코드 수정 전에 기능 테스트를 먼저 수행합니다.
2. 각 항목을 성공·실패·검증 불가로 구분하고 기기·앱 버전·서버 환경, 재현 단계, 기대 결과와 실제 결과를 기록합니다.
3. 실패를 앱·서버·환경 문제로 조사하고 확인된 결함을 수정합니다. 검증 불가 항목은 성공으로 처리하지 않습니다.
4. 수정한 흐름과 관련 기존 기능을 다시 검증하고 컴파일·단위 테스트·Lint 결과를 함께 남깁니다.

이메일 인증, 외부 독서마라톤 계정 연동, 실제 알림 수신은 해당 서비스와 기기 환경을 준비해 별도로 확인해야 합니다.

## 개발 워크플로와 참고 문서

GitHub 작업에서는 저장소의 작업 규칙에 따라 기존 Issue·PR을 먼저 확인하고 적절한 Issue에 연결합니다. Issue 번호가 포함된 브랜치와 커밋을 사용하며 PR에는 변경 사항과 실제 테스트 결과를 기록합니다.

- [BookOn Figma](https://www.figma.com/design/nJLeurKROi9jA3Pul9cdAK/Bookon?node-id=0-1): 화면 디자인 참고 자료.
- [백엔드 API 명세](https://github.com/team-native/Book-on-Backend-v1/blob/dev/fullApi.md): 백엔드 `dev` 브랜치의 API 참고 자료. 실제 서버 배포 버전과의 일치 여부는 확인 필요입니다.

PRD가 별도로 관리되고 있다면 위치와 적용 버전을 확인해 이 문서에 연결해야 합니다.
