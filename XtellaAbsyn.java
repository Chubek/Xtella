import java.util.HashMap;
import java.util.List;

interface Visitor {
  void visit(ProgramNode node);

  void visit(StatementListNode node);

  void visit(AssignStmtNode node);

  void visit(ControlFlowStmtNode node);

  void visit(ExecStmtNode node);

  void visit(RWStmtNode node);

  void visit(CloseStmtNode node);

  void visit(OpenStmtNode node);

  void visit(IfStmtNode node);

  void visit(WhileStmtNode node);

  void visit(ReturnStmtNode node);

  void visit(ForStmtNode node);

  void visit(MatchStmtNode node);

  void visit(MatchCaseNode node);

  void visit(BlockNode node);

  void visit(ExpressionNode node);

  void visit(CompoundExprNode node);

  void visit(UnaryExprNode node);

  void visit(IdentifierNode node);

  void visit(LiteralNode node);

  void visit(RWMode rwMode);

  void visit(ConditionNode node);

  void visit(ArrayNode node);

  void visit(DelimitedStringNode node);

  void visit(RegexConstNode node);
}

abstract class AbsynNode {
  void accept(Visitor visitor) {
    visitor.visit(this);
  }

  abstract void interpretSelf(XtellaVM vm);

  abstract int getOpcode();
}

class AbsynListNode extends AbsynNode {
  private List<AbsynNode> nodes;

  public void addToList(AbsynNode node) {
    this.nodes.add(node);
  }

  public AbsynListNode(AbsynNode initNode) {
    this.nodes = new List.of(initNode);
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    for (AbsynNode elementNode : this.nodes) {
      elementNode.interpretSelf(vm);
    }
  }
}

class ProgramNode extends AbsynNode {
  private StatementListNode statementList;

  public ProgramNode(StatementListNode statementList) {
    this.statementList = statementList;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    this.statementList.interpretSelf(vm);
  }
}

class StatementListNode extends AbsynNode {
  private List<StatementNode> statements;

  public StatementListNode(List<StatementNode> statements) {
    this.statements = statements;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    for (StatementNode statement : this.statements) {
      statement.interpretSelf(vm);
    }
  }
}

abstract class StatementNode extends AbsynNode {}

class AssignStmtNode extends StatementNode {
  private IdentifierNode identifier;
  private ExpressionNode expression;

  public AssignStmtNode(IdentifierNode identifier, ExpressionNode expression) {
    this.identifier = identifier;
    this.expression = expression;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    this.expression.interpretSelf(vm);
    vm.addInstruction(identifier.getOpcode());
    vm.addOperand(identifier.getValue());
  }
}

class ControlFlowStmtNode extends StatementNode {
  private ConditionNode condition;
  private BlockNode ifBlock;
  private BlockNode elseBlock;

  public ControlFlowStmtNode(ConditionNode condition, BlockNode ifBlock, BlockNode elseBlock) {
    this.condition = condition;
    this.ifBlock = ifBlock;
    this.elseBlock = elseBlock;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    this.condition.interpretSelf(vm);
    this.ifBlock.interpretSelf(vm);
    this.elseBlock.interpretSelf(vm);
  }
}

class ExecStmtNode extends StatementNode {
  private String command;

  public ExecStmtNode(String command) {
    this.command = command;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    vm.addInstruction(XtellaVM.EXEC_COMMAND);
    vm.addOperand(this.command);
  }
}

enum RWMode {
  READING,
  WRITING,
  APPENDING,
  READ_AND_WRITE,
}

class RWStmtNode extends StatementNode {
  private RWMode rwMode;
  private ExpressionNode expression;
  private IdentifierNode fileHandle;

