package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import essalud.gcpp.essaludgcppbackend.dtos.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
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

    public void insertDocumento(DocumentoDto  documento) throws IOException {
        Documento documentoNew = new Documento();
        documentoNew.setTitulo(documento.getTitulo());
        documentoNew.setDescripcion(documento.getDescripcion());
        documentoNew.setUsuario_creation(documento.getUsuario_creation() );
        documentoNew.setUsuario_update(documento.getUsuario_update() );
        documentoNew.setDate_creation(LocalDateTime.now());
        documentoNew.setDate_update(LocalDateTime.now());
        documentoNew.setArchivo( documento.getArchivo().getBytes());
        documentoNew.setExtencion(documento.getExtencion());
        documentoRepository.save(documentoNew);
    }

    public List<DocumentoDto> getDocumentos(){
        List<Documento> documentos = documentoRepository.findAll();
        List<DocumentoDto> documentosDtos = new ArrayList<>();

        for (Documento documento : documentos) {
            DocumentoDto documentoDto = new DocumentoDto();
            documentoDto.setId(documento.getId());
            documentoDto.setTitulo(documento.getTitulo());
            documentoDto.setDescripcion(documento.getDescripcion());
            documentoDto.setExtencion(documento.getExtencion());
            documentoDto.setUsuario_creation(documento.getUsuario_creation() );
            documentoDto.setUsuario_update(documento.getUsuario_update() );
            documentoDto.setDate_creation(LocalDateTime.now());
            documentoDto.setDate_update(LocalDateTime.now());
            documentosDtos.add(documentoDto);
        }
       return documentosDtos;
    }

    public void updateDocumento(DocumentoDto  documento) throws IOException {
        Documento documentoUpdate = documentoRepository.findById(documento.getId()).orElse(null);
        if (documentoUpdate != null) {
            documentoUpdate.setTitulo(documento.getTitulo());
            documentoUpdate.setDescripcion(documento.getDescripcion());
            documentoUpdate.setUsuario_update(documento.getUsuario_update() );
            documentoUpdate.setDate_update(LocalDateTime.now());
            documentoUpdate.setArchivo(documento.getArchivo().getBytes());
            documentoRepository.save(documentoUpdate);
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
            throw new RuntimeException("documento no encontrado");
        }
        return new ByteArrayResource(data);
    }

}
