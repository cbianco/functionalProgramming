package it.cbnoc.io;

import it.cbnoc.tuple.Tuple2;
import it.cbnoc.utils.Result;

public interface Input {

	Result<Tuple2<String, Input>> readString();

	Result<Tuple2<Integer, Input>> readInt();

	default Result<Tuple2<String, Input>> readString(String message){
		return readString();
	}

	default Result<Tuple2<Integer, Input>> readInt(String message){
		return readInt();
	}

}
