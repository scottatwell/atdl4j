package org.atdl4j.ui.swt.impl;


import org.apache.log4j.Logger;
import org.atdl4j.fixatdl.layout.BorderT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

/**
 * 
 * Contains static SWT-specific methods to support building StrategyPanel container and related panel/composite redrawing.
 * 
 * Creation date: (Mar 3, 2010 7:35:04 AM)
 * @author Scott Atwell
 * @version 1.0, Mar 3, 2010
 */
public class SWTStrategyPanelHelper
{
	private static final Logger logger = Logger.getLogger( SWTStrategyPanelHelper.class );


	/**
	 * Invokes revalidateLayout() within Display.getCurrent().asyncExec(new Runnable() ... )
	 * @param aControl
	 */
	public static void revalidateLayoutAsync(Control aControl)
	{ 
		SWTRevalidateLayoutThread tempRevalidateLayoutThread = new SWTRevalidateLayoutThread( aControl );
		tempRevalidateLayoutThread.start();
	}
 
	/**
	 * Helper method contributed on web:  http://stackoverflow.com/questions/586414/why-does-an-swt-composite-sometimes-require-a-call-to-resize-to-layout-correctl
	 * @author http://stackoverflow.com/users/63293/peter-walser
	 * @param control
	 */
	public static void revalidateLayout(Control control)
	{ 
        Control c = control; 
        do 
        { 
          if (c instanceof ExpandBar) 
          { 
         	 ExpandBar expandBar = (ExpandBar) c; 
         	 for (ExpandItem expandItem : expandBar.getItems()) 
         	 { 
         		 expandItem.setHeight(expandItem.getControl().computeSize(expandBar.getSize().x, SWT.DEFAULT, true).y); 
         	 } 
          } 
          c = c.getParent(); 
        } while (c != null && c.getParent() != null && !(c instanceof ScrolledComposite)); 

        
        if (c instanceof ScrolledComposite) 
        { 
      	  ScrolledComposite scrolledComposite = (ScrolledComposite) c; 
      	  if (scrolledComposite.getExpandHorizontal() || scrolledComposite.getExpandVertical()) 
      	  { 
      		  scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT, true)); 
      	  } 
      	  else 
      	  { 
      		  scrolledComposite.getContent().pack(true); 
      	  } 
        } 
        
        if (c instanceof Composite) 
        { 
      	  Composite composite = (Composite) c; 
      	  composite.layout(true, true); 
        } 
	} 

	
	/**
	 * Builds the appropriate 'container' Composite for aStrategyPanel.
	 * 
	 * Supports:
	 * 	- aStrategyPanel.isCollapsible() returning ExpandBar (collapsed state based upon aStrategyPanel.isCollapsed())
	 * 	- aStrategyPanel.getTitle() returning Group
	 * 	- aStrategyPanel.getBorder() of BorderT.LINE returning Group (without Text)
	 * 	- default returns Composite
	 * 
	 * @param aStrategyPanel
	 * @param aParent
	 * @param aStyle
	 * @return
	 */
	public static Composite createStrategyPanelContainer( StrategyPanelT aStrategyPanel, Composite aParent, int aStyle )
	{
		Composite c;

			// -- Check for Collapsible --
		if ( aStrategyPanel.isCollapsible() )
		{
			ExpandBar tempExpandBar = new ExpandBar( aParent, SWT.NONE );
			tempExpandBar.setSpacing( 1 );
//TODO if want to get rid of white background, likely want a border around whole thing...
//TODO 	tempExpandBar.setBackground( aParent.getBackground() );

			if ( aParent.getLayout() instanceof GridLayout )
			{
				// mimics createStrategyPanelLayoutData() below
				GridData tempGridData = new GridData( SWT.FILL, SWT.FILL, true, false );
//				if ( aStrategyPanel.getOrientation() == PanelOrientationT.VERTICAL )
//				{
					tempGridData.horizontalSpan = 2;	// same as createStrategyPanelLayoutData
//				}
				tempGridData.horizontalAlignment = GridData.FILL;	// occupy the full available horizontal width
				
				tempExpandBar.setLayoutData( tempGridData );
			}
			
			Composite tempExpandBarComposite = new Composite( tempExpandBar, SWT.NONE );
			// "c" is 'standard' composite containing StrategyPanel as Data
			c = tempExpandBarComposite;

			// -- since we're using GridLayout, need to force the aParent with that layout to resize via aParent.layout() when ExpandBar button is expanded/collapsed --
			tempExpandBar.addExpandListener( new SWTExpandBarResizer( tempExpandBarComposite ) );
			
			ExpandItem tempExpandItem = new ExpandItem(tempExpandBar, SWT.NONE, 0);
			if ( aStrategyPanel.getTitle() != null )
			{
				tempExpandItem.setText( aStrategyPanel.getTitle() );
			}
			tempExpandItem.setControl(tempExpandBarComposite);

			// -- not very helpful in this context, we need revalidateLayout( c ) executed _after_ the "c" component is loaded with Controls --
			tempExpandItem.setHeight( tempExpandItem.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y );
			
			tempExpandItem.setExpanded( ! aStrategyPanel.isCollapsed() );
		}
			// -- Check for titled border --
		else if ( aStrategyPanel.getTitle() != null ) 
		{
			c = new Group( aParent, aStyle );
			( (Group) c ).setText( aStrategyPanel.getTitle() );
		}
			// -- Check for Line border (no title) --
		else if ( BorderT.LINE.equals( aStrategyPanel.getBorder() ) )
		{
			c = new Group( aParent, aStyle );
		}
		else
		{
			// -- normal, non-collapsible, non-bordered StrategyPanel --
			c = new Composite( aParent, aStyle );
		}

		// -- Set the layout and layoutData for the SWT Composite containing the set of FIXatdl Controls --
		c.setLayout( createStrategyPanelLayout( aStrategyPanel ) );
		c.setLayoutData( createStrategyPanelLayoutData( c ) );
		
		// -- Keep StrategyPanel in data object --
		c.setData( aStrategyPanel );

		return c;
	}

	/**
	 * Creates the layout for the SWT Composite containing the set of FIXatdl Controls
	 * 
	 * @param c
	 * @return
	 */
	protected static Layout createStrategyPanelLayout(StrategyPanelT panel)
	{
		PanelOrientationT orientation = panel.getOrientation();
		if ( orientation == PanelOrientationT.HORIZONTAL )
		{
			int parameterCount = panel.getControl() != null ? panel.getControl().size() : 0;
			int panelCount = panel.getStrategyPanel() != null ? panel.getStrategyPanel().size() : 0;

			// make 2 columns for each parameter and 1 column for each sub-panel
			GridLayout l = new GridLayout( parameterCount * 2 + panelCount, false );
			return l;
		}
		else if ( orientation == PanelOrientationT.VERTICAL )
		{
// TODO ???? WHY TWO VS. ONE ?????			GridLayout l = new GridLayout( 2, false );
//			GridLayout l = new GridLayout( 1, false );
			GridLayout l = new GridLayout( 2, false );
			return l;
		}
		return null;
	}
	
	/**
	 * Creates the layoutData for the SWT Composite containing the set of FIXatdl Controls
	 * 
	 * @param c
	 * @return
	 */
	protected static Object createStrategyPanelLayoutData(Composite c)
	{
		// if parent is a strategy panel
		if ( c.getParent().getData() instanceof StrategyPanelT )
		{
			GridData layoutData = new GridData( SWT.FILL, SWT.FILL, true, false );
			c.setLayoutData( layoutData );

			StrategyPanelT parentPanel = (StrategyPanelT) c.getParent().getData();
			// if parent orientation is vertical, make this panel span 2 columns
			if ( parentPanel.getOrientation() == PanelOrientationT.VERTICAL )
			{
				layoutData.horizontalSpan = 2;
			}
			return layoutData;
		}
// 3/2/2010 Scott Atwell added for ExpandBar
		else if ( (c.getParent() instanceof ExpandBar) && ( c.getData() instanceof StrategyPanelT ) )
		{
			GridData layoutData = new GridData( SWT.FILL, SWT.FILL, true, false );
			c.setLayoutData( layoutData );

			StrategyPanelT parentPanel = (StrategyPanelT) c.getData();
			// if parent orientation is vertical, make this panel span 2 columns
			if ( parentPanel.getOrientation() == PanelOrientationT.VERTICAL )
			{
				layoutData.horizontalSpan = 2;
			}
			return layoutData;
		}
		return null;
	}


}
