package com.google.code.gsonrmi.serializer;

import com.google.code.gsonrmi.Parameter;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ParameterSerializer implements JsonSerializer<Parameter>, JsonDeserializer<Parameter> {

	@Override
	public Parameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new Parameter(json);
	}

	@Override
	public JsonElement serialize(Parameter src, Type typeOfSrc, JsonSerializationContext context) {
		return src.getSerializedValue(context);
	}

}
