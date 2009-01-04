
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

package net.usikkert.kouchat.misc;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.usikkert.kouchat.ui.UserInterface;
import net.usikkert.kouchat.util.Tools;

/**
 * Notifies the user interface when the day changes.
 * Checks every hour, in case daylight saving changes the time.
 *
 * @author Christian Ihle
 */
public class DayTimer extends TimerTask
{
	/**
	 * Which hour of the day the timer should notify about
	 * day change.
	 */
	private static final int NOTIFY_HOUR = 0;

	/**
	 * How often the timer should check if the day has changed,
	 * in milliseconds. Currently set to 1 hour.
	 */
	private static final long TIMER_INTERVAL = 1000 * 60 * 60;

	/** The controller for showing messages in the ui. */
	private final MessageController msgController;

	/** If the day changed check is done for the day. */
	private boolean done;

	/**
	 * Constructor. Starts the timer.
	 *
	 * @param ui The user interface.
	 */
	public DayTimer( final UserInterface ui )
	{
		msgController = ui.getMessageController();
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get( Calendar.HOUR_OF_DAY );

		// To stop the timer from thinking that the day has changed if
		// the application is started between 00 and 01 o'clock.
		if ( currentHour == NOTIFY_HOUR )
			done = true;

		cal.set( Calendar.HOUR_OF_DAY, NOTIFY_HOUR );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );

		Timer timer = new Timer( "DayTimer" );
		timer.scheduleAtFixedRate( this, new Date( cal.getTimeInMillis() ), TIMER_INTERVAL );
	}

	/**
	 * This method is run by the timer every hour, and
	 * compares the current time against the time when
	 * the day changes.
	 */
	@Override
	public void run()
	{
		int hour = Calendar.getInstance().get( Calendar.HOUR_OF_DAY );

		// Needs an extra check, so the message only shows once a day.
		if ( hour == NOTIFY_HOUR && !done )
		{
			String date = Tools.dateToString( null, "EEEE, d MMMM yyyy" );
			msgController.showSystemMessage( "Day changed to " + date );
			done = true;
		}

		else if ( hour != NOTIFY_HOUR && done )
		{
			done = false;
		}
	}
}
