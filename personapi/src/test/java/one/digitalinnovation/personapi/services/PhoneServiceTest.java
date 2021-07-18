package one.digitalinnovation.personapi.services;

import one.digitalinnovation.personapi.dto.mapper.PhoneMapper;
import one.digitalinnovation.personapi.dto.request.PhoneDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Phone;
import one.digitalinnovation.personapi.exception.PhoneNotFoundException;
import one.digitalinnovation.personapi.repositories.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static one.digitalinnovation.personapi.utils.PhoneUtils.createFakeDTO;
import static one.digitalinnovation.personapi.utils.PhoneUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PhoneServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private PhoneMapper phoneMapper;

    @InjectMocks
    private PhoneService phoneService;

    @Test
    void testGivenphoneDTOThenReturnSuccessSavedMessage() {
        PhoneDTO phoneDTO = createFakeDTO();
        Phone expectedSavedPhone = createFakeEntity();

        when(phoneMapper.toModel(phoneDTO)).thenReturn(expectedSavedPhone);
        when(phoneRepository.save(any(Phone.class))).thenReturn(expectedSavedPhone);

        MessageResponseDTO successMessage = phoneService.create(phoneDTO);

        assertEquals("Phone created with ID 1", successMessage.getMessage());
    }

    @Test
    void testGivenValidphoneIdThenReturnThisphone() throws PhoneNotFoundException {
        PhoneDTO expectedPhoneDTO = createFakeDTO();
        Phone expectedSavedPhone = createFakeEntity();
        expectedPhoneDTO.setId(expectedSavedPhone.getId());

        when(phoneRepository.findById(expectedSavedPhone.getId())).thenReturn(Optional.of(expectedSavedPhone));
        when(phoneMapper.toDTO(expectedSavedPhone)).thenReturn(expectedPhoneDTO);

        PhoneDTO phoneDTO = phoneService.findById(expectedSavedPhone.getId());

        assertEquals(expectedSavedPhone.getId(), phoneDTO.getId());
        assertEquals(expectedSavedPhone.getNumber(), phoneDTO.getNumber());
    }

    @Test
    void testGiveninvalidPhoneIdThenThrowException() {
        var invalidPhoneId = 1L;
        when(phoneRepository.findById(invalidPhoneId))
                .thenReturn(Optional.ofNullable(any(Phone.class)));

        assertThrows(PhoneNotFoundException.class, () -> phoneService.findById(invalidPhoneId));
    }

    @Test
    void testGivenNoDataThenReturnAllPeopleRegistered() {
        List<Phone> expectedRegisteredPhones = Collections.singletonList(createFakeEntity());
        PhoneDTO phoneDTO = createFakeDTO();

        when(phoneRepository.findAll()).thenReturn(expectedRegisteredPhones);
        when(phoneMapper.toDTO(any(Phone.class))).thenReturn(phoneDTO);

        List<PhoneDTO> expectedPeopleDTOList = phoneService.listAll();

        assertFalse(expectedPeopleDTOList.isEmpty());
    }

    @Test
    void testGivenValidphoneIdAndUpdateInfoThenReturnSuccesOnUpdate() throws PhoneNotFoundException {
        var updatedphoneId = 2L;

        PhoneDTO updatePhoneDTORequest = createFakeDTO();
        updatePhoneDTORequest.setId(updatedphoneId);
        updatePhoneDTORequest.setNumber("5399999-9999");

        Phone expectedPhoneToUpdate = createFakeEntity();
        expectedPhoneToUpdate.setId(updatedphoneId);

        Phone expectedPhoneUpdated = createFakeEntity();
        expectedPhoneUpdated.setId(updatedphoneId);
        expectedPhoneToUpdate.setNumber(updatePhoneDTORequest.getNumber());

        when(phoneRepository.findById(updatedphoneId)).thenReturn(Optional.of(expectedPhoneUpdated));
        when(phoneMapper.toModel(updatePhoneDTORequest)).thenReturn(expectedPhoneUpdated);
        when(phoneRepository.save(any(Phone.class))).thenReturn(expectedPhoneUpdated);

        MessageResponseDTO successMessage = phoneService.update(updatedphoneId, updatePhoneDTORequest);

        assertEquals("Phone updated with ID 2", successMessage.getMessage());
    }

    @Test
    void testGiveninvalidPhoneIdAndUpdateInfoThenThrowExceptionOnUpdate() throws PhoneNotFoundException {
        var invalidPhoneId = 1L;

        PhoneDTO updatePhoneDTORequest = createFakeDTO();
        updatePhoneDTORequest.setId(invalidPhoneId);
        updatePhoneDTORequest.setNumber("5399999-9999");

        when(phoneRepository.findById(invalidPhoneId))
                .thenReturn(Optional.ofNullable(any(Phone.class)));

        assertThrows(PhoneNotFoundException.class, () -> phoneService.update(invalidPhoneId, updatePhoneDTORequest));
    }

    @Test
    void testGivenValidphoneIdThenReturnSuccesOnDelete() throws PhoneNotFoundException {
        var deletedPhoneId = 1L;
        Phone expectedPhoneToDelete = createFakeEntity();

        when(phoneRepository.findById(deletedPhoneId)).thenReturn(Optional.of(expectedPhoneToDelete));
        phoneService.delete(deletedPhoneId);

        verify(phoneRepository, times(1)).deleteById(deletedPhoneId);
    }

    @Test
    void testGiveninvalidPhoneIdThenReturnSuccesOnDelete() throws PhoneNotFoundException {
        var invalidPhoneId = 1L;

        when(phoneRepository.findById(invalidPhoneId))
                .thenReturn(Optional.ofNullable(any(Phone.class)));

        assertThrows(PhoneNotFoundException.class, () -> phoneService.delete(invalidPhoneId));
    }
}