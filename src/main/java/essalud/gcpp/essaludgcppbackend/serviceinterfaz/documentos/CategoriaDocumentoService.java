package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.CategoriaDocumento;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.CategoriaDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaDocumentoService {

    private final CategoriaDocumentoRepository categoriaDocumentoRepository;

    public CategoriaDocumentoService(CategoriaDocumentoRepository categoriaDocumentoRepository) {
        this.categoriaDocumentoRepository = categoriaDocumentoRepository;
    }

    public List<CategoriaDocumento> getCategorias() {
        return categoriaDocumentoRepository.findByFlagTrue();
    }

    public void saveCategoria(CategoriaDocumento categoria) {
        categoria.setFlag(true);
        categoriaDocumentoRepository.save(categoria);
    }
}
