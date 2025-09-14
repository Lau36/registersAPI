package com.example.registers_api.mappers;

import com.example.registers_api.models.ResearchLayerGroup;
import com.example.registers_api.models.Variable;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.request.ResearchLayerGroupRequest;
import com.example.registers_api.request.VariablesGroupRequest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.registers_api.utils.Constants.NUMBER_TYPE;
import static com.example.registers_api.utils.Constants.STRING_TYPE;

public class ResearchLayerMapper2 {

    private static final String ERR_VALUE_MUST_BE_NUMBER = "El valor de la variable debe ser numérico.";
    private static final String ERR_VALUE_MUST_BE_STRING = "El valor de la variable debe ser String.";
    private static final String ERR_TYPE_UNDEFINED       = "Tipo de variable no soportado.";

    public ResearchLayerGroup toResearchLayerGroup(ResearchLayerGroupRequest req,
                                       Map<String, VariableCollection> variablesCatalogById) {
        return toDomain(req, id -> Optional.ofNullable(variablesCatalogById.get(id)));
    }

    public ResearchLayerGroup toDomain(ResearchLayerGroupRequest req,
                                       Function<String, Optional<VariableCollection>> variableResolver) {

        Objects.requireNonNull(req, "ResearchLayerGroupRequest no puede ser null");
        ResearchLayerGroup out = new ResearchLayerGroup();
        out.setResearchLayerId(req.getResearchLayerId());
        out.setResearchLayerName(req.getResearchLayerName());

        List<Variable> variables = (req.getVariablesInfo() == null)
                ? Collections.emptyList()
                : req.getVariablesInfo()
                .stream()
                .map(vr -> toVariable(vr, variableResolver.apply(vr.getId())))
                .collect(Collectors.toList());

        out.setVariables(variables);
        return out;
    }

    private Variable toVariable(VariablesGroupRequest vr, Optional<VariableCollection> fromDb) {
        if (vr == null) return null;

        Variable variable = new Variable();
        variable.setId(vr.getId());
        variable.setName(fromDb.map(VariableCollection::getVariableName)
                .orElse(vr.getName()));
        variable.setType(vr.getType());

        Object value = vr.getValue();

        switch (vr.getType()) {
            case NUMBER_TYPE: {
                if (value == null) {
                    variable.setValueAsNumber(null);
                    break;
                }
                if (!(value instanceof Number)) {
                    throw new IllegalArgumentException(ERR_VALUE_MUST_BE_NUMBER);
                }
                variable.setValueAsNumber(((Number) value).doubleValue());
                variable.setValueAsString(null);
                break;
            }
            case STRING_TYPE: {
                if (value == null) {
                    variable.setValueAsString(null);
                    break;
                }
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException(ERR_VALUE_MUST_BE_STRING);
                }
                variable.setValueAsString((String) value);
                variable.setValueAsNumber(null);
                break;
            }
            default:
                throw new IllegalArgumentException(ERR_TYPE_UNDEFINED + " (" + vr.getType() + ")");
        }

        return variable;
    }

}
