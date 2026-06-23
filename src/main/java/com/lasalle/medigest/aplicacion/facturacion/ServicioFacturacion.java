package com.lasalle.medigest.aplicacion.facturacion;

import com.lasalle.medigest.dominio.admision.Paciente;
import com.lasalle.medigest.dominio.facturacion.*;
import com.lasalle.medigest.persistencia.admision.PacienteRepository;
import com.lasalle.medigest.persistencia.facturacion.FacturaRepository;
import com.lasalle.medigest.persistencia.facturacion.ItemFacturableRepository;
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
    private final PacienteRepository pacienteRepository;
    private final PagoParticular pagoParticular;
    private final PagoSIS pagoSIS;
    private final PagoSeguroPrivado pagoSeguroPrivado;

    public Factura generarFactura(Long pacienteId, Long itemId, TipoCobertura tipoCobertura) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + pacienteId));
        ItemFacturable item = itemFacturableRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado: " + itemId));

        EstrategiaPago estrategia = resolverEstrategia(tipoCobertura);
        CalculadorFactura.ResultadoFactura resultado = new CalculadorFactura(estrategia).calcular(item);

        return facturaRepository.save(Factura.builder()
                .paciente(paciente)
                .fechaEmision(LocalDateTime.now())
                .costoTotal(resultado.costoTotal())
                .montoCubierto(resultado.montoCubierto())
                .montoPaciente(resultado.montoPaciente())
                .tipoCobertura(tipoCobertura != null ? tipoCobertura : TipoCobertura.PARTICULAR)
                .detalle(item.getNombre())
                .build());
    }

    public ServicioSimple crearServicioSimple(String nombre, double precio) {
        return (ServicioSimple) itemFacturableRepository.save(new ServicioSimple(nombre, precio));
    }

    public PaqueteServicios crearPaquete(String nombre, List<Long> itemIds) {
        PaqueteServicios paquete = new PaqueteServicios(nombre);
        itemIds.forEach(id -> {
            ItemFacturable item = itemFacturableRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Item no encontrado: " + id));
            paquete.agregar(item);
        });
        return (PaqueteServicios) itemFacturableRepository.save(paquete);
    }

    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    public List<ItemFacturable> listarServicios() {
        return itemFacturableRepository.findAll();
    }

    public Optional<Factura> buscarFacturaPorId(Long id) {
        return facturaRepository.findById(id);
    }

    private EstrategiaPago resolverEstrategia(TipoCobertura tipo) {
        if (tipo == null) return pagoParticular;
        return switch (tipo) {
            case SIS            -> pagoSIS;
            case SEGURO_PRIVADO -> pagoSeguroPrivado;
            default             -> pagoParticular;
        };
    }
}
