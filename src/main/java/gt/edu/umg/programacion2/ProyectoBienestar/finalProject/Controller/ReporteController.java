package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cita;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Factura;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.CitaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.FacturaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

        private final FacturaRepository facturaRepository;
        private final CitaRepository citaRepository;
        private final ServicioRepository servicioRepository;
        private final ClienteRepository clienteRepository;

        // 📊 Reporte general (todo)
        @GetMapping("/general")
        public ResponseEntity<Map<String, Object>> reporteGeneral() {
            Map<String, Object> datos = new HashMap<>();
            datos.put("totalClientes", clienteRepository.count());
            datos.put("totalServicios", servicioRepository.count());
            datos.put("totalCitas", citaRepository.count());
            datos.put("totalFacturas", facturaRepository.count());
            datos.put("totalIngresos", facturaRepository.findAll().stream()
                    .mapToDouble(Factura::getTotal)
                    .sum());
            return ResponseEntity.ok(datos);
        }

        // 📅 Reporte por rango de fechas
        @GetMapping("/por-fechas")
        public ResponseEntity<List<Factura>> reportePorFechas(
                @RequestParam LocalDate inicio,
                @RequestParam LocalDate fin) {
            return ResponseEntity.ok(facturaRepository.findAllByFechaEmisionBetween(inicio, fin));
        }

    @GetMapping("/descargar")
    public ResponseEntity<byte[]> descargarReportePDF() {
        try {
            // Crear flujo en memoria
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Encabezado
            document.add(new Paragraph("REPORTE DE FACTURAS", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));
            document.add(new Paragraph("Generado el: " + LocalDate.now()));
            document.add(new Paragraph(" ")); // Espacio en blanco

            // Obtener facturas
            List<Factura> facturas = facturaRepository.findAll();

            if (facturas.isEmpty()) {
                document.add(new Paragraph("No hay facturas registradas."));
            } else {
                for (Factura f : facturas) {
                    document.add(new Paragraph("Factura ID: " + f.getId()));
                    document.add(new Paragraph("Cliente: " + (f.getCliente() != null ? f.getCliente().getNombreCompleto() : "N/A")));
                    document.add(new Paragraph("Fecha: " + f.getFechaEmision()));
                    document.add(new Paragraph("Total: Q" + f.getTotal()));
                    document.add(new Paragraph("------------------------------"));
                }
            }

            document.close();

            byte[] pdfBytes = outputStream.toByteArray();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=reporte_facturas.pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generarReportePDF() {
        try {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();

            Document documento = new Document();
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // Título
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            documento.add(new Paragraph("Reporte General de Bienestar UMG", tituloFont));
            documento.add(new Paragraph(" "));

            // Datos
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            documento.add(new Paragraph("Fecha de generación: " + LocalDate.now(), normalFont));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Resumen general:", normalFont));
            documento.add(new Paragraph(" "));

            long totalClientes = clienteRepository.count();
            long totalServicios = servicioRepository.count();
            long totalCitas = citaRepository.count();
            long totalFacturas = facturaRepository.count();
            double totalIngresos = facturaRepository.findAll()
                    .stream()
                    .mapToDouble(Factura::getTotal)
                    .sum();

            documento.add(new Paragraph("Total de clientes: " + totalClientes));
            documento.add(new Paragraph("Total de servicios: " + totalServicios));
            documento.add(new Paragraph("Total de citas: " + totalCitas));
            documento.add(new Paragraph("Total de facturas: " + totalFacturas));
            documento.add(new Paragraph("Total de ingresos: Q" + String.format("%.2f", totalIngresos)));

            documento.close();

            byte[] pdfBytes = salida.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ReporteGeneral.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

        // 👤 Reporte por cliente
        @GetMapping("/por-cliente/{idCliente}")
        public ResponseEntity<List<Factura>> reportePorCliente(@PathVariable Long idCliente) {
            Cliente cliente = clienteRepository.findById(idCliente)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            return ResponseEntity.ok(facturaRepository.findByCliente(cliente));
        }

        // 💆‍♀️ Reporte por servicio (facturas que incluyan citas con ese servicio)
        @GetMapping("/por-servicio/{idServicio}")
        public ResponseEntity<List<Factura>> reportePorServicio(@PathVariable Long idServicio) {
            List<Cita> citas = citaRepository.findAll().stream()
                    .filter(c -> c.getServicio().getId().equals(idServicio))
                    .toList();

            Set<Factura> facturas = citas.stream()
                    .map(Cita::getFactura)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(new ArrayList<>(facturas));
        }
    }

