package org.hamnett.adm.gui;

import java.util.regex.Pattern;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import org.hamnett.adm.Main;
import org.hamnett.adm.TrayAction;
import org.hamnett.adm.gui.dialog.LightSlider;
import org.hamnett.adm.lightwaverf.LwrfConfiguration;

import com.Lightwave.LightwaveAPI;
import com.Lightwave.SendUDP;

public class LightDimAction extends Action implements TrayAction
	{
	    //private InputDialog dialDialog;
	    private LightSlider lightDialog;
	    private LwrfConfiguration configuration;
	    

	    /**
	     * Creates a new QuickDialAction
	     * 
	     * @param asterisk the Asterisk to use
	     */
	    public LightDimAction(LwrfConfiguration lwc)
	    {
	       // super(Messages.getString("LightSlider.label"), //$NON-NLS-1$ 
	        //        ImageDescriptor.createFromFile(LightDimAction.class,
	          //              "icons/dial.png")); //$NON-NLS-1$
	        super(Messages.getString("LightSlider.label")); //$NON-NLS-1$
	    	configuration = lwc;
	    }


		public void runWithEvent(Event event)
	    {
	        InputDialog dialog;
	        String defaultValue = null;

	        Shell shell = (lightDialog != null) ? lightDialog.getShell() : null;
	        
	        if (shell != null && shell.isVisible())
	        {
	            shell.setActive();

	            return;
	        }
	      
	        LightSlider dialog2 = new LightSlider(Main.getShell(), configuration.getBrowserPath());
	        lightDialog = dialog2;

	        if (dialog2.open() == Window.OK)
	        {
	        	LightwaveAPI lw = new LightwaveAPI();
	        	lw.sendDeviceDim(lightDialog.getRoom(), lightDialog.getDevice(), lightDialog.getValue());
	            lightDialog = null;
	        }
	        
	    }

	    /**
	     * Returns the contents of the clipboard.
	     * 
	     * @param display the display that owns the clipboard
	     * @return the contents of the clipboard if it's a text or <code>null</code>
	     *         if it is binary or empty.
	     */
	    private String getClipboardText(Display display)
	    {
	        Clipboard clipboard;
	        TextTransfer transfer;

	        clipboard = new Clipboard(display);
	        transfer = TextTransfer.getInstance();
	        return (String) clipboard.getContents(transfer);
	    }
	}
