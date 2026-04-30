package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import essalud.gcpp.essaludgcppbackend.dtos.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.Documento;
import essalud.gcpp.essaludgcppbackend.repositories.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.FileRepository;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final DocumentoRepository documentoRepository;
    public FileService(FileRepository fileRepository, DocumentoRepository documentoRepository) {
        this.fileRepository = fileRepository;
        this.documentoRepository=documentoRepository;
    }

    public byte[] findArchivo(String name){
        return fileRepository.findArchivo(name);
    }

    public void insertDocumento(DocumentoDto  documento) {
        Documento documentoNew = new Documento();
        documentoNew.setNombre(documento.getNombre());
        documentoNew.setDescripcion(documento.getDescripcion());
        documentoNew.setUsuario_creation(documento.getUsuario_creation() );
        documentoNew.setUsuario_update(documento.getUsuario_update() );
        documentoNew.setDate_creation(LocalDateTime.now());
        documentoNew.setDate_update(LocalDateTime.now());
        documentoNew.setArchivo(documento.getArchivo() != null ? documento.getArchivo() : new byte[0]);
        documentoNew.setTipo_archivo(documento.getTipo_archivo());
        documentoRepository.save(documentoNew);
    }

    public List<DocumentoDto> getDocumentos(){
        List<Documento> documentos = documentoRepository.findAll();
        List<DocumentoDto> documentosDtos = new ArrayList<>();

        for (Documento documento : documentos) {
            DocumentoDto documentoDto = new DocumentoDto();
            documentoDto.setId(documento.getId());
            documentoDto.setNombre(documento.getNombre());
            documentoDto.setDescripcion(documento.getDescripcion());
            documentoDto.setUsuario_creation(documento.getUsuario_creation() );
            documentoDto.setUsuario_update(documento.getUsuario_update() );
            documentoDto.setDate_creation(LocalDateTime.now());
            documentoDto.setDate_update(LocalDateTime.now());
            documentosDtos.add(documentoDto);
        }
       return documentosDtos;
    }

    public void updateDocumento(DocumentoDto  documento) {
        Documento documentoNew = documentoRepository.findById(documento.getId()).orElse(null);
        if (documentoNew != null) {
            Documento documentoUpdated = new Documento();
            documentoUpdated.setNombre(documento.getNombre());
            documentoUpdated.setDescripcion(documento.getDescripcion());
            documentoUpdated.setUsuario_update(documento.getUsuario_update() );
            documentoUpdated.setDate_update(LocalDateTime.now());
            documentoRepository.save(documentoUpdated);
        }
    }

    public void deleteDocumento(Integer id) {
        documentoRepository.deleteById(id);
    }


    public byte[] getDocumento(Integer id) {
        Documento documento = documentoRepository.findById(id).orElse(null);
        if (documento != null) {
            return documento.getArchivo() != null ? documento.getArchivo() : new byte[0];
        }
        return new byte[0];
    }



    public Resource loadResource(Integer id) {
        byte[] data = getDocumento(id);

        if (data.length == 0) {
            throw new RuntimeException("File not found");
        }

        return new ByteArrayResource(data);
    }


   public ResponseEntity<Resource> getFileResponse(Integer id) {
        Documento doc = documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        if (doc.getArchivo() == null || doc.getArchivo().length == 0) {
            throw new RuntimeException("Archivo vacío");
        }

        Resource resource = new ByteArrayResource(doc.getArchivo());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getTipo_archivo()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getNombre() + "\"")
                .body(resource);
    }






}
