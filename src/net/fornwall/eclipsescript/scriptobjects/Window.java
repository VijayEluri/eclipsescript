package net.fornwall.eclipsescript.scriptobjects;

import net.fornwall.eclipsescript.util.EclipseUtils;
import net.fornwall.eclipsescript.util.JavaUtils.MutableObject;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Window {

	public static void alert(final String message) {
		EclipseUtils.runInDisplayThread(new Runnable() {
			public void run() {
				MessageDialog.openInformation(EclipseUtils.activeWindow().getShell(), "Script Alert", message);
			}
		}, true);
	}

	public static String prompt(final String message) {
		return prompt(message, "");
	}

	public static boolean confirm(final String message) {
		final MutableObject<Boolean> enteredText = new MutableObject<Boolean>();
		EclipseUtils.runInDisplayThread(new Runnable() {
			public void run() {
				enteredText.value = MessageDialog.openConfirm(EclipseUtils.getWindowShell(), "Script Prompt", message);
			}
		}, true);
		return enteredText.value;
	}

	public static String prompt(final String message, final String initialValue) {
		final MutableObject<String> enteredText = new MutableObject<String>();

		EclipseUtils.runInDisplayThread(new Runnable() {

			public void run() {
				final PromptDialog dialog = new PromptDialog(EclipseUtils.getWindowShell(), message);

				// create the window shell so the title can be set
				dialog.create();
				dialog.getShell().setText("Prompt");
				if (initialValue != null) {
					dialog.promptField.setText(initialValue);
				}
				// since the Window has the blockOnOpen property set to true, it
				// will dipose of the shell upon close
				if (dialog.open() == org.eclipse.jface.window.Window.OK) {
					enteredText.value = dialog.enteredText;
				}
			}
		}, true);

		return enteredText.value;
	}

	private static class PromptDialog extends Dialog {

		private final String promptText;
		Text promptField;
		String enteredText;

		public PromptDialog(Shell parentShell, String promptText) {
			super(parentShell);
			this.promptText = promptText;
		}

		@Override
		public boolean close() {
			enteredText = promptField.getText();
			return super.close();
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite container = (Composite) super.createDialogArea(parent);
			final GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			container.setLayout(gridLayout);

			final Label nameLabel = new Label(container, SWT.NONE);
			nameLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
			nameLabel.setText(promptText + ":");

			promptField = new Text(container, SWT.BORDER);
			promptField.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			promptField.setText("");

			return container;
		}

	}

}