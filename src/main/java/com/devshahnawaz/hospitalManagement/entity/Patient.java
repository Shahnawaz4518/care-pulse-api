package com.devshahnawaz.hospitalManagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.devshahnawaz.hospitalManagement.entity.type.BloodGroupType;

import jakarta.persistence.CascadeType;
// ─── JPA Core Imports ──────────────────────────────────────────────────────
import jakarta.persistence.Column;
import jakarta.persistence.Entity; // Marks this class as a JPA entity (mapped to a DB table)
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated; // Maps Java enum to DB column
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Marks the primary key field
import jakarta.persistence.Index; // Defines a DB index on table columns
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table; // Customizes the table name and constraints
import jakarta.persistence.UniqueConstraint;
// ───────────────────────────────────────────────────────────────────────────

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Patient – JPA Entity
 *
 * JPA CONCEPTS USED HERE:
 * 
 * @Entity – tells Hibernate to manage this class as a persistent entity
 * @Table – maps entity to the "patient" DB table; defines unique constraints &
 *        indexes
 * @Id – marks the primary key
 * @GeneratedValue – auto-increments the PK (IDENTITY strategy → relies on DB
 *                 auto-increment)
 * @Column – fine-tunes column properties (nullable, length, updatable, unique)
 * @Enumerated – stores the Java enum as its STRING name in the DB column
 * @CreationTimestamp – Hibernate automatically sets this field to current
 *                    timestamp on INSERT
 */
@Entity
@ToString
@Getter
@Setter
// JPA: @Table lets you control the table name, unique constraints, and indexes
// at the DB level
@Table(name = "patient", uniqueConstraints = {
                // JPA: UniqueConstraint – enforces DB-level uniqueness on email
                @UniqueConstraint(name = "unique_patient_email", columnNames = { "email" }),
                // JPA: Composite unique constraint – no two patients can share same name +
                // birthDate
                @UniqueConstraint(name = "unique_patient_name_birthdate", columnNames = { "name", "birthDate" })
}, indexes = {
                // JPA: @Index – creates a DB index on birthDate for faster range queries
                @Index(name = "idx_patient_birth_date", columnList = "birthDate")
})
public class Patient {

        // JPA: @Id marks the primary key; @GeneratedValue(IDENTITY) = DB auto-increment
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // JPA: @Column(nullable=false, length=40) → NOT NULL VARCHAR(40) in DB
        @Column(nullable = false, length = 40)
        private String name;

        // JPA: LocalDate is mapped to DATE type in DB by Hibernate automatically
        private LocalDate birthDate;

        // JPA: unique=true adds a UNIQUE constraint; nullable=false → NOT NULL
        @Column(unique = true, nullable = false)
        private String email;

        // @ToString.Exclude – Lombok: omits this field from generated toString() output
        @ToString.Exclude
        private String gender;

        // JPA / Hibernate: @CreationTimestamp – Hibernate sets this automatically on
        // INSERT
        // @Column(updatable=false) – prevents this field from being changed after
        // creation
        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime createdAt;

        // JPA: @Enumerated(EnumType.STRING) – stores "A_POSITIVE", "B_NEGATIVE" etc. in
        // DB
        // (vs. EnumType.ORDINAL which stores an integer index – avoid, fragile to enum
        // reordering)
        @Enumerated(EnumType.STRING)
        private BloodGroupType bloodGroup;

        @OneToOne(cascade = { CascadeType.ALL }, orphanRemoval = true)
        @JoinColumn(name = "patient_insurance_id")
        private Insurance insurance;

        @OneToMany(mappedBy = "patient", cascade = { CascadeType.REMOVE })
        @ToString.Exclude
        private List<Appointment> appointments = new ArrayList<>();

}
