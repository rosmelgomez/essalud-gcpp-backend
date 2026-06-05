package essalud.gcpp.essaludgcppbackend.model.documentos;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogo_extension")
public class CatalogoExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;
}
