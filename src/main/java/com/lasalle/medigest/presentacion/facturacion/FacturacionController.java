package com.lasalle.medigest.presentacion.facturacion;

import com.lasalle.medigest.aplicacion.facturacion.ServicioFacturacion;
import com.lasalle.medigest.dominio.facturacion.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
public class FacturacionController {

    private final ServicioFacturacion servicioFacturacion;

    // RF08 — generar factura (Strategy aplicada)
    @PostMapping("/facturas")
    public ResponseEntity<Factura> generarFactura(@RequestBody Map<String, Object> req) {
        Long pacienteId = Long.valueOf(req.get("pacienteId").toString());
        Long itemId     = Long.valueOf(req.get("itemId").toString());
        TipoCobertura cobertura = req.get("tipoCobertura") != null
                ? TipoCobertura.valueOf(req.get("tipoCobertura").toString())
                : TipoCobertura.PARTICULAR;
        return ResponseEntity.ok(servicioFacturacion.generarFactura(pacienteId, itemId, cobertura));
    }

    // RF10 — crear servicio simple (hoja del Composite)
    @PostMapping("/servicios")
    public ResponseEntity<ServicioSimple> crearServicio(@RequestBody Map<String, Object> req) {
        String nombre  = (String) req.get("nombre");
        double precio  = Double.parseDouble(req.get("precioBase").toString());
        return ResponseEntity.ok(servicioFacturacion.crearServicioSimple(nombre, precio));
    }

    // RF10 — crear paquete de servicios (nodo Composite)
    @PostMapping("/paquetes")
    public ResponseEntity<PaqueteServicios> crearPaquete(@RequestBody Map<String, Object> req) {
        String nombre = (String) req.get("nombre");
        @SuppressWarnings("unchecked")
        List<Long> itemIds = ((List<Integer>) req.get("itemIds"))
                .stream().map(Long::valueOf).toList();
        return ResponseEntity.ok(servicioFacturacion.crearPaquete(nombre, itemIds));
    }

    @GetMapping("/facturas")
    public ResponseEntity<List<Factura>> listarFacturas() {
        return ResponseEntity.ok(servicioFacturacion.listarFacturas());
    }

    @GetMapping("/facturas/{id}")
    public ResponseEntity<Factura> buscarFactura(@PathVariable Long id) {
        return servicioFacturacion.buscarFacturaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // RF09 — listar servicios/paquetes disponibles
    @GetMapping("/servicios")
    public ResponseEntity<List<ItemFacturable>> listarServicios() {
        return ResponseEntity.ok(servicioFacturacion.listarServicios());
    }
}
