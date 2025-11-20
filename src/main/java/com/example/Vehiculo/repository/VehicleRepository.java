package com.example.Vehiculo.repository;

import com.example.Vehiculo.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    Optional<Vehicle> findByPlaca(String placa);
    List<Vehicle> findByMarcaContainingIgnoreCase(String marca);
    List<Vehicle> findByModeloContainingIgnoreCase(String modelo);
    List<Vehicle> findByAnio(Integer anio);

    @Query("{ $or: [ { 'marca': { $regex: ?0, $options: 'i' } }, { 'modelo': { $regex: ?0, $options: 'i' } }, { 'placa': { $regex: ?0, $options: 'i' } } ] }")
    List<Vehicle> search(String term);
}