  public RWStmtNode(RWMode rwMode, ExpressionNode expression, String fileHandle) {
    this.rwMode = rwMode;
    this.expression = expression;
    this.fileHandle = fileHandle;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    switch (this.rwMode) {
      case RWMode.READING:
        vm.addInstruction(XtellaVM.READ_FILE);
        break;
      case RWMode.WRITING:
        vm.addInstruction(XtellaVM.WRITE_FILE);
        break;
      case RWMode.APPENDING:
        vm.addInstruction(XtellaVM.APPEND_FILE);
        break;
      default:
        break;
    }

    this.expression.interpretSelf(vm);
    vm.addOperand(this.fileHandle.getValue());
  }
}

class CloseStmtNode extends StatementNode {
  private String fileHandle;

  public CloseStmtNode(String fileHandle) {
    this.fileHandle = fileHandle;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    vm.addInstruction(XtellaVM.CLOSE_FILE);
    vm.addOperand(this.fileHandle);
  }
}

class OpenStmtNode extends StatementNode {
  private String fileName;
  private RWMode rwMode;
  private String fileHandle;

  public OpenStmtNode(String fileName, RWMode rwMode, String fileHandle) {
    this.fileName = fileName;
    this.rwMode = rwMode;
    this.fileHandle = fileHandle;
  }

  @override
  public void interpretSelf(XtellaVM vm) {
    switch (this.rwMode) {
      case RWMode.READING:
        vm.addInstruction(XtellaVM.OPEN_FILE_FOR_READING);
        break;
      case RWMode.WRITING:
        vm.addInstruction(XtellaVM.OPEN_FILE_FOR_WRITING);
        break;
      case RWMode.APPENDING:
        vm.addInstruction(XtellaVM.OPEN_FILE_FOR_WRITING);
        break;
      case RWMode.READ_AND_WRITE:
        vm.addInstruction(XtellaVM.OPEN_FILE_FOR_READING_AND_WRITING);
        break;
      default:
        break;
    }

    vm.addOperand(this.fileName);
    vm.addOperand(this.fileHandle.getValue());
  }
}

class IfStmtNode extends ControlFlowStmtNode {
  private ConditionNode condition;
  private BlockNode ifBlock;
  private BlockNode elseBlock;

  public IfStmtNode(ConditionNode condition, BlockNode ifBlock, BlockNode elseBlock) {
    this.condition = condition;
    this.ifBlock = ifBlock;
    this.elseBlock = elseBlock;
  }
}

class WhileStmtNode extends ControlFlowStmtNode {
  private ConditionNode condition;
  private BlockNode whileBlock;

  public WhileStmtNode(ConditionNode condition, BlockNode whileBlock) {
    this.condition = condition;
    this.whileBlock = whileBlock;
  }
}

class ReturnStmtNode extends StatementNode {
  private ExpressionNode expression;

  public ReturnStmtNode(ExpressionNode expression) {
    this.expression = expression;
  }
}

class ForStmtNode extends ControlFlowStmtNode {
  private ExpressionNode iterable;
  private ExpressionNode loopVariable;
  private BlockNode forBlock;

  public ForStmtNode(ExpressionNode iterable, ExpressionNode loopVariable, BlockNode forBlock) {
    this.iterable = iterable;
    this.loopVariable = loopVariable;
    this.forBlock = forBlock;
  }
}

class MatchStmtNode extends ControlFlowStmtNode {
  private ExpressionNode expression;
  private List<MatchCaseNode> matchCases;

  public MatchStmtNode(ExpressionNode expression, List<MatchCaseNode> matchCases) {
    this.expression = expression;
    this.matchCases = matchCases;
  }
}

class MatchCaseNode extends AbsynNode {
  private ExpressionNode pattern;
  private BlockNode block;

  public MatchCaseNode(ExpressionNode pattern, BlockNode block) {
    this.pattern = pattern;
    this.block = block;
  }
}

class BlockNode extends AbsynNode {
  private StatementListNode statementList;

  public BlockNode(StatementListNode statementList) {
    this.statementList = statementList;
  }
}

abstract class ExpressionNode extends AbsynNode {}

class CompoundExprNode extends ExpressionNode {
  private ExpressionNode left;
  private ExpressionNode right;
  private String operator;

