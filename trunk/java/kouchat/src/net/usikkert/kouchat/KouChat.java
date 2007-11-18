
/***************************************************************************
 *   Copyright 2006-2007 by Christian Ihle                                 *
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

package net.usikkert.kouchat;

import net.usikkert.kouchat.ui.UIException;
import net.usikkert.kouchat.ui.UIFactory;

/**
 * This class contains KouChat's main method.
 * 
 * It prints out some information at the console, and
 * parses the arguments, if any.
 * 
 * Two different User Interfaces can be loaded from here.
 * Swing is the default, and a console version can be loaded
 * by using the --console argument.
 * 
 * @author Christian Ihle
 */
public class KouChat
{
	/**
	 * The main method.
	 * 
	 * Takes two different arguments:<br />
	 * --help, shows information about available commands.<br />
	 * --console, starts KouChat in console mode.
	 * 
	 * @param args The arguments given when starting KouChat.
	 */
	public static void main( String[] args )
	{
		System.out.println( Constants.APP_NAME + " v" + Constants.APP_VERSION );
		System.out.println( "By " + Constants.AUTHOR_NAME + " - " + Constants.AUTHOR_MAIL + " - " + Constants.APP_WEB );
		
		if ( args.length == 0 )
			System.out.println( "Use --help for more information..." );
		
		boolean swing = true;
		boolean help = false;

		for ( int i = 0; i < args.length; i++ )
		{
			if ( args[i].equals( "--console" ) )
				swing = false;
			
			else if ( args[i].equals( "--help" ) )
				help = true;
			
			else
			{
				System.out.println( "\nUnknown argument '" + args[i] + "'. Use --help for more information..." );
				return;
			}
		}
		
		if ( help )
		{
			System.out.println( "\nCommands:" +
					"\n --help \tshows this help message" +
					"\n --console \tstarts " + Constants.APP_NAME + " in console mode" );
			return;
		}

		try
		{
			if ( swing )
			{
				System.out.println( "\nLoading Swing User Interface...\n" );
				new UIFactory().loadUI( "swing" );
			}
			
			else
			{
				System.out.println( "\nLoading Console User Interface...\n" );
				new UIFactory().loadUI( "console" );
			}
		}

		catch ( UIException e )
		{
			System.err.println( e );
		}
	}
}
