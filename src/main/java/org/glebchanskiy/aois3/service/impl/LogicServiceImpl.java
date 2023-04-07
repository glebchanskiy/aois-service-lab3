package org.glebchanskiy.aois3.service.impl;

import org.glebchanskiy.aois3.logic_parser.FormulasOperations;
import org.glebchanskiy.aois3.logic_parser.LogicFormula;
import org.glebchanskiy.aois3.logic_parser.LogicParser;
import org.glebchanskiy.aois3.model.Message;
import org.glebchanskiy.aois3.service.LogicService;
import org.springframework.stereotype.Service;

@Service
public class LogicServiceImpl implements LogicService {

    @Override
    public Message getInfo(String expression) {
        LogicFormula formula = LogicParser.parse(expression);
        String output = "PCNF: " + FormulasOperations.getPcnf(formula) + "<br/>" +
                FormulasOperations.getPcnfBin(formula) + "<br/>" +
                FormulasOperations.getPcnfDig(formula) + "<br/>" +
                "PDNF: " + FormulasOperations.getPdnf(formula) + "<br>" +
                FormulasOperations.getPdnfBin(formula) + "<br>" +
                FormulasOperations.getPdnfDig(formula) + "<br>" +
                "index: " + FormulasOperations.getIndex(formula) + "<br>" +
                "<tt>" + FormulasOperations.getTruthTable(formula) + "<tt/>";
        return new Message(output, "logic", "view");
    }
}
