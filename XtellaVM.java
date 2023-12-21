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
  public static final int RETURN = 27;
  public static final int LOAD_VARIABLE = 28;
  public static final int STORE_VARIABLE = 29;

  private Stack<Object> stack; // VM stack
  private List<Map<String, Object>> scopes; // VM lexical scopes
  private int instruction_pointer;
  private int frame_pointer;
  private int scope_number;

  public XtellaVM() {
    stack = new Stack<>();
    scopes = new ArrayList<>();
    instruction_pointer = 0;
    frame_pointer = 0;
  }

  public void newFrame() {
    frame_pointer = 0;
    scope_number++;
    Map<String, Object> newScope = new HashMap<>();
    scopes.add(newScope);
  }

  public Object getInScope(String identifier) {
    return scopes.get(scope_number - 1).get(identifier);
  }

  public void putInScope(String identifier, Object object) {
    scopes.get(scope_number - 1).put(identifier, object);
  }

  private void executePushInt() {
    int value = (int) stack.get(instruction_pointer);
    stack.push(value);
  }

  private void executePushString() {
    String value = (String) stack.get(instruction_pointer);
    stack.push(value);

    frame_pointer++;
  }

  private void executePushFloat() {
    float value = (float) stack.get(instruction_pointer);
    stack.push(value);

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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

    frame_pointer++;
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
    frame_pointer++;
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
    frame_pointer++;
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
    frame_pointer++;
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
    frame_pointer++;
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
    frame_pointer++;
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
    frame_pointer++;
  }

  private void executeReturn() {
    if (frame_pointer == 0) {
      throw new IllegalStateException("Cannot RETURN from the main frame");
    }

    frame_pointer--;
    instruction_pointer = 0;
  }

  private void executeJump() {
    // Get the target instruction index from the stack
    int targetIndex = (int) stack.get(instruction_pointer);
    // Set the instruction pointer to the target index
    instruction_pointer = targetIndex;
  }

  private void executeJumpIfTrue() {
    // Pop the condition from the stack
    boolean condition = (boolean) stack.pop();
    // Get the target instruction index from the stack
    int targetIndex = (int) stack.get(instruction_pointer);

    if (condition) {
      // If the condition is true, set the instruction pointer to the target index
      instruction_pointer = targetIndex;
    } else {
      // Otherwise, move to the next instruction
      instruction_pointer++;
    }
  }

  private void executeJumpIfFalse() {
    // Pop the condition from the stack
    boolean condition = (boolean) stack.pop();
    // Get the target instruction index from the stack
    int targetIndex = (int) stack.get(instruction_pointer);

    if (!condition) {
      // If the condition is false, set the instruction pointer to the target index
      instruction_pointer = targetIndex;
    } else {
      // Otherwise, move to the next instruction
      instruction_pointer++;
    }
  }

  public void executeNextInstruction() {
    int instruction = (int) stack.get(instruction_pointer);
    instruction_pointer++;

    switch (instruction) {
      case PUSH_INT:
        executePushInt();
        break;
      case PUSH_STRING:
        executePushString();
        break;
      case PUSH_FLOAT:
        executePushFloat();
        break;
      case ADD:
        executeAdd();
        break;
      default:
        throw new UnsupportedOperationException("Unknown instruction: " + instruction);
    }
  }

  public static void main(String[] args) {}
}
