
/***************************************************************************
 *   Copyright 2006-2008 by Christian Ihle                                 *
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

import net.usikkert.kouchat.Constants;

/**
 * This class has a list of all the loggers used in the application.
 *
 * <p>Classes that need logging should use one of the loggers here
 * instead of creating a new class specific logger. That keeps the number of
 * loggers down, their usage known, and easier to administer at runtime.</p>
 *
 * <p>The default level for output to the console is <code>INFO</code>,
 * but any level can be set using the <code>java.util.logging</code> JMX MBean.</p>
 *
 * @author Christian Ihle
 */
public final class Loggers
{
	/** The root name of the loggers used in this application. */
	private static final String LOG_ROOT = Constants.APP_NAME.toLowerCase();

	/** The logger used in the network package. */
	public static final Logger NETWORK_LOG = Logger.getLogger( LOG_ROOT + ".network" );

	/** The logger used for messages in the network package. */
	public static final Logger MESSAGE_LOG = Logger.getLogger( NETWORK_LOG.getName() + ".messages" );

	/** The logger used in the utility package. */
	public static final Logger UTIL_LOG = Logger.getLogger( LOG_ROOT + ".util" );

	/** The logger used in the miscellaneous package. */
	public static final Logger MISC_LOG = Logger.getLogger( LOG_ROOT + ".misc" );

	/** The logger used in the user interface package. */
	public static final Logger UI_LOG = Logger.getLogger( LOG_ROOT + ".ui" );

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
	static
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

	/**
	 * Private constructor, because this class is for static use.
	 */
	private Loggers()
	{

	}
}