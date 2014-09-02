package org.hamnett.adm.util;

/**
 * Created by IntelliJ IDEA.
 * User: rick
 * Date: Jun 15, 2006
 * Time: 11:00:53 PM
 *
 * @author rick
 * @version $Id: PasswordFieldEditor.java,v 1.1 2006-06-15 22:44:23 rick Exp $
 */
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * A field editor for a password type preference. This is essentially
 * a StringFieldEditor, but will replace each character in the input
 * box with an '*'.
 * <p>
 * This class may be used as is, or subclassed as required.
 * </p>
 */
public class PasswordFieldEditor extends StringFieldEditor {

    /**
     * The validation strategy;
     * <code>VALIDATE_ON_KEY_STROKE</code> by default.
     */
    private int validateStrategy = VALIDATE_ON_KEY_STROKE;

    /**
     * Text limit of text field in characters; initially unlimited.
     */
    private int textLimit = UNLIMITED;

    /**
     * The text field, or <code>null</code> if none.
     */
    Text textField;

    /**
     * Creates a new password field editor
     */
    protected PasswordFieldEditor() {
    }

    /**
     * Creates a password field editor.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param width the width of the text input field in characters,
     *  or <code>UNLIMITED</code> for no limit
     * @param strategy either <code>VALIDATE_ON_KEY_STROKE</code> to perform
     *  on the fly checking (the default), or <code>VALIDATE_ON_FOCUS_LOST</code> to
     *  perform validation only after the text has been typed in
     * @param parent the parent of the field editor's control
     */
    public PasswordFieldEditor(String name, String labelText, int width,
            int strategy, Composite parent) {
        super(name, labelText, width, strategy, parent);
    }

    /**
     * Creates a password field editor.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param width the width of the text input field in characters,
     *  or <code>UNLIMITED</code> for no limit
     * @param parent the parent of the field editor's control
     */
    public PasswordFieldEditor(String name, String labelText, int width,
            Composite parent) {
        this(name, labelText, width, VALIDATE_ON_KEY_STROKE, parent);
    }

    /**
     * Creates a password field editor of unlimited width.
     * Use the method <code>setTextLimit</code> to limit the text.
     *
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public PasswordFieldEditor(String name, String labelText, Composite parent) {
        this(name, labelText, UNLIMITED, parent);
    }



	/**
     * Returns this field editor's text control.
     * <p>
     * The control is created if it does not yet exist
     * </p>
     *
     * @param parent the parent
     * @return the text control
     */
    public Text getTextControl(Composite parent) {
        if (textField == null) {
            textField = new Text(parent, SWT.PASSWORD | SWT.SINGLE | SWT.BORDER);
            textField.setFont(parent.getFont());
            switch (validateStrategy) {
            case VALIDATE_ON_KEY_STROKE:
                textField.addKeyListener(new KeyAdapter() {

                    /* (non-Javadoc)
                     * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
                     */
                    public void keyReleased(KeyEvent e) {
                        valueChanged();
                    }
                });

                break;
            case VALIDATE_ON_FOCUS_LOST:
                textField.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        clearErrorMessage();
                    }
                });
                textField.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) {
                        refreshValidState();
                    }

                    public void focusLost(FocusEvent e) {
                        valueChanged();
                        clearErrorMessage();
                    }
                });
                break;
            default:
                Assert.isTrue(false, "Unknown validate strategy");//$NON-NLS-1$
            }
            textField.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent event) {
                    textField = null;
                }
            });
            if (textLimit > 0) {//Only set limits above 0 - see SWT spec
                textField.setTextLimit(textLimit);
            }
        } else {
            checkParent(textField, parent);
        }
        return textField;
    }

}
