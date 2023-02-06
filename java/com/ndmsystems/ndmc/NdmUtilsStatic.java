package com.ndmsystems.ndmc;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx */
@ReflectionManual
public class NdmUtilsStatic {

	/** @param binary
	 * @param map
	 * @param keyExactLength
	 * @param value
	 * @return
	 * @throws IOException
	 * @throws ConcurrentModificationException */
	@ReflectionExplicit
	public static final int readTextFirstColumnToMapKeys(final TransferCopier binary, final BaseMap map, final int keyExactLength, final BaseObject value)
			throws ConcurrentModificationException, IOException {

		final Scanner scanner = new Scanner(binary.nextReaderUtf8());
		scanner.useDelimiter("\n");
		
		int count = 0;
		
		for (; scanner.hasNext();) {
			final String line = scanner.next();
			final int length = line.length();
			if (length == 0) {
				continue;
			}
			if (length == keyExactLength) {
				map.put(line, value);
				++count;
				continue;
			}
			if (length > keyExactLength && -1 != " \r\t,".indexOf(line.charAt(keyExactLength))) {
				map.put(line.substring(0, keyExactLength), value);
				++count;
				continue;
			}
		}
		return count;
	}
}
