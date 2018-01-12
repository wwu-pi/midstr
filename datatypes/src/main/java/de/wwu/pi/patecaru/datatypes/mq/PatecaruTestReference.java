package de.wwu.pi.patecaru.datatypes.mq;

import java.io.Serializable;

public class PatecaruTestReference implements Serializable {

	private static final long serialVersionUID = -5746839635530370703L;
	private Class<?> clazz;
	private String method;

	public PatecaruTestReference(Class<?> clazz, String method) {
		super();
		this.clazz = clazz;
		this.method = method;
	}

	public Class<?> getTestClass() {
		return clazz;
	}

	public String getTestMethod() {
		return method;
	}

	public int hashCode() {
		return clazz.toString().hashCode() * 13 + method.hashCode() * 17;
	}

}
