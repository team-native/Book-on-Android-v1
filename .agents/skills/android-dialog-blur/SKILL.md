---
name: android-dialog-blur
description: Jetpack Compose 다이얼로그가 열릴 때 배경 블러를 추가·수정하는 작업에 사용한다. Android Window 배경 블러, ModalBottomSheet처럼 별도 레이어인 배경, DisposableEffect 기반 설정·해제, 재사용 가능한 Modifier 블러 처리가 필요한 경우 적용한다.
---

# Android Dialog Blur

## 브랜치 선행

- 기능을 구현하거나 수정하기 전에 해당 기능 브랜치로 이동한다. 브랜치가 없으면 작업 범위에 맞는 기능 브랜치를 먼저 생성한 뒤 작업한다.

## 완료 및 Issue 처리

- 커밋한 변경이 연결된 Issue의 작업 내용과 완료 조건을 모두 충족한 경우에만 `dev` 대상 Pull Request를 생성한다.
- Pull Request 본문의 관련 Issue 영역에 `Closes #<issue-number>`를 작성한다. Issue는 Pull Request 병합 전 수동으로 닫지 않고, `dev` 병합 후 자동 종료 여부를 확인한다.

## Rules

- Create one reusable Window blur Composable in `core/component/overlay` instead of duplicating `DialogWindowProvider` casts in each dialog.
- Use `DisposableEffect(dialogWindow, blurRadiusPx)` to add the blur flag and radius, then clear the flag and reset the radius in `onDispose`.
- Use a reusable `Modifier` extension for visual fallback blur. Apply one shared Modifier from the Route to every independently rendered backdrop, such as a screen and a `ModalBottomSheet`.
- Keep blur radius in a role-based dimension token; convert `Dp` to pixels only at the Window API boundary.
- Do not use `SideEffect` for Window blur lifecycle management.

## Pattern

```kotlin
@Composable
fun ApplyDialogWindowBackgroundBlur(blurRadius: Dp) {
    val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
    val blurRadiusPx = with(LocalDensity.current) { blurRadius.toPx().roundToInt() }

    DisposableEffect(dialogWindow, blurRadiusPx) {
        dialogWindow?.apply { /* register blur */ }

        onDispose {
            dialogWindow?.apply { /* clear blur */ }
        }
    }
}
```
