package essalud.gcpp.essaludgcppbackend.service.encuesta;

import essalud.gcpp.essaludgcppbackend.dtos.encuesta.VariableDto;
import essalud.gcpp.essaludgcppbackend.model.encuesta.Variable;
import essalud.gcpp.essaludgcppbackend.repositories.encuesta.VariableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VariableService {

    private final VariableRepository VR;
    public VariableService(VariableRepository VR) {
        this.VR = VR;
    }

    public List<VariableDto> getVariables() {
        List<Variable>  variables = VR.findAll();
        List<VariableDto> variableDtos = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Variable variable : variables) {
            if (variable.getActive()){
                VariableDto variableDto = modelMapper.map(variable, VariableDto.class);
                variableDto.setId_subcategoria(variable.getSubcategoria_variable().getId());
                variableDtos.add(variableDto);
            }
        }
        return variableDtos;
    }
}
