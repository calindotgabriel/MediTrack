package com.google.code.gsonrmi.transport.rmi;

import com.google.code.gsonrmi.RpcRequest;
import com.google.code.gsonrmi.RpcResponse;
import com.google.code.gsonrmi.transport.Route;

import java.util.List;

public interface RpcHandler {

	RpcResponse handle(RpcRequest request, Route dest, Route src);
	void handle(RpcResponse response, Route dest, List<Route> srcs, Callback callback);
	void periodicCleanup();
	void shutdown();
}
