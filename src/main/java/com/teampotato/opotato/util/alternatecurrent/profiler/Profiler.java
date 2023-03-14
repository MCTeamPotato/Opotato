package com.teampotato.opotato.util.alternatecurrent.profiler;

public interface Profiler {
	
	Profiler DUMMY = new Profiler() {
		@Override
		public void start() { }

		@Override
		public void end() { }

		@Override
		public void push(String location) { }

		@Override
		public void pop() { }

		@Override
		public void swap(String location) { }
	};
	
	void start();
	
	void end();
	
	void push(String location);
	
	void pop();
	
	void swap(String location);
	
}
