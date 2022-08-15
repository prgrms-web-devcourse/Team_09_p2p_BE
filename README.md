![header](https://capsule-render.vercel.app/api?type=waving&color=509FFB&height=250&section=header&text=여행할%20땐%20💙이곳저곳💙&fontSize=50&animation=fadeIn&fontAlignY=40)

프로젝트 💙 이곳저곳 💙 BE Team의 Repository 입니다 💨

[FE Repository](https://github.com/prgrms-web-devcourse/Team_09_p2p_FE)는 👈 여기로

## 프로젝트 개요

### ⌛️ 프로젝트 기간

`2022/07/18(月)` ~ `2022/08/15(月)`

### 🍳 팀원 소개

| [Kevin](https://github.com/qjatjr29)                                             | [Kate](https://github.com/moosongsong)                                                                                            | [Charlie](https://github.com/Hyunggeun447)                                                                                        | [Frank](https://github.com/dhkstnaos)                                                                                             | [Kid](https://github.com/unnokid)                                                                                                 | 
|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| <img src = "https://avatars.githubusercontent.com/u/74031333?v=4" width = "200"> | <img src = "https://user-images.githubusercontent.com/50647845/184600278-0411f51c-48ae-4c63-aa24-7c4cd674c19c.png" width = "200"> | <img src = "https://user-images.githubusercontent.com/50647845/184652712-64628b36-8b87-4e76-b11f-3aebe75cca73.png" width = "200"> | <img src = "https://user-images.githubusercontent.com/50647845/184600042-1cee5e00-c1ff-4ce3-acc5-9deffccc3ca4.png" width = "200"> | <img src = "https://user-images.githubusercontent.com/50647845/184599960-7b6d996e-093c-4030-9586-224e58965ed2.png" width = "200"> |
| Product Owner                                                                    | Scrum Master                                                                                                                      | Developer                                                                                                                         | Developer                                                                                                                         | Developer                                                                                                                         |

### 🛠 프로젝트 구조

```
    ┌─────────────────────┐            
    ├─────────────────────┤  Request!  ┌───────────────┐           ┌──────────────┐        
    │                     ├───────────>│    Route 53   │           │    GitHub    │
    │      Front-end      │<───────────┤     (DNS)     │           └──────┬───────┘ 
    │                     │  Response! └─────┬───┬─────┘                  │ Push!
    └─────────────────────┘                  │   │                        │
                                       ┌─────┴───┴─────┐           ┌──────┴───────┐
    ┌───── Data Store ────┐            │ Application   │           │    Jenkis    │
    │   ┌──── RDS ────┐   │            │ Load Balancer │           │   (CI / CD)  │
    │   │    MySQL    │   │            └─────┬───┬─────┘           └────┬────┬────┘    
    │   └─────────────┘   │                  │   │                      │    │ Upload!
    │   ┌──── EC2 ────┐   │  Response! ┌─────┴───┴─────┐       Deploy!  │    └──────────────┐
    │   │    Redis    │   ├───────────>│  Spring Boot  │      ┌─────────┴─────┐      ┌──────┴──────┐
    │   └─────────────┘   │            │  Application  │<─────┤  Code Deploy  │<─────┤  S3 Bucket  │
    │   ┌──── S3 ─────┐   │<───────────┤     (EC2)     │      └───────────────┘      └─────────────┘
    │   │   Images    │   │  Request!  └───────────────┘
    │   └─────────────┘   │ 
    └─────────────────────┘
```

### 🔧 사용 기술

![Java](https://img.shields.io/badge/-Java%2011-007396?style=plastic&logo=java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/-Spring%20Boot%202.7.0-6DB33F?style=plastic&logo=Spring%20Boot&logoColor=white)
![SpringDataJPA](https://img.shields.io/badge/-Spring%20Data%20JPA%202.7.1-6D933F?style=plastic&logo=Spring&logoColor=white)
![Querydsl](https://img.shields.io/badge/-Querydsl%205.0.0-7D933F?style=plastic&logo=Spring&logoColor=white)
![SpringSecurity](https://img.shields.io/badge/-Spring%20Security-6DB33F?style=plastic&logo=SpringSecurity&logoColor=white)

### 🧱 인프라

![Gradle](https://img.shields.io/badge/-Gradle%207.2-02303A?style=plastic&logo=Gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL%208.028-4479A1?style=plastic&logo=MySQL&logoColor=white)
![AmazonAWS](https://img.shields.io/badge/AWS%20S3-232F3E?style=plastic&logo=AmazonAWS&logoColor=white)
![AmazonAWS](https://img.shields.io/badge/AWS%20RDS-232F6E?style=plastic&logo=AmazonAWS&logoColor=white)
![AmazonAWS](https://img.shields.io/badge/AWS%20EC2-232F8E?style=plastic&logo=AmazonAWS&logoColor=white)

### 📠 협업툴

![GitHub](https://img.shields.io/badge/-GitHub-181717?style=plastic&logo=GitHub&logoColor=white)
![Jira](https://img.shields.io/badge/-Jira-0052CC?style=plastic&logo=JiraSoftware&logoColor=white)
![Notion](https://img.shields.io/badge/-Notion-000000?style=plastic&logo=Notion&logoColor=white)
![Slack](https://img.shields.io/badge/-Slack-4A154B?style=plastic&logo=Slack&logoColor=white)

## 프로젝트 진행

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

