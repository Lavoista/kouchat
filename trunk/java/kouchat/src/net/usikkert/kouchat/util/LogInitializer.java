
/***************************************************************************
 *   Copyright 2006-2009 by Christian Ihle                                 *
 *   kontakt@usikkert.net                                                  *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

package net.usikkert.kouchat.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class initializes log settings for the application.
 *
 * <p>The default level for output to the console is <code>INFO</code>,
 * but any level can be set using the <code>java.util.logging</code> JMX MBean.</p>
 *
 * @author Christian Ihle
 */
public final class LogInitializer
{
	/**
	 * Constructor that initializes the logging.
	 */
	public LogInitializer()
	{
		initHandlers();
		initParentLoggers();
	}

	/**
	 * Creates loggers for important packages, to make
	 * it easier to change settings for a group of loggers.
	 */
	public void initParentLoggers()
	{
		final String mainPackage = "net.usikkert.kouchat";

		Logger.getLogger( mainPackage );
		Logger.getLogger( mainPackage + ".misc" );
		Logger.getLogger( mainPackage + ".net" );
		Logger.getLogger( mainPackage + ".ui" );
		Logger.getLogger( mainPackage + ".ui.console" );
		Logger.getLogger( mainPackage + ".ui.swing" );
		Logger.getLogger( mainPackage + ".util" );
	}

	/**
	 * Enable logging of all levels with the console handler.
	 *
	 * <br /><br />
	 *
	 * This is important, because the console handler level is
	 * set to <code>INFO</code> by default, which overrides the normal
	 * logger level. So changing the logger level with the JMX MBean has no effect
	 * without this change.
	 */
	public void initHandlers()
	{
		Handler[] handlers = Logger.getLogger( "" ).getHandlers();

		for ( Handler handler : handlers )
		{
			if ( handler instanceof ConsoleHandler )
			{
				handler.setLevel( Level.ALL );
				break;
			}
		}
	}
}
