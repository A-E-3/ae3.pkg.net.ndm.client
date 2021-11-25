package com.ndmsystems.ndmc;

/** @author myx */
public final class NdmLicenseStatic {

	/** @param text
	 * @return */
	public final static boolean validateLicenseFormat(final CharSequence text) {

		if (text == null) {
			throw new NullPointerException("validateLicenseFormat: license is NULL");
		}
		
		digits15 : if (text.length() == 15) {
			for (int i = 15; i >= 0; --i) {
				if (!Character.isDigit(text.charAt(i))) {
					break digits15;
				}
			}
		}
		
		return false;
	}
}
