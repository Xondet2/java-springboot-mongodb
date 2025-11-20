package com.example.Vehiculo.controller;

import com.example.Vehiculo.model.Vehicle;
import com.example.Vehiculo.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehicleController {
    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Vehicle vehicle, UriComponentsBuilder uriBuilder) {
        try {
            Vehicle created = service.create(vehicle);
            return ResponseEntity.created(
                    uriBuilder.path("/api/vehiculos/{id}")
                            .buildAndExpand(created.getId())
                            .toUri()
            ).body(created);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La placa ya existe");
        }
    }

    @GetMapping("/{placa}")
    public ResponseEntity<?> getByPlaca(@PathVariable String placa) {
        return service.getByPlaca(placa)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehículo no encontrado"));
    }

    @GetMapping("/buscar")
    public List<Vehicle> search(@RequestParam(name = "q", required = false) String q) {
        return service.search(q);
    }
}