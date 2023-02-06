package com.ndmsystems.ndmc;

import ru.myx.sapi.RandomSAPI;

/** @author myx */
public final class NdmLicenseStatic {

	/** <code>
	"calcWCN8th" : {
		value : NATIVE_IMPL.exportCalcWCN8th || function(pin7){
			var accum = 0;
			accum += 3 * ((pin7.charAt(0) - '0');
			accum += 1 * ((pin7.charAt(1) - '0');
			accum += 3 * ((pin7.charAt(2) - '0');
			accum += 1 * ((pin7.charAt(3) - '0');
			accum += 3 * ((pin7.charAt(4) - '0');
			accum += 1 * ((pin7.charAt(5) - '0');
			accum += 3 * ((pin7.charAt(6) - '0');
			return (10 - (accum % 10)) % 10;
		}
	},
	 * </code>
	 *
	 * @param pin7
	 *            - string with 7 digits
	 * @return */
	public final static int exportCalcWCN8th(final CharSequence pin7) {

		int accum = 0;
		accum += 3 * ((pin7.charAt(0) - '0') % 10);
		accum += 1 * ((pin7.charAt(1) - '0') % 10);
		accum += 3 * ((pin7.charAt(2) - '0') % 10);
		accum += 1 * ((pin7.charAt(3) - '0') % 10);
		accum += 3 * ((pin7.charAt(4) - '0') % 10);
		accum += 1 * ((pin7.charAt(5) - '0') % 10);
		accum += 3 * ((pin7.charAt(6) - '0') % 10);
		return (10 - accum % 10) % 10;
	}
	
	/** <code>
	"randomWCN" : {
		// randomPin7 + calcWCN8th(randomPin7)
		value : NATIVE_IMPL.exportRandWCN || function(){
			const randomPin7 = Random.formattedString("DDDDDDD");
			return randomPin7 + Generator.calcWCN8th(randomPin7);
		}
	},
	 * </code>
	 *
	 * @return */
	public final static String exportRandWCN() {
		
		final String randomPin7 = RandomSAPI.formattedString("DDDDDDD");
		return randomPin7 + NdmLicenseStatic.exportCalcWCN8th(randomPin7);
	}
	
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
			for (int i = 14; i >= 0; --i) {
				if (!Character.isDigit(text.charAt(i))) {
					break digits15;
				}
			}
			return true;
		}

		return false;
	}
}
