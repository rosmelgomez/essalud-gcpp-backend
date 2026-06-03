package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.TipoDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.EstablecimientosAsistencialesService;

@Service
@Transactional
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final UsersRepository usersRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstablecimientosAsistencialesService establecimientosAsistencialesService;

    public DocumentoService(DocumentoRepository documentoRepository, UsersRepository usersRepository,
                            TipoDocumentoRepository tipoDocumentoRepository,
                            EstablecimientosAsistencialesService establecimientosAsistencialesService) {
        this.documentoRepository = documentoRepository;
        this.usersRepository = usersRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.establecimientosAsistencialesService = establecimientosAsistencialesService;
    }

    public void saveDocumento(DocumentoDto documento)  {
        Users user= usersRepository.findById(documento.getId_user_creation()).orElse(null);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(documento.getId_tipo_documento()).orElse(null);

        if (user != null && tipoDocumento != null) {
            Documento documentoEntity;
            if (documento.getId() != null && documento.getId() > 0) {
                documentoEntity = documentoRepository.findById(documento.getId()).orElse(new Documento());
                if (documentoEntity.getUser_documento() != null && !documentoEntity.getUser_documento().getId().equals(documento.getId_user_creation())) {
                    throw new org.springframework.security.access.AccessDeniedException("No tiene permisos para modificar este documento.");
                }
            } else {
                documentoEntity = new Documento();
                documentoEntity.setDate_creation(LocalDateTime.now());
                documentoEntity.setUser_documento(user);
            }
            documentoEntity.setCodigo(documento.getCodigo());
            documentoEntity.setTitulo(documento.getTitulo());
            documentoEntity.setDescripcion(documento.getDescripcion());
            documentoEntity.setCodred(documento.getCodred());
            documentoEntity.setCodcas(documento.getCodcas());
            documentoEntity.setDate_update(LocalDateTime.now());
            documentoEntity.setFlag(documento.getFlag() != null ? documento.getFlag() : true);
            documentoEntity.setPublico(documento.getPublico());
            documentoEntity.setSoloMiRed(documento.getSolo_mi_red() != null ? documento.getSolo_mi_red() : false);
            documentoEntity.setTipo_documento(tipoDocumento);
            documentoRepository.save(documentoEntity);
        }
    }

    public List<Map<String, Object>> getDocumentos(){
        return enrichDocumentos(documentoRepository.getDocumentosConDetalles());
    }

    public List<Map<String, Object>> getDocumentosByCodred(String codred){
        return enrichDocumentos(documentoRepository.getDocumentosConDetallesByCodred(codred));
    }

    public List<Map<String, Object>> getDocumentosByCodcas(String codcas){
        return enrichDocumentos(documentoRepository.getDocumentosConDetallesByCodcas(codcas));
    }

    public List<Map<String, Object>> getDocumentosByUser(Integer iduser){
        return enrichDocumentos(documentoRepository.getDocumentosConDetallesByUser(iduser));
    }

    private List<Map<String, Object>> enrichDocumentos(List<Map<String, Object>> documentos) {
        List<Map<String, Object>> enrichedList = new ArrayList<>();
        
        java.util.Map<String, String> redMap = new java.util.HashMap<>();
        try {
            for (Map<String, Object> r : establecimientosAsistencialesService.getRedes()) {
                if (r.get("redasiscod") != null && r.get("redasisdes") != null) {
                    redMap.put(r.get("redasiscod").toString(), r.get("redasisdes").toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar redes para enriquecer documentos: " + e.getMessage());
        }

        java.util.Map<String, String> centroMap = new java.util.HashMap<>();
        try {
            for (Map<String, Object> c : establecimientosAsistencialesService.getCentros()) {
                if (c.get("cenasicod") != null && c.get("cenasides") != null) {
                    centroMap.put(c.get("cenasicod").toString(), c.get("cenasides").toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar centros para enriquecer documentos: " + e.getMessage());
        }

        for (Map<String, Object> doc : documentos) {
            java.util.Map<String, Object> mutableDoc = new java.util.HashMap<>(doc);
            
            String codred = mutableDoc.get("codred") != null ? mutableDoc.get("codred").toString() : null;
            String codcas = mutableDoc.get("codcas") != null ? mutableDoc.get("codcas").toString() : null;
            
            mutableDoc.put("red_nombre", (codred != null && redMap.containsKey(codred)) ? redMap.get(codred) : "Sin Red");
            mutableDoc.put("centro_nombre", (codcas != null && centroMap.containsKey(codcas)) ? centroMap.get(codcas) : "Sin Centro");
            
            enrichedList.add(mutableDoc);
        }
        return enrichedList;
    }

    public DocumentoDto getDocumentoById(Integer id){
        Documento documento = documentoRepository.findById(id).orElse(null);
        return documentoDto(documento);
    }

    public DocumentoDto getDocumentoByIdVersion(Integer idversion){
        Documento documento= documentoRepository.getDocumentoByIdVersion(idversion);
        return documentoDto(documento);
    }

    public void deleteDocumento(Integer id, Integer userId) {
        Documento doc = documentoRepository.findById(id).orElse(null);
        Users user = usersRepository.findById(userId).orElse(null);
        if (doc != null && user != null) {
            if (!doc.getUser_documento().getId().equals(userId)) {
                throw new org.springframework.security.access.AccessDeniedException("No tiene permisos para eliminar este documento.");
            }
            doc.setFlag(false);
            doc.setDeletedAt(LocalDateTime.now());
            doc.setDeletedBy(user);
            documentoRepository.save(doc);
        }
    }

    private DocumentoDto documentoDto(Documento documento){
        ModelMapper modelMapper = new ModelMapper();
        DocumentoDto documentoDto = modelMapper.map(documento, DocumentoDto.class);
        documentoDto.setId_user_creation(documento.getUser_documento().getId());
        documentoDto.setId_tipo_documento(documento.getTipo_documento().getId());
        documentoDto.setSolo_mi_red(documento.getSoloMiRed());
        return documentoDto;
    }




}
