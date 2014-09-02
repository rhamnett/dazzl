package org.hamnett.adm.gui.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.yaml.snakeyaml.Yaml;

public class LightSlider  extends Dialog{


	private int value;
	private int room;
	private int device;
	private String filePath;
	
	public int getRoom() {
		return room;
	}

	public int getDevice() {
		return device;
	}

	private int ret = -1;

	/**
	 * @param parent
	 */
	public LightSlider(Shell parent) {
		super(parent);
	}
	
	/**
	 * @param parent
	 */
	public LightSlider(Shell parent, String fp) {
		super(parent);
		filePath =fp;
	}



	/**
	 * Makes the dialog visible.
	 * 
	 * @return
	 */
	public int open() {

		Shell parent = getParent();

		File newConfiguration = new File(filePath);
		InputStream is = null;
		try {
			is = new FileInputStream(newConfiguration);
		} catch (FileNotFoundException e) {
			MessageDialog.openError(parent, "Error", "YAML not found");
			return -1;			//e.printStackTrace();
		}

		Yaml yaml = new Yaml();
		@SuppressWarnings("unchecked")
		Map<String, ArrayList> yamlParsers = (Map<String, ArrayList>) yaml
		.load(is);
		ArrayList rooms = (ArrayList)yamlParsers.get("room");



		final Shell shell =
				new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.setSize(245, 205);

		bringToFront(shell);

		Point pt = parent.getDisplay().getCursorLocation ();
		shell.setLocation (pt.x, pt.y+18);
		shell.setAlpha(240);

		GridLayout gl = new GridLayout(3, false);
		gl.marginTop = 5;
		gl.marginBottom = 5;
		gl.marginLeft = 5;
		gl.marginRight = 5;

		shell.setLayout(gl);
		
		
				final Tree tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL| SWT.SINGLE);
				tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
				
						tree.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) {
								TreeItem ti = (TreeItem) e.item;
								Object dev = ti.getData();
								Object rm =null;
								if(ti.getParentItem()!=null){
									rm = ti.getParentItem().getData();
								}
								if(dev instanceof Integer) device = (Integer) dev;
								if(rm instanceof Integer) room = (Integer) rm;
								System.out.println("Room "+room+" Device "+device);
							}
						});

		for (int i = 0; i < rooms.size(); i++) {
			Map<String, ArrayList<String>> room = (LinkedHashMap<String, ArrayList<String>>)rooms.get(i);
			ArrayList<String> devices = room.get("device");
			System.out.println("Room: "+i+" name: "+room.get("name"));
			TreeItem item = new TreeItem(tree, SWT.LEFT);
			item.setText("R"+(i+1));
			item.setData((i+1));
			for(int d = 0; d < devices.size(); d++) {
				String device = devices.get(d);
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setText(device);
				subItem.setData((d+1));
			}			
		}

		Label lblOff = new Label(shell, SWT.NULL);
		lblOff.setText("0%");

		final Slider slider = new Slider(shell, SWT.NONE);
		slider.setMinimum(0);
		slider.setThumb(20);
		slider.setMaximum(100 + slider.getThumb());

		/*
		 * 
		 * 
		 * 
		 *  Mouse movement
		 */
		Listener l = new Listener() {
			Point origin;
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.MouseDown :
					origin= new Point(e.x, e.y);
					break;
				case SWT.MouseUp :
					origin= null;
					break;
				case SWT.MouseMove :
					if (origin != null) {
						Point p= shell.getDisplay().map(shell, null, e.x, e.y);
						shell.setLocation(p.x - origin.x, p.y - origin.y);
					}
					break;
				}
			}
		};



		slider.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				System.out.println(slider.getSelection());
				value = slider.getSelection();
				ret = 0;
				shell.dispose();
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		slider.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {

			}

			public void keyReleased(KeyEvent arg0) {
				if (arg0.character == SWT.CR) {
					ret = 0;
					shell.dispose();
				}
				if (arg0.character == SWT.ESC) {
					ret = -1;
					shell.dispose();
				}
			}
		});
		shell.addListener(SWT.MouseDown, l);
		shell.addListener(SWT.MouseUp, l);
		shell.addListener(SWT.MouseMove, l);

		shell.addListener(SWT.FocusOut,new Listener() {

	        @Override
	        public void handleEvent(Event e) {
	            System.out.println("focused: " + e.text);
	        }
	    });

		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});
		
		
				Label label = new Label(shell, SWT.NONE);
				label.setText("100%");

		shell.pack();
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return ret;
	}



	private void bringToFront(final Shell shell) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				shell.forceActive();
			}
		});
	}


	public static void main(String[] args) {
		Shell shell = new Shell();
		LightSlider dialog = new LightSlider(shell);
		System.out.println(dialog.open());
		System.out.println(dialog.getValue());
	}

	public int getValue() {
		return value;
	}

	public Shell getShell() {
		return getParent();
	}


}
