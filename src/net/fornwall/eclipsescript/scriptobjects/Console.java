package net.fornwall.eclipsescript.scriptobjects;

import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class Console {

	// just a marker superclass for enablement in console closer, see plugin.xml
	public static class ConsoleClass extends MessageConsole {
		public ConsoleClass(String name, ImageDescriptor imageDescriptor) {
			super(name, imageDescriptor);
		}
	}

	private MessageConsoleStream out;
	private MessageConsole console;
	private String name;

	private void init() {
		if (console == null) {
			console = new ConsoleClass(name, null);
			out = console.newMessageStream();
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
			ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
		}
	}

	public Console(String name) {
		this.name = name;
	}

	public void println(final String msg) {
		init();
		out.println(msg);
	}

	public void print(final String msg) {
		init();
		out.print(msg);
	}

	public void clear() {
		if (console == null)
			return;

		console.clearConsole();
		try {
			out.close();
		} catch (final IOException e) {
			// not so interesting
		}
		out = console.newMessageStream();
	}
}