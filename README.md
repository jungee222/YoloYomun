# Yomun's Yolo Project
## Yomun's Yolo Project

# Git 명령어

## Add
* `git add "file name"`
  * `"file name"`의 파일을 추적 및 Staged로 전환하는 명령어
* `git add .`
  * 작업 중인 모든 파일을 추가 및 Staged로 전환하는 명령어

## Commit
* `git commit -m "Commit Message"`
  * `-m` 옵션은 커밋에 이름을 적어주는 옵션
* `git commit --amend`
* `git commit --amend -m "Commit message"`
  * `--amend` 옵션은 현재 커밋 내용을 제일 최근 커밋과 합치는 기능

## Log
* `git log`
  * 과거 버전을 확인하는 명령어
* `git log --oneline`
  * 주로 사용하며, 로그를 한 줄로 간략하게 볼 수 있음

## Branch
* `git branch`
  * 로컬에서 사용 중인 브랜치를 확인하는 명령어
* `git branch -D "브랜치명"`
  * 로컬에서 사용 중인 브랜치를 제거하는 명령어

## Checkout
* `git checkout "Branch Name"`
  * `"Branch Name"` 브랜치로 이동하는 명령어
* `git checkout -b "New Branch Name"`
  * 새로운 브랜치 `"New Branch Name"`을 개설함과 동시에 이동하는 명령어

## Push
* `git push origin`
  * 커밋한 내용을 `origin` 원격 레포지토리로 업로드하는 명령어

## Pull
* `git pull`
  * 현재 작업 중인 브랜치에서 원격 브랜치의 버전이 더 높을 때, 변경사항을 원격에서 로컬로 가져오는 명령어

## Merge
> **가정:** 개발자가 "Branch A"에서 작업 중이고, "Branch B"의 변경사항을 병합하려는 상황
* `git merge "Branch B"`
  * `"Branch B"`의 변경사항을 현재 브랜치에 병합하는 명령어

---

## Pull Request (PR)

### 작업 순서
1. 작업한 D 브랜치로 이동 및 원격 푸시
   * `git checkout D`
   * `git push origin D`
2. GitHub에서 Pull Request를 개설
3. GitHub 상에서 C 브랜치로 병합 진행 (원격 레포지토리의 `origin/C`에 병합됨)
4. 로컬의 C 브랜치로 이동 후 변경 사항 Pull
   * `git checkout C`
   * `git pull origin C`
