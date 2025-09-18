package com.ndmsystems.ndmc;

/**
 *
 */
public class NdmpLink {
	
	public static NdmpLink fromIndexKey() {
		
		return null;
	}
	public static NdmpLink fromLocalKey() {
		
		return null;
	}

	private final String serviceId;

	private NdmpLink(final String serviceId) {
		
		this.serviceId = serviceId;
	}

	public String getDeviceEcPublicBase64() {

		return null;
	}
	
	public String getServiceEcPublicBase64() {
		
		return null;
	}
	
	/** @return */
	public String getServiceId() {
		
		return this.serviceId;
	}
	
	@Override
	public String toString() {

		return String.format("[NdmpLink %s]", this.getServiceId());
	}
}
