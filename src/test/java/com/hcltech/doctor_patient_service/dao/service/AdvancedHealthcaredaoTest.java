package com.hcltech.doctor_patient_service.dao.service;

import com.hcltech.doctor_patient_service.entity.AdvancedHealthcare;
import com.hcltech.doctor_patient_service.repository.AdvancedHealthcareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class AdvancedHealthcaredaoTest
{
    @Mock
    private AdvancedHealthcareRepository advancedHealthcareRepository;

    @InjectMocks
    private AdvancedHealthcareDaoService advancedHealthcareDaoService;

    private static final Logger log = LoggerFactory.getLogger(AdvancedHealthcaredaoTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
void testGetById_Found() {
    // Arrange
    Long id = 1L;
    AdvancedHealthcare advancedHealthcare = new AdvancedHealthcare();
    when(advancedHealthcareRepository.findById(id)).thenReturn(Optional.of(advancedHealthcare));

    // Act
    Optional<AdvancedHealthcare> result = advancedHealthcareDaoService.getById(id);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(advancedHealthcare, result.get());
    verify(advancedHealthcareRepository, times(1)).findById(id);
    log.info("fetching AdvancedHealthcare details With ID: {}", id);
}

@Test
void testGetById_NotFound() {
    // Arrange
    Long id = 1L;
    when(advancedHealthcareRepository.findById(id)).thenReturn(Optional.empty());

    // Act
    Optional<AdvancedHealthcare> result = advancedHealthcareDaoService.getById(id);

    // Assert
    assertFalse(result.isPresent());
    verify(advancedHealthcareRepository, times(1)).findById(id);
    log.info("fetching AdvancedHealthcare details With ID: {}", id);
}
}
