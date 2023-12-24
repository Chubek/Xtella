import java.util.List;


abstract class AstNode {
}


class ProgramNode extends AstNode {
    private final List<AstNode> statements;

    public ProgramNode(List<AstNode> statements) {
        this.statements = statements;
    }

    public List<AstNode> getStatements() {
        return statements;
    }
}


class MacroCallNode extends AstNode {
    private final String identifier;
    private final List<AstNode> arguments;

    public MacroCallNode(String identifier, List<AstNode> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<AstNode> getArguments() {
        return arguments;
    }
}


class MacroDefineNode extends AstNode {
    private final String identifier;
    private final List<String> macroParams;
    private final List<AstNode> statements;

    public MacroDefineNode(String identifier, List<String> macroParams, List<AstNode> statements) {
        this.identifier = identifier;
        this.macroParams = macroParams;
        this.statements = statements;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getMacroParams() {
        return macroParams;
    }

    public List<AstNode> getStatements() {
        return statements;
    }
}


class AssignStmtNode extends AstNode {
    private final String identifier;
    private final AstNode expression;

    public AssignStmtNode(String identifier, AstNode expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public AstNode getExpression() {
        return expression;
    }
}


class ControlFlowStmtNode extends AstNode {
    private final AstNode condition;
    private final AstNode trueBlock;
    private final AstNode falseBlock;

    public ControlFlowStmtNode(AstNode condition, AstNode trueBlock, AstNode falseBlock) {
        this.condition = condition;
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getTrueBlock() {
        return trueBlock;
    }

    public AstNode getFalseBlock() {
        return falseBlock;
    }
}


class FunctionStmtNode extends AstNode {
    private final String identifier;
    private final List<String> parameters;
    private final AstNode block;

    public FunctionStmtNode(String identifier, List<String> parameters, AstNode block) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.block = block;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public AstNode getBlock() {
        return block;
    }
}


class IdentifierNode extends AstNode {
    private final String identifier;

    public IdentifierNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}


class NumberNode extends AstNode {
    private final String value;

    public NumberNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}


class BinaryExprNode extends AstNode {
    private final String operator;
    private final AstNode leftOperand;
    private final AstNode rightOperand;

    public BinaryExprNode(String operator, AstNode leftOperand, AstNode rightOperand) {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public String getOperator() {
        return operator;
    }

    public AstNode getLeftOperand() {
        return leftOperand;
    }

    public AstNode getRigtOperand() {
        return rightOperand;
    }
}

import java.util.List;


class IfStmtNode extends AstNode {
    private final AstNode condition;
    private final List<AstNode> ifBlock;
    private final List<AstNode> elseBlock;

    public IfStmtNode(AstNode condition, List<AstNode> ifBlock, List<AstNode> elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public AstNode getCondition() {
        return condition;
    }

    public List<AstNode> getIfBlock() {
        return ifBlock;
    }

    public List<AstNode> getElseBlock() {
        return elseBlock;
    }
}


class ElifStmtNode extends AstNode {
    private final AstNode condition;
    private final List<AstNode> block;

    public ElifStmtNode(AstNode condition, List<AstNode> block) {
        this.condition = condition;
        this.block = block;
    }

    public AstNode getCondition() {
        return condition;
    }

    public List<AstNode> getBlock() {
        return block;
    }
}


class MatchStmtNode extends AstNode {
    private final AstNode expression;
    private final List<MatchCaseNode> cases;

    public MatchStmtNode(AstNode expression, List<MatchCaseNode> cases) {
        this.expression = expression;
        this.cases = cases;
    }

    public AstNode getExpression() {
        return expression;
    }

    public List<MatchCaseNode> getCases() {
        return cases;
    }
}


class MatchCaseNode extends AstNode {
    private final AstNode pattern;
    private final List<AstNode> block;

    public MatchCaseNode(AstNode pattern, List<AstNode> block) {
        this.pattern = pattern;
        this.block = block;
    }

    public AstNode getPattern() {
        return pattern;
    }

    public List<AstNode> getBlock() {
        return block;
    }
}


class VariantStmtNode extends AstNode {
    private final String typeIdentifier;
    private final List<VariantNode> variants;

    public VariantStmtNode(String typeIdentifier, List<VariantNode> variants) {
        this.typeIdentifier = typeIdentifier;
        this.variants = variants;
    }

    public String getTypeIdentifier() {
        return typeIdentifier;
    }

    public List<VariantNode> getVariants() {
        return variants;
    }
}


class VariantNode extends AstNode {
    private final String identifier;
    private final String ofType;

    public VariantNode(String identifier, String ofType) {
        this.identifier = identifier;
        this.ofType = ofType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOfType() {
        return ofType;
    }
}

import java.util.List;


class ForStmtNode extends AstNode {
    private final AstNode loopVariable;
    private final AstNode iterable;
    private final List<AstNode> block;

    public ForStmtNode(AstNode loopVariable, AstNode iterable, List<AstNode> block) {
        this.loopVariable = loopVariable;
        this.iterable = iterable;
        this.block = block;
    }

    public AstNode getLoopVariable() {
        return loopVariable;
    }

    public AstNode getIterable() {
        return iterable;
    }

    public List<AstNode> getBlock() {
        return block;
    }
}


class UnlessStmtNode extends AstNode {
    private final AstNode condition;
    private final List<AstNode> block;

    public UnlessStmtNode(AstNode condition, List<AstNode> block) {
        this.condition = condition;
        this.block = block;
    }

    public AstNode getCondition() {
        return condition;
    }

    public List<AstNode> getBlock() {
        return block;
    }
}


class WhileStmtNode extends AstNode {
    private final AstNode condition;
    private final List<AstNode> block;

    public WhileStmtNode(AstNode condition, List<AstNode> block) {
        this.condition = condition;
        this.block = block;
    }

    public AstNode getCondition() {
        return condition;
    }

    public List<AstNode> getBlock() {
        return block;
    }
}


class DoInStmtNode extends AstNode {
    private final AstNode expression;
    private final AstNode inExpression;
    private final List<AstNode> block;

    public DoInStmtNode(AstNode expression, AstNode inExpression, List<AstNode> block) {
        this.expression = expression;
        this.inExpression = inExpression;
        this.block = block;
    }

    public AstNode getExpression() {
        return expression;
    }

    public AstNode getInExpression() {
        return inExpression;
    }

    public List<AstNode> getBlock() {
        return block;
    }
}
g
class ReturnStmtNode extends AstNode {
    private final List<AstNode> expressions;

    public ReturnStmtNode(List<AstNode> expressions) {
        this.expressions = expressions;
    }

    public List<AstNode> getExpressions() {
        return expressions;
    }
}

g
class JumpStmtNode extends AstNode {
    private final AstNode expression;

    public JumpStmtNode(AstNode expression) {
        this.expression = expression;
    }

    public AstNode getExpression() {
        return expression;
    }
}

class FunctionStmtNode extends AstNode {
    private final String identifier;
    private final List<String> parameters;
    private final BlockNode block;

    public FunctionStmtNode(String identifier, List<String> parameters, BlockNode block) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.block = block;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public BlockNode getBlock() {
        return block;
    }
}

class ExpressionNode extends AstNode {
    private final AstNode logicalOrExpr;

    public ExpressionNode(AstNode logicalOrExpr) {
        this.logicalOrExpr = logicalOrExpr;
    }

    public AstNode getLogicalOrExpr() {
        return logicalOrExpr;
    }
}

