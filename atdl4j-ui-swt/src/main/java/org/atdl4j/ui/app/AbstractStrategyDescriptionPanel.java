package org.atdl4j.ui.app;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.fixatdl.core.StrategyT;

/**
 * Represents the base, non-GUI system-specific Strategy Description GUI component.
 * 
 * Creation date: (Feb 26, 2010 11:09:19 PM)
 * @author Scott Atwell
 * @version 1.0, Mar 1, 2010
 */
public abstract class AbstractStrategyDescriptionPanel
		implements StrategyDescriptionPanel
{
	private Atdl4jConfig atdl4jConfig = null;

	abstract protected void setStrategyDescriptionText( String aText );

	/* 
	 * Loads aStrategy.getDescription() if populated and if Atdl4jConfig.isShowStrategyDescription(), 
	 * otherwise invokes setVisible( false ).
	 * 
	 * @param aStrategy
	 */
	public void loadStrategyDescription( StrategyT aStrategy )
	{
		if ( ( getAtdl4jConfig() != null ) && ( getAtdl4jConfig().isShowStrategyDescription() ) )
		{
			if ( ( aStrategy != null ) && ( aStrategy.getDescription() != null ) )
			{
				setStrategyDescriptionText( aStrategy.getDescription() );
				setVisible( true );
			}
			else
			{
				setStrategyDescriptionText( "" );
				setVisible( false );
			}
		}
		else
		{
			setVisible( false );
		}
	}

	/**
	 * @param atdl4jConfig the atdl4jConfig to set
	 */
	protected void setAtdl4jConfig(Atdl4jConfig atdl4jConfig)
	{
		this.atdl4jConfig = atdl4jConfig;
	}


	/**
	 * @return the atdl4jConfig
	 */
	public Atdl4jConfig getAtdl4jConfig()
	{
		return atdl4jConfig;
	}
}