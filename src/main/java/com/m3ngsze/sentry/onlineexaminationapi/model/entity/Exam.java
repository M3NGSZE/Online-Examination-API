package com.m3ngsze.sentry.onlineexaminationapi.model.entity;

import com.m3ngsze.sentry.onlineexaminationapi.model.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "exams")
public class Exam extends BaseEntity{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "exam_id" )
    private UUID examId;

    private String title;

    private String subject;

    @Column( name = "is_expired" )
    private Boolean isExpired;

    private Integer duration;

    @Column( name = "total_marks" )
    private BigDecimal totalMarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_status")
    private ExamStatus examStatus;

    @Column( name = "started_at" )
    private LocalDateTime startedAt;

    @Column( name = "endedAt" )
    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, referencedColumnName = "room_id")
    private Room room;

}
