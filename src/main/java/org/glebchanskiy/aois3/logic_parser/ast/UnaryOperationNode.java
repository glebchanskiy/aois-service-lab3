package org.glebchanskiy.aois3.logic_parser.ast;

public abstract class UnaryOperationNode extends Node {
    private final Node operand;

    public UnaryOperationNode(Node operand) {
        this.operand = operand;
    }

    public Node getOperand() {
        return operand;
    }
}
