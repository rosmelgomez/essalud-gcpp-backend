package essalud.gcpp.essaludgcppbackend.service.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.TipoDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.CatalogoExtension;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.TipoDocumentoRep;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.CatalogoExtensionRep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoDocumentoServ {

    private final TipoDocumentoRep tdr;
    private final CatalogoExtensionRep cer;

    public TipoDocumentoServ(TipoDocumentoRep tdr, CatalogoExtensionRep cer) {
        this.tdr = tdr;
        this.cer = cer;
    }

    public void saveTipoDocumento(TipoDocumentoDto tipoDocumentoDto) {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNombre(tipoDocumentoDto.getNombre());
        tipoDocumento.setDescripcion(tipoDocumentoDto.getDescripcion());
        tipoDocumento.setRequiere_aprobacion(tipoDocumentoDto.getRequiere_aprobacion());
        tipoDocumento.setMax_size_mb(tipoDocumentoDto.getMax_size_mb());
        tipoDocumento.setFlag(true);
        tipoDocumento.setDate_creation(LocalDateTime.now());
        tipoDocumento.setDate_update(LocalDateTime.now());

        List<CatalogoExtension> exts = new ArrayList<>();
        if (tipoDocumentoDto.getId_extensiones() != null && !tipoDocumentoDto.getId_extensiones().isEmpty()) {
            exts = cer.findAllById(tipoDocumentoDto.getId_extensiones());
        }
        tipoDocumento.setExtensionesPermitidas(exts);
        tdr.save(tipoDocumento);
    }

    public List<TipoDocumentoDto> getTipoDocumentos() {
        List<TipoDocumento> tipoDocumentos = tdr.findAll();
        List<TipoDocumentoDto> tipoDocumentosDto = new ArrayList<>();
        for (TipoDocumento tipoDocumento : tipoDocumentos) {
            tipoDocumentosDto.add(tipoDocumentoDto(tipoDocumento));
        }
        return tipoDocumentosDto;
    }

    public void updateTipoDocumento(TipoDocumentoDto tipoDocumentoDto) {
        TipoDocumento tipoDocumento = tdr.findById(tipoDocumentoDto.getId()).orElse(null);
        if (tipoDocumento != null) {
            tipoDocumento.setNombre(tipoDocumentoDto.getNombre());
            tipoDocumento.setDescripcion(tipoDocumentoDto.getDescripcion());
            tipoDocumento.setMax_size_mb(tipoDocumentoDto.getMax_size_mb());
            tipoDocumento.setFlag(tipoDocumentoDto.getFlag());
            tipoDocumento.setDate_update(LocalDateTime.now());

            if (tipoDocumento.getExtensionesPermitidas() != null) {
                tipoDocumento.getExtensionesPermitidas().clear();
            } else {
                tipoDocumento.setExtensionesPermitidas(new ArrayList<>());
            }

            if (tipoDocumentoDto.getId_extensiones() != null && !tipoDocumentoDto.getId_extensiones().isEmpty()) {
                List<CatalogoExtension> exts = cer.findAllById(tipoDocumentoDto.getId_extensiones());
                tipoDocumento.getExtensionesPermitidas().addAll(exts);
            }
            tdr.save(tipoDocumento);
        }
    }

    @SuppressWarnings("null")
    private TipoDocumentoDto tipoDocumentoDto(TipoDocumento tipoDocumento) {
        if (tipoDocumento == null) {
            return null;
        }
        String csv = "";
        List<Integer> idExtensions = new ArrayList<>();
        if (tipoDocumento.getExtensionesPermitidas() != null) {
            csv = tipoDocumento.getExtensionesPermitidas().stream()
                    .map(CatalogoExtension::getNombre)
                    .collect(Collectors.joining(","));
            idExtensions = tipoDocumento.getExtensionesPermitidas().stream()
                    .map(CatalogoExtension::getId)
                    .collect(Collectors.toList());
        }
        TipoDocumentoDto dto = new TipoDocumentoDto();
        dto.setId(tipoDocumento.getId());
        dto.setNombre(tipoDocumento.getNombre());
        dto.setDescripcion(tipoDocumento.getDescripcion());
        dto.setExtensiones_permitidas(csv);
        dto.setId_extensiones(idExtensions);
        dto.setRequiere_aprobacion(tipoDocumento.getRequiere_aprobacion());
        dto.setMax_size_mb(tipoDocumento.getMax_size_mb());
        dto.setFlag(tipoDocumento.getFlag());
        dto.setDate_creation(tipoDocumento.getDate_creation());
        dto.setDate_update(tipoDocumento.getDate_update());
        return dto;
    }
}
