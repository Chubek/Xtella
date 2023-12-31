import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XtellaVM {

  public static final int PUSH_INT = 1;
  public static final int PUSH_STRING = 2;
  public static final int PUSH_FLOAT = 3;
  public static final int PUSH_ARRAY = 4;
  public static final int PUSH_HASHMAP = 5;
  public static final int ADD = 6;
  public static final int SUBTRACT = 7;
  public static final int MULTIPLY = 8;
  public static final int DIVIDE = 9;
  public static final int MODULO = 10;
  public static final int BITWISE_AND = 11;
  public static final int BITWISE_OR = 12;
  public static final int BITWISE_XOR = 13;
  public static final int SHIFT_LEFT = 14;
  public static final int SHIFT_RIGHT = 15;
  public static final int LOGICAL_AND = 16;
  public static final int LOGICAL_OR = 17;
  public static final int LOGICAL_NOT = 18;
  public static final int EQUAL = 19;
  public static final int NOT_EQUAL = 20;
  public static final int LESS_THAN = 21;
  public static final int LESS_THAN_OR_EQUAL = 22;
  public static final int GREATER_THAN = 23;
  public static final int GREATER_THAN_OR_EQUAL = 24;
  public static final int JUMP = 25;
  public static final int JUMP_IF_TRUE = 26;
  public static final int JUMP_IF_FALSE = 27;
  public static final int RETURN = 28;
  public static final int LOAD_VARIABLE = 29;
  public static final int STORE_VARIABLE = 30;
  public static final int LOAD_VARIABLE_FROM_ARRAY = 31;
  public static final int STORE_VARIABLE_INTO_ARRAY = 32;
  public static final int LOAD_VARIABLE_FROM_HASHMAP = 33;
  public static final int STORE_VARIABLE_INTO_HASHMAP = 34;
  public static final int REGEX_MATCH = 36;
  public static final int EXEC_COMMAND = 37;
  public static final int MAKE_VARIANT = 38;
  public static final int RUN_THREAD = 39;
  public static final int OPEN_FILE_FOR_READING = 40;
  public static final int OPEN_FILE_FOR_WRITING = 41;
  public static final int OPEN_FILE_FOR_READING_AND_WRITING = 42;
  public static final int CLOSE_FILE = 43;
  public static final int READ_FILE = 44;
  public static final int WRITE_FILE = 45;
  public static final int APPEND_FILE = 46;
  public static final int READ_LINE_FROM_FILE = 47;
  public static final int SWAP_VALUE = 48;
  public static final int DUPLICATE_VALUE = 49;

  private Stack<int> instructionStack;
  private Stack<Object> operandStack;
  private Map<String, Object> globalScope;
  private List<Map<String, Object>> scopes;
  private int instructionPointer;
  private int stackPointer;
  private int framePointer;
  private int scopeNumber;
  private int lastExitCode;

  public XtellaVM() {
    this.instructionStack = new Stack<>();
    this.operandStack = new Stack<>();
    this.scopes = new ArrayList<>();
    this.instructionPointer = 0;
    this.stackPointer = 0;
    this.framePointer = 0;
  }

  public static int getProgramCounter() {
    return this.instructionPointer;
  }

  public static int getStackPointer() {
    return this.stackPointer;
  }

  public static int getFramePointer() {
    return this.framePointer;
  }

  public static int getScopeNumber() {
    return this.scopeNumber;
  }

  public static int getLastExitCode() {
    return this.lastExitCode;
  }

  private static int newFrame() {
    this.framePointer = this.stackPointer;
    Map<String, Object> newScope = new HashMap<>();
    this.scopes.add(newScope);
    return this.scopeNumber++;
  }

  private static void dropFrame() {
    this.scopes.remove(this.scopesNumber - 1);
    this.stackPointer += this.framePointer;
    this.scopesNumber--;
  }

  private static Object getInScope(String identifier) {
    return this.scopes.get(this.scopeNumber).get(identifier);
  }

  private static void putInScope(String identifier, Object object) {
    this.scopes.get(this.scopeNumber).put(identifier, object);
  }

  private void executePushInt() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_INT");
    }

    Object intValue = this.operandStack.pop();

    if (intValue instanceof Integer) {
      this.operandStack.push(intValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_INT");
    }

    this.framePointer++;
  }

  private void executePushString() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_STRING");
    }

    Object stringValue = this.operandStack.pop();

    if (stringValue instanceof String) {
      this.operandStack.push(stringValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_STRING");
    }

    this.framePointer++;
  }

  private void executePushFloat() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_FLOAT");
    }

    Object floatValue = this.operandStack.pop();

    if (floatValue instanceof Float) {
      this.operandStack.push(floatValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_FLOAT");
    }

    this.framePointer++;
  }

  private void executePushArray() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_ARRAY");
    }

    Object arrayObject = this.operandStack.pop();

    if (arrayObject instanceof List<?>) {
      this.operandStack.add(array);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_ARRAY");
    }

    this.framePointer += array.size();
  }

  private void executePushHashMap() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_HASHMAP");
    }

    Object hashMapValue = this.operandStack.pop();

    if (hashMapValue instanceof HashMap<?, ?>) {
      this.operandStack.push(hashMapValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_HASHMAP");
    }

    this.framePointer++;
  }

  private void executeAdd() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for ADD");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 + (int) operand2;
      this.operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand1 + (float) operand2;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for ADD");
    }

    this.framePointer++;
  }

  private void executeSubtract() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SUBTRACT");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 - (int) operand1;
      this.operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand2 - (float) operand1;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SUBTRACT");
    }

    this.framePointer++;
  }

  private void executeMultiply() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for MULTIPLY");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 * (int) operand2;
      this.operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand1 * (float) operand2;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for MULTIPLY");
    }

    this.framePointer++;
  }

  private void executeDivide() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for DIVIDE");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 / (int) operand1;
      this.operandStack.push(result);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      float result = (float) operand2 / (float) operand1;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for DIVIDE");
    }

    this.framePointer++;
  }

  private void executeModulo() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for MODULO");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 % (int) operand1;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for MODULO");
    }

    this.framePointer++;
  }

  private void executeBitwiseAnd() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_AND");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 & (int) operand2;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_AND");
    }

    this.framePointer++;
  }

  private void executeBitwiseOr() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_OR");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 | (int) operand2;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_OR");
    }

    this.framePointer++;
  }

  private void executeBitwiseXor() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for BITWISE_XOR");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand1 ^ (int) operand2;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for BITWISE_XOR");
    }

    this.framePointer++;
  }

  private void executeShiftLeft() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SHIFT_LEFT");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 << (int) operand1;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SHIFT_LEFT");
    }

    this.framePointer++;
  }

  private void executeShiftRight() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SHIFT_RIGHT");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      int result = (int) operand2 >> (int) operand1;
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for SHIFT_RIGHT");
    }

    this.framePointer++;
  }

  private void executeEqual() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for EQUAL");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

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

    this.operandStack.push(result);
    this.framePointer++;
  }

  private void executeNotEqual() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for NOT_EQUAL");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

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

    this.operandStack.push(result);
    this.framePointer++;
  }

  private void executeLessThan() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for LESS_THAN");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) < ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) < ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for LESS_THAN");
    }

    this.operandStack.push(result);
    this.framePointer++;
  }

  private void executeLessThanOrEqual() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for LESS_THAN_OR_EQUAL");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) <= ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) <= ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for LESS_THAN_OR_EQUAL");
    }

    this.operandStack.push(result);
    this.framePointer++;
  }

  private void executeGreaterThan() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for GREATER_THAN");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) > ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) > ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for GREATER_THAN");
    }

    this.operandStack.push(result);
    this.framePointer++;
  }

  private void executeGreaterThanOrEqual() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for GREATER_THAN_OR_EQUAL");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    boolean result;

    if (operand1 instanceof Integer && operand2 instanceof Integer) {
      result = ((int) operand2) >= ((int) operand1);
    } else if (operand1 instanceof Float && operand2 instanceof Float) {
      result = ((float) operand2) >= ((float) operand1);
    } else {
      throw new IllegalArgumentException("Invalid operand types for GREATER_THAN_OR_EQUAL");
    }

    this.operandStack.push(result);
    this.framePointer++;
  }

  private void executeReturn() {
    if (this.framePointer == 0) {
      throw new IllegalStateException("Cannot RETURN from the main frame");
    }

    int returnIP = (int) this.operandStack.pop();
    this.instructionPointer = returnIP;

    dropFrame();
  }

  private void executeJump() {
    int targetIndex = (int) this.operandStack.pop();
    this.instructionPointer = targetIndex;
  }

  private void executeJumpIfTrue() {

    boolean condition = (boolean) this.operandStack.pop();
    int targetIndex = (int) this.operandStack.pop();

    if (condition) {

      this.instructionPointer = targetIndex;
    } else {

      this.instructionPointer++;
    }
  }

  private void executeJumpIfFalse() {

    boolean condition = (boolean) this.operandStack.pop();
    int targetIndex = (int) this.operandStack.pop();

    if (!condition) {

      this.instructionPointer = targetIndex;
    } else {

      this.instructionPointer++;
    }
  }

  private void executeLoadVariable() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for LOAD_VARIABLE");
    }

    Object variableName = this.operandStack.pop();
    Object variableValue = getInScope((String) variableName);

    if (variableValue == null) {
      throw new IllegalArgumentException("Variable not found: " + variableName);
    }

    this.operandStack.push(variableValue);
    this.framePointer++;
  }

  private void executeStoreVariable() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for STORE_VARIABLE");
    }

    Object variableName = this.operandStack.pop();
    Object variableValue = this.operandStack.pop();

    putInScope((String) variableName, variableValue);
    this.framePointer++;
  }

  private void executeLoadVariableFromArray() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException(
          "Not enough operands on the stack for LOAD_VARIABLE_FROM_ARRAY");
    }

    int index = (int) this.operandStack.pop();
    Object arrayName = this.operandStack.pop();

    Object array = getInScope((String) arrayName);

    if (array instanceof Object[] && index >= 0 && index < ((Object[]) array).size()) {
      Object value = ((Object[]) array)[index];
      this.operandStack.push(value);
    } else {
      throw new IllegalArgumentException("Invalid array or index for LOAD_VARIABLE_FROM_ARRAY");
    }

    this.framePointer++;
  }

  private void executeLoadVariableFromHashMap() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException(
          "Not enough operands on the stack for LOAD_VARIABLE_FROM_HASHMAP");
    }

    Object key = this.operandStack.pop();
    Object mapName = this.operandStack.pop();

    Object map = getInScope((String) mapName);

    if (map instanceof HashMap && ((HashMap<?, ?>) map).containsKey(key)) {
      Object value = ((HashMap<?, ?>) map).get(key);
      this.operandStack.push(value);
    } else {
      throw new IllegalArgumentException("Invalid HashMap or key for LOAD_VARIABLE_FROM_HASHMAP");
    }

    this.framePointer++;
  }

  private void executeStoreVariableIntoHashMap() {
    if (this.operandStack.size() < 3) {
      throw new IllegalStateException(
          "Not enough operands on the stack for STORE_VARIABLE_INTO_HASHMAP");
    }

    Object value = this.operandStack.pop();
    Object key = this.operandStack.pop();
    Object mapName = this.operandStack.pop();

    Object map = getInScope((String) mapName);

    if (map instanceof HashMap) {
      ((HashMap<Object, Object>) map).put(key, value);
    } else {
      throw new IllegalArgumentException("Invalid HashMap for STORE_VARIABLE_IN_HASHMAP");
    }

    this.framePointer++;
  }

  private void executeStoreVariableIntoArray() {
    if (this.operandStack.size() < 3) {
      throw new IllegalStateException(
          "Not enough operands on the stack for STORE_VARIABLE_INTO_ARRAY");
    }

    Object value = this.operandStack.pop();
    int index = (int) this.operandStack.pop();
    Object arrayName = this.operandStack.pop();

    Object array = getInScope((String) arrayName);

    if (array instanceof Object[] && index >= 0 && index < ((Object[]) array).size()) {
      ((Object[]) array)[index] = value;
    } else {
      throw new IllegalArgumentException("Invalid array or index for STORE_VARIABLE_IN_ARRAY");
    }

    this.framePointer++;
  }

  private void executeRegexMatch() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for REGEX_MATCH");
    }

    Object operand1 = this.operandStack.pop();
    Object operand2 = this.operandStack.pop();

    if (operand1 instanceof String && operand2 instanceof String) {
      String input = (String) operand1;
      String pattern = (String) operand2;

      boolean result = input.matches(pattern);
      this.operandStack.push(result);
    } else {
      throw new IllegalArgumentException("Invalid operand types for REGEX_MATCH");
    }

    this.framePointer++;
  }

  private void executeExecCommand() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for EXEC_COMMAND");
    }

    Object command = this.operandStack.pop();

    if (command instanceof String) {
      String commandString = (String) command;

      try {
        Process process = Runtime.getRuntime().exec(commandString);
        this.lastExitCode = process.waitFor();
        this.operandStack.push(this.lastExitCode);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException("Error executing command: " + commandString, e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for EXEC_COMMAND");
    }

    this.framePointer++;
  }

  private void executeMakeVariant() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateExeception("Not enough operands on the stack for MAKE_VARIANT");
    }

    List<String> params = new List<>();

    String varianetName = (String) this.operandStack.pop();
    int numParams = (int) this.operandStack.pop();

    while (numParams--) {
      String paramName = this.operandStack.pop();
      params.add(paramName);
    }

    this.globalScope.put(variantName, params);
  }

  private void executeRunThread() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for RUN_THREAD");
    }

    String threadIdentifier = (String) stack.pop();

    Thread t = (Thread) this.globalScope.get(threadIdentifier);

    t.start();
    t.join();
  }

  private void executeOpenFileForReading() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for OPEN_FILE");
    }

    Object filenameObject = this.operandStack.pop();

    if (filenameObject instanceof String) {
      String filename = (String) filenameObject;

      try {
        FileReader fileReader = new FileReader(new File(filename));
        this.operandStack.push(fileReader);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Error opening file: " + filename, e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for OPEN_FILE");
    }

    this.framePointer++;
  }

  private void executeOpenFileForWriting() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for OPEN_FILE");
    }

    Object filenameObject = this.operandStack.pop();

    if (filenameObject instanceof String) {
      String filename = (String) filenameObject;

      try {
        FileWriter fileWriter = new FileWriter(new File(filename));
        this.operandStack.push(fileWriter);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Error opening file: " + filename, e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for OPEN_FILE");
    }

    this.framePointer++;
  }

  private void executeOpenFileForReadingAndWriting() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException(
          "Not enough operands on the stack for OPEN_FILE_FOR_READING_AND_WRITING");
    }

    Object filePathObject = this.operandStack.pop();

    if (filePathObject instanceof String) {
      String filePath = (String) filePathObject;
      File file = new File(filePath);

      try {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        this.operandStack.push(randomAccessFile);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("File not found: " + filePath, e);
      } catch (IOException e) {
        throw new RuntimeException("Error opening file: " + filePath, e);
      }
    } else {
      throw new IllegalArgumentException(
          "Invalid operand type for OPEN_FILE_FOR_READING_AND_WRITING");
    }

    this.framePointer++;
  }

  private void executeCloseFile() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for CLOSE_FILE");
    }

    Object fileObject = this.operandStack.pop();

    if (fileObject instanceof Closeable) {
      Closeable fileToClose = (Closeable) fileObject;

      try {
        fileToClose.close();
      } catch (IOException e) {
        throw new RuntimeException("Error closing file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for CLOSE_FILE");
    }
  }

  private void executeReadFileForReading() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for READ_FILE");
    }

    Object fileObject = this.operandStack.pop();

    if (fileObject instanceof FileReader) {
      FileReader fileReader = (FileReader) fileObject;
      try (BufferedReader reader = new BufferedReader(fileReader)) {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
          content.append(line).append(System.lineSeparator());
        }

        this.operandStack.push(content.toString().trim());
      } catch (IOException e) {
        throw new RuntimeException("Error reading file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for READ_FILE");
    }

    this.framePointer++;
  }

  private void executeReadLineFromFile() {
    if (this.operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for READ_LINE_FROM_FILE");
    }

    Object fileObject = this.operandStack.pop();

    if (fileObject instanceof FileReader || fileObject instanceof RandomAccessFile) {
      FileReader fileReader = (FileReader) fileObject;
      try (BufferedReader reader = new BufferedReader(fileReader)) {
        String line = reader.readLine();
        this.operandStack.push(line != null ? line : "");
      } catch (IOException e) {
        throw new RuntimeException("Error reading line from file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for READ_LINE_FROM_FILE");
    }

    this.framePointer++;
  }

  private void executeWriteFile() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for WRITE_FILE");
    }

    Object fileObject = this.operandStack.pop();
    Object contentObject = this.operandStack.pop();

    if ((fileObject instanceof FileWriter || fileObject instanceof RandomAccessFile)
        && contentObject instanceof String) {
      FileWriter fileWriter = (FileWriter) fileObject;
      String content = (String) contentObject;
      try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
        writer.write(content);
      } catch (IOException e) {
        throw new RuntimeException("Error writing to file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand types for WRITE_FILE");
    }

    this.framePointer++;
  }

  private void executeAppendFile() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for APPEND_FILE");
    }

    Object fileObject = this.operandStack.pop();
    Object contentObject = this.operandStack.pop();

    if ((fileObject instanceof FileWriter || fileObject instanceof RandomAccessFile)
        && contentObject instanceof String) {
      FileWriter fileWriter = (FileWriter) fileObject;
      String content = (String) contentObject;
      try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
        writer.append(content);
      } catch (IOException e) {
        throw new RuntimeException("Error appending to file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand types for APPEND_FILE");
    }
  }

  private void executeSwapValue() {
    if (this.operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for SWAP_VALUE");
    }

    Object swapped = this.operandStack.pop();
    Object swappee = this.operandStack.pop();

    stack.push(swappee);
    stack.push(swapped);

    this.framePointer += 2;
  }

  private void executeDuplicateValue() {
    if (opernadStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for DUPLICATE_VALUE");
    }

    Object duplicatedValue = this.operandStack.pop();
    stack.push(duplicatedValue);

    this.framePointer++;
  }
}
