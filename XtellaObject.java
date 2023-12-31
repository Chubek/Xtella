import java.util.HashMap;
import java.util.Map;

public class XtellaObject {
  enum XtellaType {
    INTEGER,
    STRING,
    FLOAT,
    METHOD,
    CALLABLE,
    CLOSURE,
    REGEX,
    FILEHANDLE,
    FIELD,
    BINARYOP,
    UNARYOP,
    TERNARYOP,
    SYMBOL,
  }

  private XtellaBytecodeChunk bytecodeChunk;
  private XtellaObject parent;
  private XtellaType type;
  private Map<String, XtellaObject> methods;
  private Map<String, XtellaObject> fields;

  public XtellaObject(XtellaType type, XtellaBytecodeChunk bytecodeChunk, XtellaObject parent) {
    this.type = type;
    this.bytecodeChunk = bytecodeChunk;
    this.parent = parent;
    this.methods = new HashMap<>();
    initializeDefaults();
  }

  private void addBinaryOpMethods() {
    if (type == XtellaType.INTEGER || type == XtellaType.FLOAT || type == XtellaType.STRING) {
      addMethod("binaryAdd", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binarySubtract", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryMultiply", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryDivide", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryModulo", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryXor", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryAnd", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryOr", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));

      addMethod("binaryShr", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryShl", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryEq", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryNe", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryGt", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryGe", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryLt", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("binaryLe", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryRegexMatch", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryRegexNonMatch",
          new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryLogicalAnd", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "binaryLogicalOr", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    }
  }

  private void addUnaryOpMethods() {
    if (type == XtellaType.INTEGER || type == XtellaType.FLOAT || type == XtellaType.STRING) {
      addMethod("unaryPlus", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("unaryMinus", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("unaryNot", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "unaryLogicalNot", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "unaryPreIncrement",
          new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));

      addMethod(
          "unaryPreDecrement",
          new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod(
          "unaryPostIncrement",
          new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));

      addMethod(
          "unaryPostDecrement",
          new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    }
  }

  private void initializeDefaults() {
    if (type == XtellaType.INTEGER) {
      addMethod("assignment", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addBinaryOpMethods();
      addUnaryOpMethods();
    } else if (type == XtellaType.FLOAT) {
      addMethod("assignment", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addBinaryOpMethods();
      addUnaryOpMethods();
    } else if (type == XtellaType.CLOSURE) {
      addMethod("assignment", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.STRING) {
      addMethod("assignment", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addBinaryOpMethods();
    } else if (type == XtellaType.REGEX) {
      addMethod("match", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("replace", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.FILEHANDLE) {
      addMethod("read", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("write", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.FIELD) {
      addMethod("get", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
      addMethod("set", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.BINARYOP) {
      addMethod("evaluate", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.UNARYOP) {
      addMethod("apply", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.TERNARYOP) {
      addMethod("operate", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.SYMBOL) {
      addMethod("evaluate", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    } else if (type == XtellaType.CALLABLE) {
      addMethod("call", new XtellaObject(XtellaType.METHOD, new XtellaBytecodeChunk(), this));
    }
  }

  public XtellaType getType() {
    return this.type;
  }

  public XtellaBytecodeChunk getBytecodeChunk() {
    return bytecodeChunk;
  }

  public void setBytecodeChunk(XtellaBytecodeChunk bytecodeChunk) {
    this.bytecodeChunk = bytecodeChunk;
  }

  public void addMethod(String methodName, XtellaObject method) {
    methods.put(methodName, method);
  }

  public void addField(String fieldName, XtellaObject field) {
    fields.put(fieldName, field);
  }
}