  public CompoundExprNode(ExpressionNode left, ExpressonNode right, String operator) {
    this.left = left;
    this.right = right;
    this.operator = operator;
  }
}

class UnaryExprNode extends ExpressionNode {
  private ExpressionNode operand;
  private String operator;

  public UnaryExprNode(ExpressionNode operand, String operator) {
    this.operand = operand;
    this.operator = operator;
  }
}

class IdentifierNode extends ExpressionNode {
  private String identifier;
  private int opcode;

  public IdentifierNode(String identifier, int opcode) {
    this.identifier = identifier;
    this.opcode = opcode;
  }

  public String getValue() {
    return this.identifier;
  }

  @override
  public int getOpcode() {
    return this.opcode;
  }
}

class LiteralNode extends ExpressionNode {
  private Object value;

  public LiteralNode(Object value) {
    this.value = value;
  }
}

enum RWMode {
  READING,
  WRITING,
  APPENDING,
  READ_AND_WRITE
}

class ConditionNode extends ExpressionNode {
  private ExpressionNode expression;

  public ConditionNode(ExpressionNode expression) {
    this.expression = expression;
  }
}

enum Operator {
  OR("OR"),
  AND("AND"),
  BIT_AND("BIT_AND"),
  BIT_OR("BIT_OR"),
  BIT_XOR("BIT_XOR"),
  EQ("EQ"),
  NEQ("NEQ"),
  LT("LT"),
  LTE("LTE"),
  GT("GT"),
  GTE("GTE"),
  PLUS("PLUS"),
  MINUS("MINUS"),
  MULTIPLY("MULTIPLY"),
  DIVIDE("DIVIDE"),
  MODULO("MODULO"),
  POW("POW");

  private final String symbol;

  Operator(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }
}

class PrimaryExprNode extends ExpressionNode {
  public enum PrimaryExprNodeType {
    IDENTIFIER,
    IDENTIFIER_WITH_INDEX,
    CONST_VALUE,
    FUNCTION_CALL,
    TERNARY_EXPR,
    LAMBDA_EXPR,
    ARGUMENT_BACK_REF
  }

  private String identifier;
  private ExpressionNode arrayIndex;
  private Object constValue;
  private FunctionCallNode functionCall;
  private Object ternaryExpr;
  private Object lambdaExpr;
  private Object argumentBackRef;
  private PrimaryExprNodeType nodeType;

  public PrimaryExprNode(String identifier) {
    this.nodeType = PrimaryExprNodeType.IDENTIFIER;
    this.identifier = identifier;
  }

  public PrimaryExprNode(String identifier, ExpressionNode arrayIndex) {
    this.nodeType = PrimaryExprNodeType.IDENTIFIER_WITH_INDEX;
    this.identifier = identifier;
    this.arrayIndex = arrayIndex;
  }

  public PrimaryExprNode(Object constValue) {
    this.nodeType = PrimaryExprNodeType.CONST_VALUE;
    this.constValue = constValue;
  }

  public PrimaryExprNode(FunctionCallNode functionCall) {
    this.nodeType = PrimaryExprNodeType.FUNCTION_CALL;
    this.functionCall = functionCall;
  }

  public PrimaryExprNode(Object ternaryExpr) {
    this.nodeType = PrimaryExprNodeType.TERNARY_EXPR;
    this.ternaryExpr = ternaryExpr;
  }

  public PrimaryExprNode(Object lambdaExpr) {
    this.nodeType = PrimaryExprNodeType.LAMBDA_EXPR;
    this.lambdaExpr = lambdaExpr;
  }

  public PrimaryExprNode(Object argumentBackRef) {
    this.nodeType = PrimaryExprNodeType.ARGUMENT_BACK_REF;
    this.argumentBackRef = argumentBackRef;
  }

  public PrimaryExprNodeType getNodeType() {
    return nodeType;
  }

  public String getIdentifier() {
    return identifier;
  }

  public ExpressionNode getArrayIndex() {
    return arrayIndex;
  }

