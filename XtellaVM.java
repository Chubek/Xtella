import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XtellaVM {

  public static final int PUSH_INT = 1;
  public static final int PUSH_STRING = 2;
  public static final int PUSH_FLOAT = 3;
  public static final int ADD = 4;
  public static final int SUBTRACT = 5;
  public static final int MULTIPLY = 6;
  public static final int DIVIDE = 7;
  public static final int MODULO = 8;
  public static final int BITWISE_AND = 9;
  public static final int BITWISE_OR = 10;
  public static final int BITWISE_XOR = 11;
  public static final int SHIFT_LEFT = 12;
  public static final int SHIFT_RIGHT = 13;
  public static final int LOGICAL_AND = 14;
  public static final int LOGICAL_OR = 15;
  public static final int LOGICAL_NOT = 16;
  public static final int EQUAL = 17;
  public static final int NOT_EQUAL = 18;
  public static final int LESS_THAN = 19;
  public static final int LESS_THAN_OR_EQUAL = 20;
  public static final int GREATER_THAN = 21;
  public static final int GREATER_THAN_OR_EQUAL = 22;
  public static final int JUMP = 23;
  public static final int JUMP_IF_TRUE = 24;
  public static final int JUMP_IF_FALSE = 25;
  public static final int CALL_FUNCTION = 26;
  public static final int LOAD_FUNCTION = 27;
  public static final int RETURN = 28;
  public static final int LOAD_VARIABLE = 29;
  public static final int STORE_VARIABLE = 30;
  public static final int GET_VARARG = 31;
  public static final int REGEX_MATCH = 32;
  public static final int EXEC_COMMAND = 33;

  private Stack<int> instructionStack;
  private Stack<Object> operandStack;
  private List<Object> varArgs;
  private Map<String, Object> globalScope;
  private List<Map<String, Object>> scopes;
  private int instructionPointer;
  private int stackPointer;
  private int framePointer;
  private int scopeNumber;
  private int lastExitCode;

  public XtellaVM() {
    stack = new Stack<>();
    scopes = new ArrayList<>();
    instructionPointer = 0;
    framePointer = 0;
  }

  private static int newFrame() {
    framePointer = stackPointer;
    Map<String, Object> newScope = new HashMap<>();
    scopes.add(newScope);
    return scopeNumber++;
  }

  private static void dropFrame(int rmIndex) {
    scopes.remove(rmIndex);
    stackPointer += framePointer;
    scopesNumber--;
  }

  private static Object getInScope(String identifier) {
    return scopes.get(scopeNumber).get(identifier);
  }

  private static void putInScope(String identifier, Object object) {
    scopes.get(scopeNumber).put(identifier, object);
  }

  private void executePushInt() {
    int value = (int) operandStack.get(instructionPointer);
    operandStack.push(value);
  }

  private void executePushString() {
    String value = (String) operandStack.get(instructionPointer);
    operandStack.push(value);

    framePointer++;
  }

  private void executePushFloat() {
    float value = (float) operandStack.get(instructionPointer);
    operandStack.push(value);

    framePointer++;
  }

  private void executeAdd() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for ADD");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 + (int) operand2;
      operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand1 + (float) operand2;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for ADD");
    }

    framePointer++;
  }

  private void executeSubtract() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SUBTRACT");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 - (int) operand1;
      operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand2 - (float) operand1;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SUBTRACT");
    }

    framePointer++;
  }

  private void executeMultiply() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for MULTIPLY");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 * (int) operand2;
      operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand1 * (float) operand2;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for MULTIPLY");
    }

    framePointer++;
  }

  private void executeDivide() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for DIVIDE");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 / (int) operand1;
      operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand2 / (float) operand1;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for DIVIDE");
    }

    framePointer++;
  }

  private void executeModulo() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for MODULO");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 % (int) operand1;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for MODULO");
    }

    framePointer++;
  }

  private void executeBitwiseAnd() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_AND");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 & (int) operand2;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_AND");
    }

    framePointer++;
  }

  private void executeBitwiseOr() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_OR");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 | (int) operand2;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_OR");
    }

    framePointer++;
  }

  private void executeBitwiseXor() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_XOR");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 ^ (int) operand2;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_XOR");
    }

    framePointer++;
  }

  private void executeShiftLeft() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SHIFT_LEFT");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 << (int) operand1;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SHIFT_LEFT");
    }

    framePointer++;
  }

  private void executeShiftRight() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SHIFT_RIGHT");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 >> (int) operand1;
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SHIFT_RIGHT");
    }

    framePointer++;
  }

  private void executeEqual() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for EQUAL");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand1) == ((int) operand2);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = Float.compare((float) operand1, (float) operand2) == 0;
    } else if (operand1 instanceof String && operand2 instanceof String) {
      result = operand1.equals(operand2);
    } else {
      throw new IllegalArgumentException("Invalid operand types for EQUAL");
    }

    operandStack.push(result);
    framePointer++;
  }

  private void executeNotEqual() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for NOT_EQUAL");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand1) != ((int) operand2);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = Float.compare((float) operand1, (float) operand2) != 0;
    } else if (operand1 instanceof String && operand2 instanceof String) {
      result = !operand1.equals(operand2);
    } else {
      throw new IllegalArgumentException("Invalid operand types for NOT_EQUAL");
    }

    operandStack.push(result);
    framePointer++;
  }

  private void executeLessThan() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for LESS_THAN");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) < ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) < ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for LESS_THAN");
    }

    operandStack.push(result);
    framePointer++;
  }

  private void executeLessThanOrEqual() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for LESS_THAN_OR_EQUAL");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) <= ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) <= ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for LESS_THAN_OR_EQUAL");
    }

    operandStack.push(result);
    framePointer++;
  }

  private void executeGreaterThan() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for GREATER_THAN");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) > ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) > ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for GREATER_THAN");
    }

    operandStack.push(result);
    framePointer++;
  }

  private void executeGreaterThanOrEqual() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for GREATER_THAN_OR_EQUAL");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) >= ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) >= ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for GREATER_THAN_OR_EQUAL");
    }

    operandStack.push(result);
    framePointer++;
  }

  private void executeReturn() {
    if (framePointer == 0) {
      throw new IllegalStateException("Cannot RETURN from the main frame");
    }

    framePointer--;
    instructionPointer = 0;
  }

  private void executeJump() {

    int targetIndex = (int) operandStack.get(instructionPointer);

    instructionPointer = targetIndex;
  }

  private void executeJumpIfTrue() {

    boolean condition = (boolean) operandStack.pop();

    int targetIndex = (int) operandStack.get(instructionPointer);

    if (condition) {

      instructionPointer = targetIndex;
    } else {

      instructionPointer++;
    }
  }

  private void executeJumpIfFalse() {

    boolean condition = (boolean) operandStack.pop();

    int targetIndex = (int) operandStack.get(instructionPointer);

    if (!condition) {

      instructionPointer = targetIndex;
    } else {

      instructionPointer++;
    }
  }

  private void executeLoadVariable() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for LOAD_VARIABLE");
    }

    Object variableName = operandStack.pop();
    Object variableValue = getInScope((String) variableName);

    if (variableValue == null) {
      throw new IllegalArgumentException("Variable not found: " + variableName);
    }

    operandStack.push(variableValue);
    framePointer++;
  }

  private void executeStoreVariable() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for STORE_VARIABLE");
    }

    Object variableName = operandStack.pop();
    Object variableValue = operandStack.pop();

    putInScope((String) variableName, variableValue);
    framePointer++;
  }

  private void executeLoadFunction() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for LOAD_FUNCTION");
    }

    String functionName = (String) operandStack.pop();
    int functionArity = (int) operandStack.pop();

    globalScope.put(functionName, instructionPointer);
    globalScope.put(functionName + "_arity", functionArity);

    instructionPointer++;
    framePointer++;
  }

  private void executeCallFunction() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Invalid state for CALL_FUNCTION");
    }

    String functionName = (String) operandStack.pop();

    int functionAddress = globalScope.get(functionName);
    List<String> functionParams = globalScope.get(functionName + "_params");

    int scopeId = newFrame();

    for (int i = 0; i <= functionParams.size(); i++) {
      String argumentId = functionParams[i];
      Object argument = operandStack.pop();

      operandStack.push(argument);
      operandStack.push(argumentId);

      executeStoreVariable();
    }

    int varArgNum = (int) operandStack.pop();

    if (varArgNum > 0) {
      varArgs.clear();
      while (--varArgNum) {
        varArgs.add(operandstack.pop());
      }
    }

    executeVM(functionAddress);
    dropFrame(scopeId);
  }

  private void executeGetVarArg() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands for GET_VARARG");
    }

    int varArgIndex = (int) operandStack.pop();

    if (varArgIndex >= varArgs.size()) {
      throw new IllegalStateException("Number of requested variable argument is illegal");
    }

    operandStack.push(varArg.get(varArgIndex));

    framePointer++;
  }

  private void executeRegexMatch() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for REGEX_MATCH");
    }

    Object operand1 = operandStack.pop();
    Object operand2 = operandStack.pop();

    if (operand1 instanceof String && operand2 instanceof String) {
      String input = (String) operand1;
      String pattern = (String) operand2;

      boolean result = input.matches(pattern);
      operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for REGEX_MATCH");
    }

    framePointer++;
  }

  private void executeExecCommand() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for EXEC_COMMAND");
    }

    Object command = operandStack.pop();

    if (command instanceof String) {
      String commandString = (String) command;

      try {
        Process process = Runtime.getRuntime().exec(commandString);
        lastExitCode = process.waitFor();
        operandStack.push(lastExitCode);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException("Error executing command: " + commandString, e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for EXEC_COMMAND");
    }

    framePointer++;
  }

  public static void executeVM(int stackPointer) {}
}
