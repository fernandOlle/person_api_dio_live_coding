package one.digitalinnovation.personapi.controllers;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PhoneDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.exception.PhoneNotFoundException;
import one.digitalinnovation.personapi.services.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/phone")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneController {

    private PhoneService phoneService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createPhone(@RequestBody @Valid PhoneDTO phoneDTO) {
        return phoneService.create(phoneDTO);
    }

    @GetMapping
    public List<PhoneDTO> listAll() {
        return phoneService.listAll();
    }

    @GetMapping("/{id}")
    public PhoneDTO findById(@PathVariable Long id) throws PhoneNotFoundException {
        return phoneService.findById(id);
    }

    @PutMapping("/{id}")
    public MessageResponseDTO updateById(@PathVariable Long id, @RequestBody @Valid PhoneDTO phoneDTO) throws PhoneNotFoundException {
        return phoneService.update(id, phoneDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws PhoneNotFoundException {
        phoneService.delete(id);
    }
}
