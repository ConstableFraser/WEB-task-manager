package hexlet.code.service;

import hexlet.code.dto.status.StatusCreateDTO;
import hexlet.code.dto.status.StatusDTO;
import hexlet.code.dto.status.StatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.StatusMapper;
import hexlet.code.repository.StatusRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private StatusMapper statusMapper;

    public List<StatusDTO> getAll() {
        var statuses = statusRepository.findAll();

        return statuses.stream()
                .map(statusMapper::map)
                .toList();
    }

    public StatusDTO create(@Valid @RequestBody StatusCreateDTO statusCreateDTO) {
        var status = statusMapper.map(statusCreateDTO);
        statusRepository.save(status);

        return statusMapper.map(status);
    }

    public StatusDTO show(@PathVariable Long id) {
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id: " + id + " not found"));

        return statusMapper.map(status);
    }

    public StatusDTO update(@Valid @RequestBody StatusUpdateDTO statusData, @PathVariable Long id) {
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id: " + id + " not found"));
        statusMapper.update(statusData, status);
        status = statusRepository.save(status);

        return statusMapper.map(status);
    }

    public void destroy(@PathVariable Long id) {
        statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status with id: " + id + " not found"));
        statusRepository.deleteById(id);
    }
}
