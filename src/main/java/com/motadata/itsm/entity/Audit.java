package com.motadata.itsm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String operationName;
    private LocalDate localDate;
    private String message;
}
