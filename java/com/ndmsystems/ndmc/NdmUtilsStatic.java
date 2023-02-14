package com.ndmsystems.ndmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
		
		final long binaryLength = binary.length();
		
		if (binaryLength < 24L * 1024L) {
			try (final Scanner scanner = binaryLength < 12L * 1024L
				? new Scanner(binary.toStringUtf8())
				: new Scanner(binary.nextReaderUtf8());) {
				// scanner.useDelimiter("\n");
				
				for (;;) {
					// if (!scanner.hasNext()) {
					if (!scanner.hasNextLine()) {
						return count;
					}
					// final String line = scanner.next();
					final String line = scanner.nextLine();
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
					if (line.trim().length() != 0 && null != errorTemplate) {
						process.getConsole().error(errorTemplate, line);
					}
				}
			}
		}
		
		try (final Scanner scanner = new Scanner(new BufferedReader(binary.nextReaderUtf8()));) {
			// scanner.useDelimiter("\n");
			
			for (;;) {
				final String line;
				try {
					// line = scanner.next();
					line = scanner.nextLine();
				} catch (final NoSuchElementException e) {
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
				if (line.trim().length() != 0 && null != errorTemplate) {
					process.getConsole().error(errorTemplate, line);
				}
			}
		}
	}
}
