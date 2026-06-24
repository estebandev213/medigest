package com.lasalle.medigest.aplicacion.facturacion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.facturacion.*;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import com.lasalle.medigest.persistencia.facturacion.FacturaRepository;
import com.lasalle.medigest.persistencia.facturacion.ItemFacturableRepository;
import com.lasalle.medigest.persistencia.facturacion.PaqueteServiciosRepository;
import com.lasalle.medigest.persistencia.facturacion.ServicioSimpleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioFacturacion {

    private final FacturaRepository facturaRepository;
    private final ItemFacturableRepository itemFacturableRepository;
    private final ServicioSimpleRepository servicioSimpleRepository;
    private final PaqueteServiciosRepository paqueteServiciosRepository;
    private final PacienteRepository pacienteRepository;
    private final PagoParticular pagoParticular;
    private final PagoSIS pagoSIS;
    private final PagoSeguroPrivado pagoSeguroPrivado;

    /** RF08 — Generar factura aplicando Strategy de cobertura */
    public Factura generarFactura(Long pacienteId, Long itemId, TipoCobertura tipoCobertura) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + pacienteId));
        ItemFacturable item = itemFacturableRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado: " + itemId));

        // Strategy: elegir estrategia según tipo de cobertura
        EstrategiaPago estrategia = resolverEstrategia(tipoCobertura);
        // Composite + Strategy: calcularCosto() puede ser simple o suma recursiva de paquete
        CalculadorFactura.ResultadoFactura resultado = new CalculadorFactura(estrategia).calcular(item);

        TipoCobertura coberturaFinal = tipoCobertura != null ? tipoCobertura : TipoCobertura.PARTICULAR;

        return facturaRepository.save(Factura.builder()
                .paciente(paciente)
                .item(item)
                .fechaEmision(LocalDateTime.now())
                .costoTotal(resultado.costoTotal())
                .montoCubierto(resultado.montoCubierto())
                .montoPaciente(resultado.montoPaciente())
                .tipoCobertura(coberturaFinal)
                .detalle(item.getNombre())
                .build());
    }

    /** RF10 — Crear servicio simple (hoja Composite) */
    public ServicioSimple crearServicioSimple(String nombre, double precio) {
        return servicioSimpleRepository.save(new ServicioSimple(nombre, precio));
    }

    /** RF10 — Crear paquete de servicios (nodo Composite) */
    public PaqueteServicios crearPaquete(String nombre, List<Long> itemIds) {
        PaqueteServicios paquete = new PaqueteServicios(nombre);
        itemIds.forEach(id -> {
            ItemFacturable item = itemFacturableRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Item no encontrado: " + id));
            paquete.agregar(item);
        });
        return paqueteServiciosRepository.save(paquete);
    }

    /** RF09 — Listar todos los ítems facturables (servicios + paquetes) */
    public List<ItemFacturable> listarServicios() {
        return itemFacturableRepository.findAll();
    }

    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    public List<Factura> listarFacturasPorPaciente(Long pacienteId) {
        return facturaRepository.findByPacienteId(pacienteId);
    }

    public Optional<Factura> buscarFacturaPorId(Long id) {
        return facturaRepository.findById(id);
    }

    // ── Strategy: resolver estrategia según tipo de cobertura ──────────────
    private EstrategiaPago resolverEstrategia(TipoCobertura tipo) {
        if (tipo == null) return pagoParticular;
        return switch (tipo) {
            case SIS            -> pagoSIS;
            case SEGURO_PRIVADO -> pagoSeguroPrivado;
            default             -> pagoParticular;
        };
    }
}