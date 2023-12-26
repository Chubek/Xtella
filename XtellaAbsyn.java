import java.util.HashMap;
import java.util.List;

abstract class AbsynNode {}

class ProgramNode extends AbsynNode {
  private StatementListNode statementList;

  public ProgramNode(StatementListNode statementList) {
    this.statementList = statementList;
  }
}

class StatementListNode extends AbsynNode {
  private List<StatementNode> statements;

  public StatementListNode(List<StatementNode> statements) {
    this.statements = statements;
  }
}

abstract class StatementNode extends AbsynNode {}

class AssignStmtNode extends StatementNode {
  private String identifier;
  private ExpressionNode expression;

  public AssignStmtNode(String identifier, ExpressionNode expression) {
    this.identifier = identifier;
    this.expression = expression;
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
}

class FunctionStmtNode extends StatementNode {
  private String functionName;
  private List<ExpressionNode> arguments;

  public FunctionStmtNode(String functionName, List<ExpressionNode> arguments) {
    this.functionName = functionName;
    this.arguments = arguments;
  }
}

class FileStmtNode extends StatementNode {
  private String fileName;
  private RWMode rwMode;
  private FileHandleNode fileHandle;

  public FileStmtNode(String fileName, RWMode rwMode, FileHandleNode fileHandle) {
    this.fileName = fileName;
    this.rwMode = rwMode;
    this.fileHandle = fileHandle;
  }
}

class IOStmtNode extends StatementNode {
  private List<ExpressionNode> arguments;
  private RedirectionNode redirection;

  public IOStmtNode(List<ExpressionNode> arguments, RedirectionNode redirection) {
    this.arguments = arguments;
    this.redirection = redirection;
  }
}

class ExecStmtNode extends StatementNode {
  private String command;

  public ExecStmtNode(String command) {
    this.command = command;
  }
}

class OpenStmtNode extends StatementNode {
  private String fileName;
  private RWMode rwMode;
  private FileHandleNode fileHandle;

  public OpenStmtNode(String fileName, RWMode rwMode, FileHandleNode fileHandle) {
    this.fileName = fileName;
    this.rwMode = rwMode;
    this.fileHandle = fileHandle;
  }
}

class CloseStmtNode extends StatementNode {
  private FileHandleNode fileHandle;

  public CloseStmtNode(FileHandleNode fileHandle) {
    this.fileHandle = fileHandle;
  }
}

class RWStmtNode extends StatementNode {

  enum RWMode {
    READING,
    WRITING,
    APPENDING,
    READ_AND_WRITE
  }

  private RWMode rwMode;
  private ExpressionNode expression;
  private FileHandleNode fileHandle;

  public RWStmtNode(RWMode rwMode, ExpressionNode expression, FileHandleNode fileHandle) {
    this.rwMode = rwMode;
    this.expression = expression;
    this.fileHandle = fileHandle;
  }
}

class RedirectionNode extends Node {
  enum RedirectionType {
    GT,
    DOUBLE_GT,
    LT,
    DOUBLE_LT
  }

  private RedirectionType redirectionType;
  private ExpressionNode expression;

  public RedirectionNode(RedirectionType redirectionType, ExpressionNode expression) {
    this.redirectionType = redirectionType;
    this.expression = expression;
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

class BinaryExprNode extends ExpressionNode {
  private ExpressionNode left;
  private ExpressionNode right;
  private String operator;

  public BinaryExprNode(ExpressionNode left, ExpressionNode right, String operator) {
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

  public IdentifierNode(String identifier) {
    this.identifier = identifier;
  }
}

class LiteralNode extends ExpressionNode {
  private Object value;

  public LiteralNode(Object value) {
    this.value = value;
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

class CompoundExprNode extends ExpressionNode {
  private ExpressionNode left;
  private ExpressionNode right;
  private String operator;

  public CompoundExprNode(ExpressionNode left, ExpressionNode right, String operator) {
    this.left = left;
    this.right = right;
    this.operator = operator;
  }
}

public class PrimaryExprNode extends ExpressionNode {
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
  private ConstValueNode constValue;
  private FunctionCallNode functionCall;
  private TernaryExprNode ternaryExpr;
  private LambdaExprNode lambdaExpr;
  private ArgumentBackRefNode argumentBackRef;
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

  public PrimaryExprNode(ConstValueNode constValue) {
    this.nodeType = PrimaryExprNodeType.CONST_VALUE;
    this.constValue = constValue;
  }

  public PrimaryExprNode(FunctionCallNode functionCall) {
    this.nodeType = PrimaryExprNodeType.FUNCTION_CALL;
    this.functionCall = functionCall;
  }

  public PrimaryExprNode(TernaryExprNode ternaryExpr) {
    this.nodeType = PrimaryExprNodeType.TERNARY_EXPR;
    this.ternaryExpr = ternaryExpr;
  }

  public PrimaryExprNode(LambdaExprNode lambdaExpr) {
    this.nodeType = PrimaryExprNodeType.LAMBDA_EXPR;
    this.lambdaExpr = lambdaExpr;
  }

  public PrimaryExprNode(ArgumentBackRefNode argumentBackRef) {
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

  public ConstValueNode getConstValue() {
    return constValue;
  }

  public FunctionCallNode getFunctionCall() {
    return functionCall;
  }

  public TernaryExprNode getTernaryExpr() {
    return ternaryExpr;
  }

  public LambdaExprNode getLambdaExpr() {
    return lambdaExpr;
  }

  public ArgumentBackRefNode getArgumentBackRef() {
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

public class RegexExprNode extends ExpressionNode {
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

public class ConstValueNode extends ExpressionNode {
  public enum ConstValueType {
    ARRAY,
    HASHMAP,
    EXEC_COMMAND,
    NUMBER,
    STRING
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

public class HashMapNode extends ConstValueNode {
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

public class NumberNode extends ConstValueNode {
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

public class StringConstNode extends ConstValueNode {
  private String value;

  public StringConstNode(String value) {
    super(ConstValueType.STRING);
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

public class RegexConstNode extends ConstValueNode {
  private String pattern;

  public RegexConstNode(String pattern) {
    super(ConstValueType.REGEX);
    this.pattern = pattern;
  }

  public String getPattern() {
    return pattern;
  }
}
