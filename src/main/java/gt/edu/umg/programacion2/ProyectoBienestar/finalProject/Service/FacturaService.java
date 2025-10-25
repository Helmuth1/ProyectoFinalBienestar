package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;


import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.*;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.CitaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.FacturaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    public Factura crearFactura(Long idCliente, List<Long> idsCitas) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setFechaEmision(LocalDate.now());
        factura.setCitas(new ArrayList<>());

        double total = 0.0;

        for (Long idCita : idsCitas) {
            Cita citaExistente = citaRepository.findById(idCita)
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

            // 🔍 Validar que la cita ya fue atendida antes de facturar
            if (citaExistente.getEstado() != EstadoCita.ATENDIDA) {
                throw new RuntimeException("La cita con ID " + citaExistente.getId() + " aún no ha sido atendida.");
            }

            // 📦 Validar servicio y calcular precio
            Servicio servicio = servicioRepository.findById(citaExistente.getServicio().getId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            // 💡 Validar que el servicio tenga precio y duración válidos
            if (servicio.getPrecio() == null || servicio.getPrecio() <= 0) {
                throw new RuntimeException("El servicio '" + servicio.getNombre() + "' no tiene un precio válido.");
            }

            if (servicio.getDuracionMinutos() == null || servicio.getDuracionMinutos() <= 0) {
                throw new RuntimeException("El servicio '" + servicio.getNombre() + "' no tiene una duración válida.");
            }

            // 💰 Cambiar estado de la cita y asociarla a la factura
            citaExistente.setEstado(EstadoCita.COBRADA);
            citaExistente.setFactura(factura);

            // 📋 Agregar la cita a la factura y acumular el total
            factura.getCitas().add(citaExistente);
            total += servicio.getPrecio();

            // 💰 Cambiar estado de la cita y asociarla a la factura
            citaExistente.setEstado(EstadoCita.COBRADA);
            citaExistente.setFactura(factura);

            // 📋 Agregar la cita a la factura y acumular el total
            factura.getCitas().add(citaExistente);
            total += servicio.getPrecio();
        }

        factura.setTotal(total);
        return facturaRepository.save(factura);
    }

    public Factura marcarComoCobrada(Long idFactura) {
        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        // Verifica si ya está cobrada
        if (factura.getEstado() == EstadoFactura.COBRADA) {
            throw new RuntimeException("Esta factura ya fue cobrada.");
        }

        // Marcar citas asociadas como cobradas
        for (Cita cita : factura.getCitas()) {
            if (cita.getEstado() != EstadoCita.COBRADA) {
                cita.setEstado(EstadoCita.COBRADA);
                citaRepository.save(cita);
            }
        }

        // Cambiar estado de la factura
        factura.setEstado(EstadoFactura.COBRADA);

        // Calcular total actualizado
        double total = factura.getCitas().stream()
                .mapToDouble(c -> c.getServicio().getPrecio())
                .sum();

        factura.setTotal(total);
        return facturaRepository.save(factura);
    }

    public byte[] generarFacturaPDF(Long idFactura) {
        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font textoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("Factura No. " + factura.getId(), tituloFont));
            document.add(new Paragraph("Fecha de emisión: " + factura.getFechaEmision(), textoFont));
            document.add(new Paragraph("Cliente: " + factura.getCliente().getNombreCompleto(), textoFont));
            document.add(new Paragraph("Correo: " + factura.getCliente().getEmail(), textoFont));
            document.add(new Paragraph("Teléfono: " + factura.getCliente().getTelefono(), textoFont));
            document.add(new Paragraph("------------------------------------------------------------"));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell("Servicio");
            table.addCell("Duración (min)");
            table.addCell("Precio (Q)");

            for (Cita cita : factura.getCitas()) {
                table.addCell(cita.getServicio().getNombre());
                table.addCell(String.valueOf(cita.getServicio().getDuracionMinutos()));
                table.addCell(String.valueOf(cita.getServicio().getPrecio()));
            }

            document.add(table);

            document.add(new Paragraph("------------------------------------------------------------"));
            document.add(new Paragraph("Total: Q" + factura.getTotal(), tituloFont));
            document.add(new Paragraph("Estado: " + factura.getEstado(), textoFont));
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF de factura", e);
        }
    }


    public List<Factura> listar() {
        return facturaRepository.findAll();
    }

    public List<Factura> reporteGeneral(LocalDate inicio, LocalDate fin, Long idCliente, Long idServicio) {
        // Si no hay fechas, tomar todo el rango posible
        if (inicio == null) inicio = LocalDate.of(2000, 1, 1);
        if (fin == null) fin = LocalDate.now();

        List<Factura> facturas = facturaRepository.findAllByFechaEmisionBetween(inicio, fin);

        if (idCliente != null) {
            facturas = facturas.stream()
                    .filter(f -> f.getCliente().getId().equals(idCliente))
                    .toList();
        }

        if (idServicio != null) {
            facturas = facturas.stream()
                    .filter(f -> f.getCitas().stream()
                            .anyMatch(c -> c.getServicio().getId().equals(idServicio)))
                    .toList();
        }

        return facturas;
    }

    public List<Factura> obtenerPorCliente(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return facturaRepository.findByCliente(cliente);
    }

    public List<Factura> reportePorFechas(LocalDate inicio, LocalDate fin) {
        return facturaRepository.findAllByFechaEmisionBetween(inicio, fin);
    }
}
