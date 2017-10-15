package it.cbnoc.io;

public interface IOutPut {

	void run();

	IOutPut empty = () -> {};

	default IOutPut add(IOutPut io) {
		return () -> {
			this.run();
			io.run();
		};
	}
}
