# 📚 Bookmarker - 도서관 관리 시스템 (JDBC 기반 콘솔 프로젝트)

## 🔍 개요

**Bookmarker**는 Java와 Oracle DB를 기반으로 한 **콘솔형 도서관 관리 시스템**입니다.  
관리자(Admin), 사서(Librarian), 학생(Student) 사용자 역할(Role)을 기반으로 도서 대출, 장바구니, 회원 관리 기능 등을 제공합니다.

---

## 🛠 주요 기능

### 📖 공통 기능
- 사용자 로그인 및 세션 관리 (`Session.java`)
- 도서 검색 및 상세 조회 (`BookSelectClass.java`)
- JDBC 기반 DB 연결 관리 (`DataSource.java`)

### 👨‍🎓 학생 기능
- 도서 장바구니에 담기 및 삭제
- 장바구니 기반 대출 신청 (`CartDAO`, `LoanDAO`)
- 내 대출 도서 확인 및 반납

### 📚 사서 기능
- 도서 CRUD (등록, 수정, 삭제)
- 도서 대출 및 반납 처리
- 장르 및 도서 카테고리 관리

### 🛡 관리자 기능
- 회원 전체 조회, 권한 수정
- 회원 등록 및 삭제
- 통계 기능 (대출 건수, 인기 도서 등)

---

## 🧩 프로젝트 구조

```bash
librarySystem_JDBCProject/
├── src/com/bookmark/
│   ├── common/        # DB 연결, 세션
│   ├── dao/           # DB 접근 객체 (DAO)
│   ├── selectview/    # 역할별 콘솔 UI 및 제어 클래스
│   └── vo/            # VO (Value Object) 클래스
