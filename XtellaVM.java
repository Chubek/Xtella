import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XtellaVM {
  // Constants for operations
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
  public static final int LOAD_ARG = 31;

  private Stack<Object> stack;
  private List<Object> argumentList;
  private Map<String, Object> globalScope;
  private List<Map<String, Object>> scopes;
  private int instructionPointer;
  private int framePointer;
  private int scopeNumber;

  public XtellaVM() {
    stack = new Stack<>();
    scopes = new ArrayList<>();
    instructionPointer = 0;
    framePointer = 0;
  }

  private static int newFrame() {
    framePointer = 0;
    Map<String, Object> newScope = new HashMap<>();
    scopes.add(newScope);
    return scopeNumber++;
  }

  private static void dropFrame(int rmIdx) {
    scopes.remove(rmIdx);
    scopesNumber--;
  }

  private static Object getInScope(String identifier) {
    return scopes.get(scopeNumber - 1).get(identifier);
  }

  private static void putInScope(String identifier, Object object) {
    scopes.get(scopeNumber - 1).put(identifier, object);
  }

  private void executePushInt() {
    int value = (int) stack.get(instructionPointer);
    stack.push(value);
  }

  private void executePushString() {
    String value = (String) stack.get(instructionPointer);
    stack.push(value);

    framePointer++;
  }

  private void executePushFloat() {
    float value = (float) stack.get(instructionPointer);
    stack.push(value);

    framePointer++;
  }

  private void executeAdd() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for ADD");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 + (int) operand2;
      stack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand1 + (float) operand2;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for ADD");
    }

    framePointer++;
  }

  private void executeSubtract() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SUBTRACT");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 - (int) operand1;
      stack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand2 - (float) operand1;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SUBTRACT");
    }

    framePointer++;
  }

  private void executeMultiply() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for MULTIPLY");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 * (int) operand2;
      stack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand1 * (float) operand2;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for MULTIPLY");
    }

    framePointer++;
  }

  private void executeDivide() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for DIVIDE");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 / (int) operand1;
      stack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand2 / (float) operand1;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for DIVIDE");
    }

    framePointer++;
  }

  private void executeModulo() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for MODULO");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 % (int) operand1;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for MODULO");
    }

    framePointer++;
  }

  private void executeBitwiseAnd() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_AND");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 & (int) operand2;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_AND");
    }

    framePointer++;
  }

  private void executeBitwiseOr() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_OR");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 | (int) operand2;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_OR");
    }

    framePointer++;
  }

  private void executeBitwiseXor() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_XOR");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 ^ (int) operand2;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_XOR");
    }

    framePointer++;
  }

  private void executeShiftLeft() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SHIFT_LEFT");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 << (int) operand1;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SHIFT_LEFT");
    }

    framePointer++;
  }

  private void executeShiftRight() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SHIFT_RIGHT");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 >> (int) operand1;
      stack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SHIFT_RIGHT");
    }

    framePointer++;
  }

  private void executeEqual() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for EQUAL");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

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

    stack.push(result);
    framePointer++;
  }

  private void executeNotEqual() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for NOT_EQUAL");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

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

    stack.push(result);
    framePointer++;
  }

  private void executeLessThan() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for LESS_THAN");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) < ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) < ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for LESS_THAN");
    }

    stack.push(result);
    framePointer++;
  }

  private void executeLessThanOrEqual() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for LESS_THAN_OR_EQUAL");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) <= ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) <= ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for LESS_THAN_OR_EQUAL");
    }

    stack.push(result);
    framePointer++;
  }

  private void executeGreaterThan() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for GREATER_THAN");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) > ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) > ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for GREATER_THAN");
    }

    stack.push(result);
    framePointer++;
  }

  private void executeGreaterThanOrEqual() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for GREATER_THAN_OR_EQUAL");
    }

    Object operand1 = stack.pop();
    Object operand2 = stack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) >= ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) >= ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for GREATER_THAN_OR_EQUAL");
    }

    stack.push(result);
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
    // Get the target instruction index from the stack
    int targetIndex = (int) stack.get(instructionPointer);
    // Set the instruction pointer to the target index
    instructionPointer = targetIndex;
  }

  private void executeJumpIfTrue() {
    // Pop the condition from the stack
    boolean condition = (boolean) stack.pop();
    // Get the target instruction index from the stack
    int targetIndex = (int) stack.get(instructionPointer);

    if (condition) {
      // If the condition is true, set the instruction pointer to the target index
      instructionPointer = targetIndex;
    } else {
      // Otherwise, move to the next instruction
      instructionPointer++;
    }
  }

  private void executeJumpIfFalse() {
    // Pop the condition from the stack
    boolean condition = (boolean) stack.pop();
    // Get the target instruction index from the stack
    int targetIndex = (int) stack.get(instructionPointer);

    if (!condition) {
      // If the condition is false, set the instruction pointer to the target index
      instructionPointer = targetIndex;
    } else {
      // Otherwise, move to the next instruction
      instructionPointer++;
    }
  }

  private void executeLoadVariable() {
    if (stack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for LOAD_VARIABLE");
    }

    Object variableName = stack.pop();
    Object variableValue = getInScope((String) variableName);

    if (variableValue == null) {
      throw new IllegalArgumentException("Variable not found: " + variableName);
    }

    stack.push(variableValue);
    framePointer++;
  }

  private void executeStoreVariable() {
    if (stack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for STORE_VARIABLE");
    }

    Object variableName = stack.pop();
    Object variableValue = stack.pop();

    putInScope((String) variableName, variableValue);
    framePointer++;
  }

  private void executeLoadFunction() {
     if (stack.size() < 1) {
        throw new IllegalStateException("Not enough operands on the stack for LOAD_FUNCTION");
     }

     String functionName = (String) stack.pop();
     int functionArity = (int) stack.pop();

     globalScope.put(functionName, instructionPointer);
     globalScope.put(functionName + "_arity", functionArity);

     instructionPointer++;
     framePointer++;
     
  }

  private void executeCallFunction() {
    if (stack.size() < 1) {
      throw new IllegalStateException("Invalid state for CALL_FUNCTION");
    }
    
    String functionName = (String) stack.pop();
    int argumentNum = (int) stack.pop();

    int functionAddress = globalScope.get(functionName);
    int functionArity = globalScope.get(functionName + "_arity");
 
    if (argumentNum < functionArity) {
       throw new IllegalStateException("Not enough arguments passed to function");
    }

   argumentList.clear();

    for (int i = 0; i <= argumentNum; i++) {
  	argumentList.add(stack.pop());
    }
 
    int scopeId = newFrame();
    executeVM(functionAddress);
    dropFrame(scopeId);
  }

  private void executeLoadArg() {
    if (stack.size() < 1) {
	throw new IllegalStateException("Not enough operands on stack for LOAD_ARG");
    }

    int argIndex = (int) stack.pop();
    stack.push(argumentList.get(argIndex));

    framePointer++;
  }

  public static void executeVM(int stackPointer) { }

}
