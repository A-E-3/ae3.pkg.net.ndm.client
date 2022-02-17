package com.ndmsystems.ndmc;

/** @author myx */
public final class NdmLicenseStatic {
	
	/** <code>
	formatLicenseAsLabel3 = NATIVE_IMPL.formatLicenseAsLabel3 || function(k){
		return k.substr(0,3) + '-' + k.substr(3,3) + '-' + k.substr(6,3) + '-' + k.substr(9,3) + '-' + k.substr(12,3);
	};
	 * </code>
	 *
	 * @param license
	 * @return */
	public final static CharSequence formatLicenseAsLabel3(final CharSequence license) {
		
		return new StringBuilder(19)//
				.append(license.subSequence(0, 3))//
				.append('-')//
				.append(license.subSequence(3, 6))//
				.append('-')//
				.append(license.subSequence(6, 9))//
				.append('-')//
				.append(license.subSequence(9, 12))//
				.append('-')//
				.append(license.subSequence(12, 15))//
		;
	}
	
	/** <code>
	formatLicenseAsLabel5 = NATIVE_IMPL.formatLicenseAsLabel5 || function(k){
		return	k.substr(0,5) + '-' + k.substr(5,5) + '-' + k.substr(10,5);
	};
	 * </code>
	 *
	 * @param license
	 * @return */
	public final static CharSequence formatLicenseAsLabel5(final CharSequence license) {
		
		return new StringBuilder(17)//
				.append(license.subSequence(0, 5))//
				.append('-')//
				.append(license.subSequence(5, 10))//
				.append('-')//
				.append(license.subSequence(10, 15))//
		;
	}
	
	/** @param text
	 *
	 *            <code>
		validateLicenseFormat = NATIVE_IMPL.validateLicenseFormat || function(licenseNumber){
			return licenseNumber.replace(/\D/g, "").length === 15;
		};
	 * </code>
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
