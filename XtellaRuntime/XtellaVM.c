#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include <unistr.h>
#include <gc.h>

void error_out(const char* message) {
    fprintf(stderr, "Error: %s\n", message);
    exit(EXIT_FALURE);
}

typedef struct {
	enum Kind {
	   INTEGER,
	   STRING,
	   RATIONAL,
	   OPCODE,
	} kind;

	union {
	   long integer;
	   unsigned char* string;
	   long double rational;
	   int opcode;
	};
} StackValue;

StackValue* createInteger(long value) {
    StackValue* integerStackValue = (StackValue*)GC_MALLOC(sizeof(StackValue));
    if (integerStackValue == NULL) {
        
        error_out("VM faced memory allocation error");
    }
    integerStackValue->kind = INTEGER;
    integerStackValue->integer = value;
    return integerStackValue;
}

StackValue* createString(const char* value) {
    StackValue* stringStackValue = (StackValue*)GC_MALLOC(sizeof(StackValue));
    if (stringStackValue == NULL) {
        
        error_out("VM faced memory allocation error");
    }
    stringStackValue->kind = STRING;
    stringStackValue->string = (unsigned char*)GC_MALLOC(strlen(value) + 1);
    if (stringStackValue->string == NULL) {
        
        free(stringStackValue);
        error_out("VM faced memory allocation error");
    }
    strcpy((char*)stringStackValue->string, value);
    return stringStackValue;
}

StackValue* createRational(long double value) {
    StackValue* rationalStackValue = (StackValue*)GC_MALLOC(sizeof(StackValue));
    if (rationalStackValue == NULL) {
        
        error_out("VM faced memory allocation error");
    }
    rationalStackValue->kind = RATIONAL;
    rationalStackValue->rational = value;
    return rationalStackValue;
}

StackValue* createOpcode(int value) {
    StackValue* opcodeStackValue = (StackValue*)GC_MALLOC(sizeof(StackValue));
    if (opcodeStackValue == NULL) {
        
        error_out("VM faced memory allocation error");
    }
    opcodeStackValue->kind = OPCODE;
    opcodeStackValue->opcode = value;   
    return opcodeStackValue;
}


typedef struct {
    StackValue* stack;
    int stack_size;
    int sp; 
    int* bytecode;
    int ip; 
} XtellaVM;


void initVM(XtellaVM* vm, int stack_size, int* bytecode, int bytecode_size) {
    vm->stack_size = stack_size;
    vm->stack = (int*)GC_MALLOC(stack_size * sizeof(int));
    vm->sp = -1; 
    vm->bytecode = bytecode;
    vm->ip = 0; 
}


void push(XtellaVM* vm, int value) {
    if (vm->sp < vm->stack_size - 1) {
        vm->stack[++vm->sp] = value;
    } else {
	error_out("VM faced stack overflow");
    }
}


int pop(XtellaVM* vm) {
    if (vm->sp >= 0) {
        return vm->stack[vm->sp--];
    } else {
   	error_out("VM faced stack underflow");
    }
}


int fetch(XtellaVM* vm) {
    return vm->bytecode[vm->ip++];
}
