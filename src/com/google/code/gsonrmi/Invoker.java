package com.google.code.gsonrmi;

import com.google.code.gsonrmi.annotations.RMI;
import com.google.gson.JsonParseException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Invoker {

	private final ParamProcessor paramProcessor;
	
	public Invoker(ParamProcessor paramProcessor) {
		this.paramProcessor = paramProcessor;
	}
	
	public RpcResponse doInvoke(RpcRequest request, Object target, Object context) {
		RpcResponse response = new RpcResponse();
		response.id = request.id;
		try {
			Parameter[] params = request.params != null ? request.params : new Parameter[0];
			Method m = findMethod(target, request.method, params);
			if (m == null) throw new NoSuchMethodException(request.method);
			
			Type[] paramTypes = m.getGenericParameterTypes();
			Annotation[][] paramAnnotations = m.getParameterAnnotations();
			Object[] processedParams = new Object[paramTypes.length];
			for (int i=0, j=0; i<processedParams.length; i++) {
				if (paramProcessor.isInjectedParam(paramAnnotations[i])) processedParams[i] = paramProcessor.injectParam(paramTypes[i], paramAnnotations[i], context);
				else processedParams[i] = paramProcessor.processParam(params[j++], paramTypes[i], paramAnnotations[i], context);
			}
			Object returnValue = m.invoke(target, processedParams);
			response.result = returnValue != null ? new Parameter(returnValue) : null;
		}
		catch (JsonParseException e) {
			response.error = RpcError.PARSER_ERROR;
            e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			response.error = new RpcError(RpcError.INVOCATION_EXCEPTION, e.getCause());
            e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			response.error = RpcError.METHOD_NOT_FOUND;
            e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			response.error = RpcError.INVALID_PARAMS;
            e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			response.error = RpcError.METHOD_NOT_FOUND;
            e.printStackTrace();
		}
		catch (ParamValidationException e) {
			response.error = new RpcError(RpcError.PARAM_VALIDATION_FAILED, e.getMessage());
            e.printStackTrace();
		}
		return response;
	}
	
	public Method findMethod(Object target, String method, Parameter[] params) {
		for (Method m : target.getClass().getMethods()) {
			RMI rmi = m.getAnnotation(RMI.class);
			if (rmi != null) {
				String rmiName = rmi.value();
				if (rmiName.isEmpty()) rmiName = m.getName();
				if (rmiName.equals(method)) {
					int countInjects = 0;
					for (Annotation[] a : m.getParameterAnnotations())
                        if (paramProcessor.isInjectedParam(a))
                            countInjects++;
					if (params.length + countInjects == m.getParameterTypes().length)
                        return m;
				}
			}
		}
		return null;
	}
	
	public static interface ParamProcessor {
		boolean isInjectedParam(Annotation[] paramAnnotations);
		Object injectParam(Type paramType, Annotation[] paramAnnotations, Object context) throws ParamValidationException;
		Object processParam(Parameter param, Type paramType, Annotation[] paramAnnotations, Object context) throws ParamValidationException;
	}
}
