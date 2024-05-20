package hexlet.code.controller;

import hexlet.code.dto.status.StatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import hexlet.code.dto.status.StatusDTO;
import hexlet.code.dto.status.StatusCreateDTO;
import hexlet.code.mapper.StatusMapper;
import hexlet.code.repository.StatusRepository;

@RestController
@RequestMapping("/api")
public class StatusesController {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private StatusMapper statusMapper;

    @GetMapping("/task_statuses")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StatusDTO>> index() {
        var statuses = statusRepository.findAll();
        var result = statuses.stream()
                .map(statusMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(statuses.size()))
                .body(result);
    }

    @PostMapping("/task_statuses")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO create(@Valid @RequestBody StatusCreateDTO statusCreateDTO) {
        var status = statusMapper.map(statusCreateDTO);
        status.setCreatedAt(LocalDate.now());
        statusRepository.save(status);

        return statusMapper.map(status);
    }

    @GetMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO show(@PathVariable Long id) {
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id: " + id + " not found"));
        return statusMapper.map(status);
    }

    @PutMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO update(@Valid @RequestBody StatusUpdateDTO statusData, @PathVariable Long id) {
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id: " + id + " not found"));
        statusMapper.update(statusData, status);
        statusRepository.save(status);
        return statusMapper.map(status);
    }


    @DeleteMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id: " + id + " not found"));
        statusRepository.deleteById(id);
    }
}
