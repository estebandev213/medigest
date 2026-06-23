package com.lasalle.medigest.dominio.admision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosPaciente {
    private String dni;
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private String direccion;
}
