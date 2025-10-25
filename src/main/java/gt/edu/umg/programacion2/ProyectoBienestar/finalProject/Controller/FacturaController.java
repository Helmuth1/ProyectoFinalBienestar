package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cita;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Factura;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.CitaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.FacturaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service.FacturaService;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.request.FacturaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping("/registrar")
    public ResponseEntity<Factura> crearFactura(@RequestBody FacturaRequest request) {
        Factura nuevaFactura = facturaService.crearFactura(request.getIdCliente(), request.getIdsCitas());
        return ResponseEntity.ok(nuevaFactura);
    }

    @GetMapping
    public ResponseEntity<List<Factura>> listarFacturas() {
        List<Factura> facturas = facturaService.listar();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Factura>> obtenerPorCliente(@PathVariable Long idCliente) {
        List<Factura> facturas = facturaService.obtenerPorCliente(idCliente);
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{idFactura}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPDF(@PathVariable Long idFactura) {
        byte[] pdfBytes = facturaService.generarFacturaPDF(idFactura);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura_" + idFactura + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/reporte")
    public ResponseEntity<List<Factura>> reporteGeneral(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idServicio
    ) {
        List<Factura> resultado = facturaService.reporteGeneral(inicio, fin, idCliente, idServicio);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}/cobrar")
    public ResponseEntity<?> marcarComoCobrada(@PathVariable Long id) {
        try {
            Factura facturaCobrada = facturaService.marcarComoCobrada(id);
            return ResponseEntity.ok(facturaCobrada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
