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
  public static final int CALL_FUNCTION = 28;
  public static final int LOAD_FUNCTION = 29;
  public static final int RETURN = 30;
  public static final int LOAD_VARIABLE = 31;
  public static final int STORE_VARIABLE = 32;
  public static final int LOAD_VARIABLE_FROM_ARRAY = 33;
  public static final int STORE_VARIABLE_INTO_ARRAY = 34;
  public static final int LOAD_VARIABLE_FROM_HASHMAP = 35;
  public static final int STORE_VARIABLE_INTO_HASHMAP = 36;
  public static final int GET_VARARG = 37;
  public static final int REGEX_MATCH = 38;
  public static final int EXEC_COMMAND = 39;
  public static final int MAKE_VARIANT = 40;
  public static final int RUN_THREAD = 41;
  public static final int OPEN_FILE_FOR_READING = 42;
  public static final int OPEN_FILE_FOR_WRITING = 43;
  public static final int OPEN_FILE_FOR_READING_AND_WRITING = 44;
  public static final int CLOSE_FILE = 45;
  public static final int READ_FILE = 46;
  public static final int WRITE_FILE = 47;
  public static final int APPEND_FILE = 48;
  public static final int READ_LINE_FROM_FILE = 49;

  private Stack<int> instructionStack;
  private Stack<Object> operandStack;
  private List<Object> varArgs;
  private Map<String, Object> globalScope;
  private List<Map<String, Object>> scopes;
  private int programCounter;
  private int stackPointer;
  private int framePointer;
  private int scopeNumber;
  private int lastExitCode;

  public XtellaVM() {
    instructionStack = new Stack<>();
    operandStack = new Stack<>();
    scopes = new ArrayList<>();
    programCounter = 0;
    stackPointer = 0;
    framePointer = 0;
  }

  public static int getProgramCounter() {
    return programCounter;
  }

  public static int getStackPointer() {
    return stackPointer;
  }

  public static int getFramePointer() {
    return framePointer;
  }

  public static int getScopeNumber() {
    return scopeNumber;
  }

  public static int getLastExitCode() {
    return lastExitCode;
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
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_INT");
    }

    Object intValue = operandStack.pop();

    if (intValue instanceof Integer) {
      operandStack.push(intValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_INT");
    }

    framePointer++;
  }

  private void executePushString() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_STRING");
    }

    Object stringValue = operandStack.pop();

    if (stringValue instanceof String) {
      operandStack.push(stringValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_STRING");
    }

    framePointer++;
  }

  private void executePushFloat() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_FLOAT");
    }

    Object floatValue = operandStack.pop();

    if (floatValue instanceof Float) {
      operandStack.push(floatValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_FLOAT");
    }

    framePointer++;
  }

  private void executePushArray() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_ARRAY");
    }

    Object arrayObject = operandStack.pop();

    if (arrayObject instanceof List<?>) {
      operandStack.add(array);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_ARRAY");
    }

    framePointer += array.size();
  }

  private void executePushHashMap() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for PUSH_HASHMAP");
    }

    Object hashMapValue = operandStack.pop();

    if (hashMapValue instanceof HashMap<?, ?>) {
      operandStack.push(hashMapValue);
    } else {
      throw new IllegalArgumentException("Invalid operand type for PUSH_HASHMAP");
    }

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
    programCounter = 0;
  }

  private void executeJump() {

    int targetIndex = (int) operandStack.get(programCounter);

    programCounter = targetIndex;
  }

  private void executeJumpIfTrue() {

    boolean condition = (boolean) operandStack.pop();

    int targetIndex = (int) operandStack.get(programCounter);

    if (condition) {

      programCounter = targetIndex;
    } else {

      programCounter++;
    }
  }

  private void executeJumpIfFalse() {

    boolean condition = (boolean) operandStack.pop();

    int targetIndex = (int) operandStack.get(programCounter);

    if (!condition) {

      programCounter = targetIndex;
    } else {

      programCounter++;
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

  private void executeLoadVariableFromArray() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException(
          "Not enough operands on the stack for LOAD_VARIABLE_FROM_ARRAY");
    }

    int index = (int) operandStack.pop();
    Object arrayName = operandStack.pop();

    Object array = getInScope((String) arrayName);

    if (array instanceof Object[] && index >= 0 && index < ((Object[]) array).size()) {
      Object value = ((Object[]) array)[index];
      operandStack.push(value);
    } else {
      throw new IllegalArgumentException("Invalid array or index for LOAD_VARIABLE_FROM_ARRAY");
    }

    framePointer++;
  }

  private void executeLoadVariableFromHashMap() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException(
          "Not enough operands on the stack for LOAD_VARIABLE_FROM_HASHMAP");
    }

    Object key = operandStack.pop();
    Object mapName = operandStack.pop();

    Object map = getInScope((String) mapName);

    if (map instanceof HashMap && ((HashMap<?, ?>) map).containsKey(key)) {
      Object value = ((HashMap<?, ?>) map).get(key);
      operandStack.push(value);
    } else {
      throw new IllegalArgumentException("Invalid HashMap or key for LOAD_VARIABLE_FROM_HASHMAP");
    }

    framePointer++;
  }

  private void executeStoreVariableIntoHashMap() {
    if (operandStack.size() < 3) {
      throw new IllegalStateException(
          "Not enough operands on the stack for STORE_VARIABLE_INTO_HASHMAP");
    }

    Object value = operandStack.pop();
    Object key = operandStack.pop();
    Object mapName = operandStack.pop();

    Object map = getInScope((String) mapName);

    if (map instanceof HashMap) {
      ((HashMap<Object, Object>) map).put(key, value);
    } else {
      throw new IllegalArgumentException("Invalid HashMap for STORE_VARIABLE_IN_HASHMAP");
    }

    framePointer++;
  }

  private void executeStoreVariableIntoArray() {
    if (operandStack.size() < 3) {
      throw new IllegalStateException(
          "Not enough operands on the stack for STORE_VARIABLE_INTO_ARRAY");
    }

    Object value = operandStack.pop();
    int index = (int) operandStack.pop();
    Object arrayName = operandStack.pop();

    Object array = getInScope((String) arrayName);

    if (array instanceof Object[] && index >= 0 && index < ((Object[]) array).size()) {
      ((Object[]) array)[index] = value;
    } else {
      throw new IllegalArgumentException("Invalid array or index for STORE_VARIABLE_IN_ARRAY");
    }

    framePointer++;
  }

  private void executeLoadFunction() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for LOAD_FUNCTION");
    }

    String functionName = (String) operandStack.pop();
    List<String> functionParams = (List<String>) operandStack.pop();

    globalScope.put(functionName, programCounter);
    globalScope.put(functionName + "_params", functionParams);

    programCounter++;
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

    boolean runThreaded = (boolean) stack.pop();
    boolean runImmediately = (boolean) stack.pop();
    String threadIdentifier = (String) stack.pop();

    if (runThreaded) {
      Thread t =
          new Thread(
              () -> {
                executeVM(functionAddress);
              });
      if (runImmediately) {
        t.start();
      } else {
        globalScope.put(threadIdentifier, t);
      }
    } else {
      executeVM(functionAddress);
    }

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

  private void executeMakeVariant() {
    if (operandStack.size() < 1) {
      throw new IllegalStateExeception("Not enough operands on the stack for MAKE_VARIANT");
    }

    List<String> params = new List<>();

    String varianetName = (String) operandStack.pop();
    int numParams = (int) operandStack.pop();

    while (numParams--) {
      String paramName = operandStack.pop();
      params.add(paramName);
    }

    globalScope.put(variantName, params);

    programCounter++;
  }

  private void executeRunThread() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for RUN_THREAD");
    }

    String threadIdentifier = (String) stack.pop();

    Thread t = (Thread) globalScope.get(threadIdentifier);

    t.start();
    t.join();
  }

  private void executeOpenFileForReading() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for OPEN_FILE");
    }

    Object filenameObject = operandStack.pop();

    if (filenameObject instanceof String) {
      String filename = (String) filenameObject;

      try {
        FileReader fileReader = new FileReader(new File(filename));
        operandStack.push(fileReader);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Error opening file: " + filename, e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for OPEN_FILE");
    }

    framePointer++;
  }

  private void executeOpenFileForWriting() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for OPEN_FILE");
    }

    Object filenameObject = operandStack.pop();

    if (filenameObject instanceof String) {
      String filename = (String) filenameObject;

      try {
        FileWriter fileWriter = new FileWriter(new File(filename));
        operandStack.push(fileWriter);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Error opening file: " + filename, e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for OPEN_FILE");
    }

    framePointer++;
  }

  private void executeOpenFileForReadingAndWriting() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException(
          "Not enough operands on the stack for OPEN_FILE_FOR_READING_AND_WRITING");
    }

    Object filePathObject = operandStack.pop();

    if (filePathObject instanceof String) {
      String filePath = (String) filePathObject;
      File file = new File(filePath);

      try {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        operandStack.push(randomAccessFile);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("File not found: " + filePath, e);
      } catch (IOException e) {
        throw new RuntimeException("Error opening file: " + filePath, e);
      }
    } else {
      throw new IllegalArgumentException(
          "Invalid operand type for OPEN_FILE_FOR_READING_AND_WRITING");
    }

    framePointer++;
  }

  private void executeCloseFile() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for CLOSE_FILE");
    }

    Object fileObject = operandStack.pop();

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
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for READ_FILE");
    }

    Object fileObject = operandStack.pop();

    if (fileObject instanceof FileReader) {
      FileReader fileReader = (FileReader) fileObject;
      try (BufferedReader reader = new BufferedReader(fileReader)) {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
          content.append(line).append(System.lineSeparator());
        }

        operandStack.push(content.toString().trim());
      } catch (IOException e) {
        throw new RuntimeException("Error reading file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for READ_FILE");
    }

    framePointer++;
  }

  private void executeReadLineFromFile() {
    if (operandStack.size() < 1) {
      throw new IllegalStateException("Not enough operands on the stack for READ_LINE_FROM_FILE");
    }

    Object fileObject = operandStack.pop();

    if (fileObject instanceof FileReader || fileObject instanceof RandomAccessFile) {
      FileReader fileReader = (FileReader) fileObject;
      try (BufferedReader reader = new BufferedReader(fileReader)) {
        String line = reader.readLine();
        operandStack.push(line != null ? line : "");
      } catch (IOException e) {
        throw new RuntimeException("Error reading line from file", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid operand type for READ_LINE_FROM_FILE");
    }

    framePointer++;
  }

  private void executeWriteFile() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for WRITE_FILE");
    }

    Object fileObject = operandStack.pop();
    Object contentObject = operandStack.pop();

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

    framePointer++;
  }

  private void executeAppendFile() {
    if (operandStack.size() < 2) {
      throw new IllegalStateException("Not enough operands on the stack for APPEND_FILE");
    }

    Object fileObject = operandStack.pop();
    Object contentObject = operandStack.pop();

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

  public static void executeVM(int programCounter) {}
}