  public Object getConstValue() {
    return constValue;
  }

  public FunctionCallNode getFunctionCall() {
    return functionCall;
  }

  public Object getTernaryExpr() {
    return ternaryExpr;
  }

  public Object getLambdaExpr() {
    return lambdaExpr;
  }

  public Object getArgumentBackRef() {
    return argumentBackRef;
  }
}

class FunctionCallNode extends PrimaryExprNode {
  private String identifier;
  private List<ExpressionNode> argumentList;

  public FunctionCallNode(String identifier, List<ExpressionNode> argumentList) {
    this.identifier = identifier;
    this.argumentList = argumentList;
  }
}

class RegexExprNode extends ExpressionNode {
  public enum RegexMatchType {
    MATCHES,
    NOT_MATCHES
  }

  private ExpressionNode expression;
  private String regex;
  private RegexMatchType matchType;

  public RegexExprNode(ExpressionNode expression, String regex, RegexMatchType matchType) {
    this.expression = expression;
    this.regex = regex;
    this.matchType = matchType;
  }
}

class ConstValueNode extends ExpressionNode {
  public enum ConstValueType {
    ARRAY,
    HASHMAP,
    EXEC_COMMAND,
    NUMBER,
    STRING,
    FSTRING,
    REGEX
  }

  private ConstValueType constType;

  public ConstValueNode(ConstValueType constType) {
    this.constType = constType;
  }
}

class ArrayNode extends ConstValueNode {
  private List<ExpressionNode> elements;

  public ArrayNode(List<ExpressionNode> elements) {
    this.elements = elements;
  }
}

class HashMapNode extends ConstValueNode {
  private HashMap<ExpressionNode, ExpressionNode> keyValues;

  public HashMapNode() {
    super(ConstValueType.HASHMAP);
    this.keyValues = new HashMap<>();
  }

  public void addKeyValue(ExpressionNode key, ExpressionNode value) {
    keyValues.put(key, value);
  }

  public HashMap<ExpressionNode, ExpressionNode> getKeyValues() {
    return keyValues;
  }
}

class NumberNode extends ConstValueNode {
  public enum NumberType {
    INTEGER,
    FLOAT,
    SCIENTIFIC
  }

  private NumberType numberType;
  private String value;

  public NumberNode(String value, NumberType numberType) {
    super(ConstValueType.NUMBER);
    this.value = value;
    this.numberType = numberType;
  }

  public String getValue() {
    return value;
  }

  public NumberType getNumberType() {
    return numberType;
  }
}

class StringConstNode extends ConstValueNode {
  private String value;

  public StringConstNode(String value) {
    super(ConstValueType.STRING);
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

class FStringConstNode extends ConstValueNode {
  private AbsynListNode value;

  public FStringConstNode(AbsynListNode value) {
    super(ConstValueType.FSTRING);
    this.value = value;
  }

  public AbsynListNode getValue() {
    return value;
  }
}

class RegexConstNode extends ConstValueNode {
  private String pattern;

  public RegexConstNode(String pattern) {
    super(ConstValueType.REGEX);
    this.pattern = pattern;
  }

  public String getPattern() {
    return pattern;
  }
}

enum Delimiter {
  SLASH,
  ANGLE,
  PAREN,
  CURLY,
  PERCENT,
}

class DelimitedStringNode extends ConstValue {
  private Delimiter delimLeft;
  private Delimiter delimRight;
  private AbsynListNode contents;

  public DelimitedString(Delimiter delimLeft, Delimiter delimRight, AbsynListNode contents) {
    this.delimLeft = delimLeft;
    this.delimRight = delimRight;
    this.contents = contents;
  }

  public boolean delimitersMatch() {
    return this.delimLeft == this.delimRight;
  }
}

class InterpreterVisitor implements Visitor {
  public void visit(ProgramNode node) {
    node.getStatementList().accept(this);
  }

  public void visit(StatementListNode node) {
    for (StatementNode statement : node.getStatements()) {
      statement.accept(this);
    }
  }
}
