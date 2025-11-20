package com.example.Vehiculo.service;

import com.example.Vehiculo.model.Vehicle;
import com.example.Vehiculo.repository.VehicleRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public Vehicle create(Vehicle vehicle) throws DuplicateKeyException {
        return repository.save(vehicle);
    }

    public Optional<Vehicle> getByPlaca(String placa) {
        return repository.findByPlaca(placa);
    }

    public List<Vehicle> search(String term) {
        if (term == null || term.isBlank()) {
            return repository.findAll();
        }
        return repository.search(term);
    }
}