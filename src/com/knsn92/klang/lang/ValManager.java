package com.knsn92.klang.lang;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.collect.Maps;

public class ValManager {
	
	private static ValManager instance = new ValManager();
	
	private Map<Integer, Val> vals = Maps.newHashMap();
	private Map<Integer, Integer> using = Maps.newHashMap();
	private Deque<Integer> deleted = new ArrayDeque<>();
	private int nextID = 0;
	
	private ValManager() {}
	
	public static ValManager manager() {
		return instance;
	}
	
	public void init() {
		vals.clear();
		using.clear();
		deleted.clear();
		nextID = 0;
	}
	
	public int new_(Val val) {
		int id = issue();
		use(id);
		set(id, val);
		return id;
	}
	
	private void set(int id, Val value) {
		vals.put(id, value);
	}
	
	public Val get(int id) {
		if(!vals.containsKey(id)) {
			throw new NoSuchElementException();
		}
		return vals.get(id);
	}
	
	public boolean has(int id) {
		return vals.containsKey(id);
	}
	
	public void use(int id) {
		Integer users = using.get(id);
		if(users == null) users = 0;
		++users;
		using.put(id, users);
	}
	
	public void discard(int id) {
		int users = using.get(id);
		--users;
		using.put(id, users);
		if(users <= 0) {
			collect(id);
			using.remove(id);
		}
	}
	
	private int issue() {
		int id;
		if(!deleted.isEmpty()) {
			id = deleted.poll();
		}else {
			id = nextID++;
		}
		vals.put(id, Val.NONE);
		return id;
	}
	
	private void collect(int id) {
		vals.remove(id);
		deleted.add(id);
	}
	
}
