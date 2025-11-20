package com.example.Vehiculo.controller;

import com.example.Vehiculo.model.Vehicle;
import com.example.Vehiculo.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VehicleController.class)
class VehicleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    @Test
    void crearVehiculo_devuelve201() throws Exception {
        Vehicle vehiculo = new Vehicle(null, "Toyota", "Corolla", 2020, "ABC123", "Rojo");
        Vehicle creado = new Vehicle("1", "Toyota", "Corolla", 2020, "ABC123", "Rojo");
        when(vehicleService.create(any(Vehicle.class))).thenReturn(creado);

        mockMvc.perform(post("/api/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.placa").value("ABC123"));
    }

    @Test
    void buscarVehiculos_devuelveLista() throws Exception {
        Vehicle v1 = new Vehicle("1", "Mazda", "3", 2019, "XYZ987", "Azul");
        when(vehicleService.search("Mazda")).thenReturn(List.of(v1));

        mockMvc.perform(get("/api/vehiculos/buscar").param("q", "Mazda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Mazda"));
    }

    @Test
    void obtenerPorPlaca_devuelve200() throws Exception {
        Vehicle v1 = new Vehicle("1", "Mazda", "3", 2019, "XYZ987", "Azul");
        when(vehicleService.getByPlaca("XYZ987")).thenReturn(Optional.of(v1));

        mockMvc.perform(get("/api/vehiculos/XYZ987"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("XYZ987"));
    }
}