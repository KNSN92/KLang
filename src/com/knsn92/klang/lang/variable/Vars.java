
package com.knsn92.klang.lang.variable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;

public class Vars {
	
	public static final Vars global = new Vars(null);
	
	private BiMap<String, Integer> vars = HashBiMap.create();
	private Set<String> constants = Sets.newHashSet();
	private Set<String> notAsignedConstants = Sets.newHashSet();
	private Vars parent = null;
	
	
	public Vars() {
		this(global);
	}
	
	public Vars(Vars parent) {
		this.parent = parent;
	}
	
	public void newVar(String name) {
		int id = ValManager.manager().new_(Val.NONE);
		vars.put(name, id);
	}
	
	public void setVar(String name, int id) {
		int oldId = findVarId(name);
		ValManager.manager().discard(oldId);
		ValManager.manager().use(id);
		Vars vars = findVar(name);
		vars.vars.put(name, id);
		if(isNotAsignedConstant(name)) {
			notAsignedConstants.remove(name);
		}
	}
	
	public void setVar(String name, String nameto) {
		int newId = findVarId(nameto);
		setVar(name, newId);
	}
	
	public Val getVar(String name) {
		int id = findVarId(name);
		return ValManager.manager().get(id);
	}
	
	public void delVar(String name) {
		int id = findVarId(name);
		vars.remove(name);
		constants.remove(name);
		notAsignedConstants.remove(name);
		ValManager.manager().discard(id);
	}
	
	public void clearVar() {
		for(int id:vars.values()) {
			ValManager.manager().discard(id);
		}
		vars.clear();
		constants.clear();
		notAsignedConstants.clear();
	}
	
	public boolean hasVar(String name) {
		boolean has = vars.containsKey(name);
		if(!has && parent != null) has = parent.hasVar(name);
		return has;
	}
	
	public String varName(int id) {
		String name = vars.inverse().get(id);
		if(name == null && parent != null) name = parent.varName(id);
		return name;
	}
	
	public int findVarId(String name) {
		Integer id = vars.get(name);
		if(id == null && parent != null) id = parent.findVarId(name);
		return id;
	}
	
	public Vars findVar(String name) {
		Vars vars = this;
		if(!this.vars.containsKey(name)) vars = null;
		if(!this.vars.containsKey(name) && parent != null) vars = parent.findVar(name);
		return vars;
	}
	
	public Collection<String> names() {
		return Collections.unmodifiableCollection(vars.keySet());
	}
	
	public Collection<Integer> values() {
		return Collections.unmodifiableCollection(vars.values());
	}
	
	public Map<String, Integer> asMap() {
		return Collections.unmodifiableMap(vars);
	}
	
	public void setConst(String name) {
		setConst(name, true);
	}
	
	public void setConst(String name, boolean canAssignOnlyOnce) {
		if(!this.vars.containsKey(name) && parent != null) {
			parent.setConst(name, canAssignOnlyOnce);
		}else {
			constants.add(name);
			if(canAssignOnlyOnce) {
				notAsignedConstants.add(name);
			}
		}
	}
	
	public boolean isConst(String name) {
		boolean is = constants.contains(name);
		if(!is && parent != null) is = parent.isConst(name);
		return is;
	}
	public boolean isNotAsignedConstant(String name) {
		boolean is = notAsignedConstants.contains(name);
		if(!is && parent != null) is = parent.isNotAsignedConstant(name);
		return is;
	}
	
	public boolean canAssign(String name) {
		if(!isConst(name)) {
			
		}
		return !isConst(name) || isNotAsignedConstant(name);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[Variables]\n");
		for(Map.Entry<String, Integer> entry : vars.entrySet()) {
			String name = entry.getKey();
			Integer id = entry.getValue();
			Val val = ValManager.manager().get(id);
			name = (name == null ? "(Name not defined)" : name);
			String valStr = val == null ? "(None)" : val.toString();
			if(val != null && val.val.getClass().isArray()) {
				Object[] arr = (Object[])val.val;
				valStr = Arrays.deepToString(arr);
			}
			str.append(name+"[id="+id+", value="+valStr+"]\n");
		}
		str.deleteCharAt(str.length()-1);
		return str.toString();
	}
	
}
