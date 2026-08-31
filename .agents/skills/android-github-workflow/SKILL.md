---
name: android-github-workflow
description: Manage Android project GitHub work using the issue-first workflow. Use when creating or updating GitHub issues, issue-linked branches, commits, pull requests, labels, or release pull requests for this repository.
---

# Android GitHub Workflow

## Overview

Android 작업은 Issue를 먼저 만들고, Issue 번호를 브랜치·커밋·PR에 연결한다. 저장소의 `AGENTS.md` 규칙을 이 Skill보다 우선 적용한다.

## Workflow

1. 저장소, 작업 범위, 작업 유형이 feature·fix·refactor·chore·hotfix 중 무엇인지 확인한다.
2. 동일한 작업을 다루는 열린 Issue 또는 PR이 있는지 먼저 확인한다.
3. 새 작업이면 요구 템플릿으로 Issue를 만들고 적용 가능한 라벨 하나만 지정한다.
4. 저장소의 최신 기본 브랜치에서 다음 형식으로 작업 브랜치를 만든다.
   - 기능: `feature/#<issue-number>-<kebab-case-summary>`
   - 버그: `fix/#<issue-number>-<kebab-case-summary>`
   - 리팩터링: `refactor/#<issue-number>-<kebab-case-summary>`
5. Android 변경을 구현하고 관련 Build·Unit Test·Lint·UI 검증을 수행한다.
6. 커밋 메시지와 PR 본문에 Issue 번호를 포함한다.

## Issue Convention

```md
제목: [Feature] 구현할 작업

## 목적
-

## 작업 내용
-

## 완료 조건
- [ ]
- [ ]

## 참고 사항
- 브랜치: `feature/#<issue-number>-<summary>`
```

작업 유형에 따라 제목 접두사는 `[Feature]`, `[Fix]`, `[Refactor]`를 사용한다. 한글 `[기능]`, `[수정]` 접두사는 사용하지 않는다.

## Label Convention

- `✨ feature`: 새로운 기능 구현
- `🚨 fix`: 버그 수정
- `♻️ refactor`: 동작 변경 없는 구조 개선
- `🔧 chore`: 설정, 빌드, dependency 작업
- `🔥 hotfix`: 긴급 수정

Issue에는 위 라벨 중 작업 성격에 맞는 하나만 적용한다.

## Commit Convention

```text
<type>: [#<issue-number>] <작업 요약>
```

커밋은 하나의 논리적 변경만 포함한다. `codex/` 접두사는 브랜치·커밋·PR·Issue 식별자에 사용하지 않는다.

## Pull Request Convention

PR 제목은 `<type>: <전체 작업 요약>`으로 작성하고 작업 내용, 주요 변경 사항, 테스트 결과, 관련 Issue를 포함한다. 사용자가 명시하지 않은 경우 PR은 draft로 만든다.

```md
## 작업 내용
-

## 주요 변경 사항
-

## 테스트 결과
- [ ] Build
- [ ] Unit Test
- [ ] Lint
- [ ] UI / Preview 확인

## 관련 Issue
Closes #<issue-number>
```

## Release Convention

릴리스 작업은 `🔧 chore` Issue를 만들고 `chore/#<issue-number>-release-<version>` 브랜치에서 검증한다. PR 대상은 저장소의 릴리스 정책에 따른다.
