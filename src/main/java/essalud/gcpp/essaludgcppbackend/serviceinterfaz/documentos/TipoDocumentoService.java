package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.TipoDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.TipoDocumentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tdr;
    public TipoDocumentoService(TipoDocumentoRepository tdr) {
        this.tdr = tdr;
    }

    public void saveTipoDocumento(TipoDocumentoDto tipoDocumentoDto) {
     TipoDocumento tipoDocumento = new TipoDocumento();
     tipoDocumento.setNombre(tipoDocumentoDto.getNombre());
     tipoDocumento.setDescripcion(tipoDocumentoDto.getDescripcion());
     tipoDocumento.setExtensiones_permitidas(tipoDocumentoDto.getExtensiones_permitidas());
     tipoDocumento.setRequiere_aprobacion(tipoDocumentoDto.getRequiere_aprobacion());
     tipoDocumento.setMax_size_mb(tipoDocumentoDto.getMax_size_mb());
     tipoDocumento.setFlag(true);
     tipoDocumento.setDate_creation(LocalDateTime.now());
     tipoDocumento.setDate_update(LocalDateTime.now());
     tdr.save(tipoDocumento);
    }

    public List<TipoDocumentoDto> getTipoDocumentos() {
        List<TipoDocumento>  tipoDocumentos = tdr.findAll();
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
            tipoDocumento.setExtensiones_permitidas(tipoDocumentoDto.getExtensiones_permitidas());
            tipoDocumento.setMax_size_mb(tipoDocumentoDto.getMax_size_mb());
            tipoDocumento.setFlag(tipoDocumentoDto.getFlag());
            tipoDocumento.setDate_creation(LocalDateTime.now());
            tipoDocumento.setDate_update(LocalDateTime.now());
            tdr.save(tipoDocumento);
        }
    }

    public TipoDocumentoDto getTipoDocumentoById(Integer id) {
        TipoDocumento tipoDocumento = tdr.findById(id).orElse(null);
        return tipoDocumentoDto(tipoDocumento);
    }

    private TipoDocumentoDto tipoDocumentoDto(TipoDocumento tipoDocumento) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tipoDocumento, TipoDocumentoDto.class);
    }
}
