package it.cbnoc.io;

import it.cbnoc.collection.List;
import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

public class ScriptReader implements Input {

	private final List<String> commands;

	public ScriptReader(List<String> commands) {
		super();
		this.commands = commands;
	}

	public ScriptReader(String... commands) {
		super();
		this.commands = List.list(commands);
	}

	public Result<Tuple2<String, Input>> readString() {
		return commands.isEmpty()
			? Result.failure("Not enough entries in script")
			: Result.success(new Tuple2<>(commands.headOption().getOrElse(""),
			new ScriptReader(commands.drop(1))));
	}

	@Override
	public Result<Tuple2<Integer, Input>> readInt() {
		try {
			return commands.isEmpty()
				? Result.failure("Not enough entries in script")
				: Integer.parseInt(commands.headOption().getOrElse("")) >= 0
					? Result.success(new Tuple2<>(Integer.parseInt(
						commands.headOption().getOrElse("")),
						new ScriptReader(commands.drop(1))))
					: Result.empty();
		} catch(Exception e) {
			return Result.failure(e);
		}
	}
}