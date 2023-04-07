package org.glebchanskiy.aois3.logic_parser;

import org.glebchanskiy.aois3.logic_parser.ast.*;
import org.glebchanskiy.aois3.logic_parser.util.TableRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FormulasOperations {
    private FormulasOperations() {
    }

    static public String getTruthTable(LogicFormula logicFormula) {
        StringBuilder output = new StringBuilder();

        for (String variableName : logicFormula.getVariables())
            output.append(variableName).append("    ");

        output.append(" <br/>");

        for (TableRow row : logicFormula.getTruthTable()) {
            for (Boolean variable : row.getVariablesValues())
                output.append(variable ? "1" : "0").append("    ");
            output.append(row.isFunctionTakeTrueValue() ? "1" : "0").append("<br/>");
        }

        return output.toString();
    }


    static public String getIndex(LogicFormula logicFormula) {

        String index = Arrays.stream(logicFormula.getTruthTable().getIndex())
                .map(b -> b ? "1" : "0")
                .reduce((a, b) -> a + b)
                .orElse("");

        return "index: " + index + " : " + toDecimal(index);
    }

    static public String getPcnf(LogicFormula logicFormula) {
        List<Boolean[]> values = logicFormula.getTruthTable().getFalses();

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            output.append('(');
            for (int j = 0; j < values.get(i).length; j++) {
                output.append(values.get(i)[j] ? "!x" : "x").append(j);
                if (j != values.get(i).length - 1)
                    output.append("||");
            }
            output.append(')');
            if (i != values.size() - 1)
                output.append("&&");
        }
        return output.toString();
    }

    static public String getPdnf(LogicFormula logicFormula) {
        List<Boolean[]> values = logicFormula.getTruthTable().getTruths();

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            output.append('(');
            for (int j = 0; j < values.get(i).length; j++) {
                output.append(values.get(i)[j] ? "x" : "!x").append(j);
                if (j != values.get(i).length - 1)
                    output.append("&&");
            }
            output.append(')');
            if (i != values.size() - 1)
                output.append("||");
        }
        return output.toString();
    }

    static public String getPdnfBin(LogicFormula logicFormula) {
        List<Boolean[]> pdnf = logicFormula.getTruthTable().getTruths();
        List<String> pdnf_inner_brackets = new ArrayList<>();

        pdnf.forEach(row ->
                pdnf_inner_brackets.add(
                        Arrays.stream(row)
                                .map(b -> b ? "1" : "0")
                                .reduce((accum, bin) -> accum += bin)
                                .orElse("")
                )
        );

        StringBuilder result = new StringBuilder();

        for (String bracket : pdnf_inner_brackets)
            if (result.isEmpty())
                result.append("PDNF: ").append("(").append(bracket).append(")");
            else
                result.append("||").append("(").append(bracket).append(")");

        return result.toString();

    }

    static public String getPdnfDig(LogicFormula logicFormula) {
        List<Boolean[]> pdnf = logicFormula.getTruthTable().getTruths();
        List<Integer> pdnf_inner_brackets_dig = new ArrayList<>();

        pdnf.forEach(row ->
                pdnf_inner_brackets_dig.add(
                        toDecimal(
                                Arrays.stream(row)
                                        .map(b -> b ? "1" : "0")
                                        .reduce((accum, bin) -> accum += bin)
                                        .orElse("")
                        )
                )
        );

        return "PDNF: " + pdnf_inner_brackets_dig;
    }

    static public String getPcnfBin(LogicFormula logicFormula) {
        List<Boolean[]> pcnf = logicFormula.getTruthTable().getFalses();
        List<String> pcnf_inner_brackets = new ArrayList<>();

        pcnf.forEach(row ->
                pcnf_inner_brackets.add(
                        Arrays.stream(row)
                                .map(b -> b ? "1" : "0")
                                .reduce((accum, bin) -> accum += bin)
                                .orElse("")
                )
        );

        StringBuilder result = new StringBuilder();

        for (String bracket : pcnf_inner_brackets)
            if (result.isEmpty())

                result.append("PCNF: ").append("(").append(bracket).append(")");
            else
                result.append("&&").append("(").append(bracket).append(")");

        return result.toString();

    }

    static public String getPcnfDig(LogicFormula logicFormula) {
        List<Boolean[]> pcnf = logicFormula.getTruthTable().getFalses();
        List<Integer> pcnf_inner_brackets_dig = new ArrayList<>();

        pcnf.forEach(row ->
                pcnf_inner_brackets_dig.add(
                        toDecimal(
                                Arrays.stream(row)
                                        .map(b -> b ? "1" : "0")
                                        .reduce((accum, bin) -> accum += bin)
                                        .orElse("")
                        )
                )
        );

        return "PCNF: " + pcnf_inner_brackets_dig;
    }

    static private int toDecimal(String row) {
        int decimal = 0;
        int power = 0;
        for (int i = row.length() - 1; i >= 0; i--) {
            int bit = row.charAt(i) - '0';
            decimal += bit * Math.pow(2, power);
            power++;
        }
        return decimal;
    }

    static public String getTree(LogicFormula logicFormula) {
        StringBuilder output = new StringBuilder();
        treeTraversal(logicFormula.getAstRoot(), output, 0);
        return output.toString();
    }

    static private void treeTraversal(Node node, StringBuilder output, int deep) {
        StringBuilder tabs = new StringBuilder();

        tabs.append(" ".repeat(Math.max(0, deep)));

        if (node instanceof VariableNode)
            output.append(tabs).append(((VariableNode) node).getName()).append("<br/>");
        if (node instanceof UnaryOperationNode) {
            output.append(tabs).append("!").append("<br/>");
            treeTraversal(((UnaryOperationNode) node).getOperand(), output, deep + 3);
        }
        if (node instanceof BinaryOperationNode) {
            output.append(tabs).append("[").append("<br/>");
            treeTraversal(((BinaryOperationNode) node).getLeft(), output, deep + 3);
            if (node instanceof AndNode)
                output.append(tabs).append("and").append("<br/>");
            else if (node instanceof OrNode)
                output.append(tabs).append("or").append("<br/>");
            treeTraversal(((BinaryOperationNode) node).getRight(), output, deep + 3);
            output.append(tabs).append("]").append("<br/>");
        }
    }
}
