package com.knsn92.klang.parse.token;

import org.apache.commons.lang3.ClassUtils;

import com.knsn92.klang.parse.token.type.ITokenType;

public class Token {
	
	public int line = -1;
	public int index = -1;
	private ITokenType type = null;
	private Object value = null;
	
	public static Token of(ITokenType type, Object value) {
		return new Token(type, value);
	}
	
	public Token(ITokenType type, Object value) {
		checkType(type, value);
		this.value = value;
		this.type = type;
	}
	
	public ITokenType type() {
		return type;
	}
	
	public Object value() {
		return value;
	}
	
	
	public Class<?> innerType() {
		return type.innerType();
	}
	
	public boolean equalsType(ITokenType type) {
		return this.type == type;
	}
	
	@Override
	public String toString() {
		String toStrVal = type.overrideValueToString(value);
		return "Token[" + (type != null ? "type=" + type.getClass().getSimpleName() : "")
				+ toStrVal + "]";
	}
	
	private static void checkType(ITokenType type, Object value) {
		if(type != value) {
			Class<?> typeClass = type.innerType();
			Class<?> valueClass = value.getClass();
			if(typeClass.isPrimitive()) {
				typeClass = ClassUtils.primitiveToWrapper(typeClass);
			}
			if(valueClass.isPrimitive()) {
				valueClass = ClassUtils.primitiveToWrapper(valueClass);
			}
			if(typeClass != valueClass)
				throw new ClassCastException("Cannot match type right:"+type.innerType().getSimpleName()+" current:"+value.getClass().getSimpleName());
		}
	}

}
