package com.ndmsystems.ndmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.reflect.ReflectionContextArgument;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx */
@ReflectionManual
public class NdmUtilsStatic {
	
	/** @param process
	 * @param binary
	 *            - source binary with text lines
	 * @param map
	 *            - target map, <String, Object>
	 * @param keyExactLength
	 *            - the exact length of the key column
	 * @param value
	 *            - value to use for items put in map
	 * @param errorTemplate
	 *            - null or error template for invalid lines (must contain one '%s')
	 * @return
	 * @throws IOException
	 * @throws ConcurrentModificationException */
	@ReflectionExplicit
	@ReflectionContextArgument
	public static final int readTextFirstColumnToMapKeys(//
			final ExecProcess process,
			final TransferCopier binary,
			final Map<String, Object> map,
			final int keyExactLength,
			final BaseObject value,
			final String errorTemplate//
	) throws ConcurrentModificationException, IOException {
		
		int count = 0;
		
		try (final BufferedReader reader = new BufferedReader(binary.nextReaderUtf8())) {
			for (;;) {
				final String line;
				try {
					// line = scanner.next();
					line = reader.readLine();
				} catch (final IOException e) {
					// error
					return count;
				}
				if (line == null) {
					// end of file
					return count;
				}
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
				if (!line.isBlank() && null != errorTemplate) {
					process.getConsole().error(errorTemplate, line);
				}
			}
		}
	}
}
