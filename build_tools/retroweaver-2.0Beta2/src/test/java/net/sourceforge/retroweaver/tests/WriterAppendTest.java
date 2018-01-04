package net.sourceforge.retroweaver.tests;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public class WriterAppendTest extends AbstractTest {

	public void testAppendChar() {
		CharArrayWriter c = new CharArrayWriter();
		Writer w = c;
		try {
			w.append('a');
		} catch (IOException ioe) {
		}

		assertEquals("testAppendChar", "a", c.toString());
	}

	public void testAppendSequence() {
		CharSequence csq = "bcd";
		CharArrayWriter c = new CharArrayWriter();
		Writer w = c;
		try {
			w.append(csq);
			w.append(csq, 0, 2);
		} catch (IOException ioe) {
		}

		assertEquals("testAppendSequence", "bcdbc", c.toString());
	}

}
