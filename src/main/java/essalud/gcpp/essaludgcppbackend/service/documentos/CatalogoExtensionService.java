package essalud.gcpp.essaludgcppbackend.service.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.CatalogoExtensionDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.CatalogoExtension;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.CatalogoExtensionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CatalogoExtensionService {

    private final CatalogoExtensionRepository cer;

    public CatalogoExtensionService(CatalogoExtensionRepository cer) {
        this.cer = cer;
    }

    public List<CatalogoExtensionDto> getCatalogo() {
        List<CatalogoExtension> list = cer.findAll();
        List<CatalogoExtensionDto> dtos = new ArrayList<>();
        for (CatalogoExtension entity : list) {
            dtos.add(new CatalogoExtensionDto(entity.getId(), entity.getNombre(), entity.getMimeType()));
        }
        return dtos;
    }
}
