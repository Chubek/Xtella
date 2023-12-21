import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XtellaVM {
    public static final int PUSH_INT = ;
    public static final int PUSH_STRING = ;
    public static final int PUSH_FLOAT = ;
    public static final int ADD = ;
    public static final int SUBTRACT = ;
    public static final int MULTIPLY = ;
    public static final int DIVIDE = ;
    public static final int MODULO = ;
    public static final int BITWISE_AND = ;
    public static final int BITWISE_OR = ;
    public static final int BITWISE_XOR = ;
    public static final int SHIFT_LEFT = ;
    public static final int SHIFT_RIGHT = ;
    public static final int LOGICAL_AND = ;
    public static final int LOGICAL_OR = ;
    public static final int LOGICAL_NOT = ;
    public static final int EQUAL = ;
    public static final int NOT_EQUAL = ;
    public static final int LESS_THAN = ;
    public static final int LESS_THAN_OR_EQUAL = ;
    public static final int GREATER_THAN = ;
    public static final int GREATER_THAN_OR_EQUAL = ;
    public static final int JUMP = ;
    public static final int JUMP_IF_TRUE = ;
    public static final int JUMP_IF_FALSE = ;
    public static final int CALL_FUNCTION = ;
    public static final int RETURN = ;
    public static final int LOAD_VARIABLE = ;
    public static final int STORE_VARIABLE = ;
   
    private Stack<Object> stack;		// vm stack
    private List<Map<String, Object>> scopes;   // vm lexical scopes

    private instruction_pointer;
    private frame_pointer;

    public XtellaVM() {
        stack = new Stack<>();
        variables = new ArrayList<>();
    }
   
    public static void push(Object instruction) { 
	this.stack.push(instruction);
	this.instruction_pointer++;
    }

    public static Object pop() {
	this.instruction_pointer--;
	return this.stack.pop();
    }

    
}

