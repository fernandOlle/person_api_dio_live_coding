package one.digitalinnovation.personapi.services;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.mapper.PhoneMapper;
import one.digitalinnovation.personapi.dto.request.PhoneDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entities.Phone;
import one.digitalinnovation.personapi.exception.PhoneNotFoundException;
import one.digitalinnovation.personapi.repositories.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneService {

    private PhoneRepository phoneRepository;

    private final PhoneMapper phoneMapper = PhoneMapper.INSTANCE;

    public MessageResponseDTO create(PhoneDTO phoneDTO) {
        Phone phoneToSave = phoneMapper.toModel(phoneDTO);

        Phone savedPhone = phoneRepository.save(phoneToSave);
        return createMessageResponse(savedPhone.getId(), "Phone created with ID ");
    }

    public List<PhoneDTO> listAll() {
        List<Phone> allPhones = phoneRepository.findAll();
        return allPhones.stream()
                .map(phoneMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PhoneDTO findById(Long id) throws PhoneNotFoundException {
        Phone phone = verifyIfExists(id);
        return phoneMapper.toDTO(phone);
    }

    public void delete(Long id) throws PhoneNotFoundException {
        verifyIfExists(id);
        phoneRepository.deleteById(id);
    }

    public MessageResponseDTO update(Long id, PhoneDTO phoneDTO) throws PhoneNotFoundException {
        verifyIfExists(id);

        Phone phoneToUpdate = phoneMapper.toModel(phoneDTO);

        Phone upatedPhone = phoneRepository.save(phoneToUpdate);
        return createMessageResponse(upatedPhone.getId(), "Phone updated with ID ");
    }

    private Phone verifyIfExists(Long id) throws PhoneNotFoundException {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new PhoneNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
