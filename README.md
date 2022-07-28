# 이곳저곳 💨 

프로젝트 💙 이곳저곳 💙 BE의 Repository 입니다.

### 팀 : 오프와 에프 BE

| Product Owner                      | Scrum Master                          | Developer                           | Developer                         | Developer                              |
|------------------------------------|---------------------------------------|-------------------------------------|-----------------------------------|----------------------------------------|
| [고범석](https://github.com/qjatjr29) | [송무송](https://github.com/moosongsong) | [강완수](https://github.com/dhkstnaos) | [김기현](https://github.com/unnokid) | [박형근](https://github.com/Hyunggeun447) |

## 프로젝트 개요

### 프로젝트 목적

- 백엔드 시스템의 도메인 전반을 이해하고 구현해보자.
- 페어 프로그래밍을 함으로서 **활발한 지식 공유**와 **깊은 사고**를 하는 태도를 체화하자.

### 사용 기술

![Java](https://img.shields.io/badge/-Java%2011-007396?style=plastic&logo=java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/-Spring%20Boot%202.7.0-6DB33F?style=plastic&logo=Spring%20Boot&logoColor=white)
![SpringDataJPA](https://img.shields.io/badge/-Spring%20Data%20JPA%202.7.1-6D933F?style=plastic&logo=Spring&logoColor=white)
![Querydsl](https://img.shields.io/badge/-Querydsl%205.0.0-7D933F?style=plastic&logo=Spring&logoColor=white)
![SpringSecurity](https://img.shields.io/badge/-Spring%20Security-6DB33F?style=plastic&logo=SpringSecurity&logoColor=white)

### 인프라

![Gradle](https://img.shields.io/badge/-Gradle%207.2-02303A?style=plastic&logo=Gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL%208.028-4479A1?style=plastic&logo=MySQL&logoColor=white)
![AmazonAWS](https://img.shields.io/badge/AWS%20S3-232F3E?style=plastic&logo=AmazonAWS&logoColor=white)
![AmazonAWS](https://img.shields.io/badge/AWS%20RDS-232F6E?style=plastic&logo=AmazonAWS&logoColor=white)
![AmazonAWS](https://img.shields.io/badge/AWS%20EC2-232F8E?style=plastic&logo=AmazonAWS&logoColor=white)

### 협업툴

![GitHub](https://img.shields.io/badge/-GitHub-181717?style=plastic&logo=GitHub&logoColor=white)
![Jira](https://img.shields.io/badge/-Jira-0052CC?style=plastic&logo=JiraSoftware&logoColor=white)
![Notion](https://img.shields.io/badge/-Notion-000000?style=plastic&logo=Notion&logoColor=white)
![Slack](https://img.shields.io/badge/-Slack-4A154B?style=plastic&logo=Slack&logoColor=white)

## 프로젝트 진행

### 페어 프로그래밍

- (1주차) 5인 페어 프로그래밍
    - 1시간 단위로 `driver`와 `navigator` 변경하여 진행
- (2주차) 2인, 3인 페어 프로그래밍
    - Task 단위로 `driver`와 `navigator` 변경하여 진행
- (3주차) 1인 프로그래밍

### Branch Convention

현재 보호되고 있는 브랜치는 `develop`과 `main` 이며, `develop`은 개발용 `main`은 배포용입니다.

브랜치는 다음과 같이 명명합니다.

- 기능 개발 목적의 브랜치
    - feature/Jira-이슈번호
- 브랜치에서 발생한 버그 수정 목적의 브랜치
    - hotfix/Jira-이슈번호

### PR Convention

- `[Jira 이슈번호] 간략한 제목` 으로 PR 제목을 기재합니다.
- `merge`는 2명 이상의 `approve`가 필요합니다.

### Commit Convention

```
feat : 새로운 기능에 대한 커밋
fix : 버그 수정에 대한 커밋
chore : 빌드 업무 수정, 패키지 매니저 수정
docs : 문서 수정에 대한 커밋
style : 코드 스타일 혹은 포맷 등에 관한 커밋
refactor :  코드 리팩토링에 대한 커밋
test : 테스트 코드 수정에 대한 커밋
```

### Code Convention

- 코드 스타일
    - google code style
- 접근제한자에 따른 코드 작성 순서
    - 필드: public -> private
    - 메서드: public -> private
    - 생성자: private -> public
- 어노테이션에 따른 코드 작성 순서
    - DB 관련 어노테이션 (ex: Entity, Table)
    - 객체 관련 어노테이션 (ex: Getter, ToString)
    - 생성 관련 어노테이션 (ex: Builder, RequiredArgsConstructor)

