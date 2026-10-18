package essalud.gcpp.essaludgcppbackend.service.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.CatalogoExtensionDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.CatalogoExtension;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.CatalogoExtensionRep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CatalogoExtensionServ {

    private final CatalogoExtensionRep cer;

    public CatalogoExtensionServ(CatalogoExtensionRep cer) {
        this.cer = cer;
    }

    public List<CatalogoExtensionDto> getCatalogo() {
        List<CatalogoExtension> list = cer.findAll();
        List<CatalogoExtensionDto> dtos = new ArrayList<>();
        for (CatalogoExtension entity : list) {
            CatalogoExtensionDto dto = new CatalogoExtensionDto();
            dto.setId(entity.getId());
            dto.setNombre(entity.getNombre());
            dto.setMimeType(entity.getMimeType());
            dtos.add(dto);
        }
        return dtos;
    }
}
