package com.benromberg.jackson.propertynameextractor;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.objenesis.ObjenesisStd;

public class MethodNameRecorder<T> implements MethodInterceptor {
	private T object;
	private String lastCalledMethodName;

	public MethodNameRecorder(Class<T> clazz) {
		Class<T> proxyClass = createProxyClass(clazz);
		Enhancer.registerCallbacks(proxyClass, new Callback[] { this });
		object = new ObjenesisStd().newInstance(proxyClass);
	}

	@SuppressWarnings("unchecked")
	private Class<T> createProxyClass(Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallbackType(MethodNameRecorder.class);
		return enhancer.createClass();
	}

	@Override
	public Object intercept(Object object, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
		lastCalledMethodName = method.getName();
		return null;
	}

	public String getLastCalledMethodName() {
		return lastCalledMethodName;
	}

	public T getObject() {
		return object;
	}
}