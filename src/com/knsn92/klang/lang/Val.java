package com.knsn92.klang.lang;

import java.util.Objects;

import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.NoneType;
import com.knsn92.klang.util.ValueUtils;

public class Val {
	
	public static final Val NONE = Val.of(NoneType.type, null);
	
	public Val(IType type, Object val) {
		this.type = type;
		this.val = val;
	}

	public IType type;
	public Object val;
	
	public static Val of(IType type, Object val) {
		return new Val(type, val);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type, val);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Val)) {
			return false;
		}
		Val other = (Val) obj;
		return Objects.equals(type, other.type) && Objects.equals(val, other.val);
	}
	
	@Override
	public String toString() {
		String toStrVal = type.overrideValToString(val);
		return "Val[" + (type != null ? "type=" + type.getClass().getSimpleName() : "")
				+ toStrVal + "]";
	}
	
	public String toStr() {
		if(type == null) return "";
		if(ValueUtils.equalsType(this, NoneType.type)) return "None";
		return type.toStr(val);
	}
	
	public Val copy() {
		if(this == Val.NONE) return Val.NONE;
		return Val.of(type, val);
	}
	
}
