package com.ll.commars.global.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter             // 모든 필드의 getter 메서드 생성
@SuperBuilder       // 상속된 클래스에서 빌더 패턴 사용 가능
@MappedSuperclass   // JPS Entity 클래스들이 이 클래스를 상속할 경우 필드들도 컬럼으로 인식
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자 생성, protected 접근 제어
@AllArgsConstructor(access = AccessLevel.PROTECTED) // 모든 필드 값을 파라미터로 받는 생성자 생성
@EntityListeners(AuditingEntityListener.class)      // JPA Auditing 기능 사용
@ToString           // toString 메서드 자동 생성
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
