
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.usikkert.kouchat.Constants;
import net.usikkert.kouchat.event.SettingsListener;
import net.usikkert.kouchat.util.Tools;

/**
 * This is a singleton class that loads and saves the application settings to file.
 *
 * <p>These settings are persisted:</p>
 *
 * <ul>
 *   <li>Nick name</li>
 *   <li>Browser</li>
 *   <li>Enable sound</li>
 *   <li>Enable logging</li>
 *   <li>Enable debug</li>
 *   <li>Enable smileys</li>
 *   <li>Own message color</li>
 *   <li>System message color</li>
 *   <li>Chosen look and feel</li>
 * </ul>
 *
 * @author Christian Ihle
 */
public final class Settings
{
	/** The logger. */
	private static final Logger LOG = Logger.getLogger( Settings.class.getName() );

	/** The path to the file storing the settings. */
	private static final String FILENAME = Constants.APP_FOLDER + "kouchat.ini";

	/** The single instance of this class. */
	private static final Settings SETTINGS = new Settings();

	/** A list of listeners. These listeners are notified when a setting is changed. */
	private final List<SettingsListener> listeners;

	/** The error handler, for showing messages to the user. */
	private final ErrorHandler errorHandler;

	// The stored settings:

	/**
	 * The nick name of the application user. The rest of the values in <code>me</code>
	 * is generated in the constructor.
	 */
	private final User me;

	/** The color of the user's own messages. */
	private int ownColor;

	/** The color the system messages. */
	private int sysColor;

	/** If sound is enabled. */
	private boolean sound;

	/** If logging of the main chat is enabled. */
	private boolean logging;

	/** If smileys are enabled. */
	private boolean smileys;

	/** The choice of browser to open urls with. */
	private String browser;

	/** Name of the chosen look and feel. */
	private String lookAndFeel;

	/**
	 * Private constructor.
	 *
	 * Initializes default settings, and creates <code>me</code>.
	 */
	private Settings()
	{
		int code = 10000000 + (int) ( Math.random() * 9999999 );

		me = new User( createNickName( code ), code );
		me.setMe( true );
		me.setLastIdle( System.currentTimeMillis() );
		me.setLogonTime( System.currentTimeMillis() );
		me.setOperatingSystem( System.getProperty( "os.name" ) );
		me.setClient( Constants.APP_NAME + " v" + Constants.APP_VERSION
				+ " " + System.getProperty( Constants.PROPERTY_CLIENT_UI ) );

		listeners = new ArrayList<SettingsListener>();
		errorHandler = ErrorHandler.getErrorHandler();
		browser = "";
		ownColor = -15987646;
		sysColor = -16759040;
		sound = true;
		smileys = true;
		lookAndFeel = "";

		loadSettings();
	}

	/**
	 * Creates a new default nick name from the name of the user logged in to
	 * the operating system. The name is shortened to 10 characters and the
	 * first letter is capitalized.
	 *
	 * <p>If the name is invalid as a nick name then the user code is used instead.</p>
	 *
	 * @param code The user code.
	 * @return The created nick name.
	 */
	private String createNickName( final int code )
	{
		String userName = System.getProperty( "user.name" );

		if ( userName == null )
			return Integer.toString( code );

		String[] splitUserName = userName.split( " " );
		String defaultNick = Tools.capitalizeFirstLetter( Tools.shorten( splitUserName[0].trim(), 10 ) );

		if ( Tools.isValidNick( defaultNick ) )
			return defaultNick;

		return Integer.toString( code );
	}

	/**
	 * Static method to get the single instance of this class.
	 *
	 * @return The settings instance.
	 */
	public static Settings getSettings()
	{
		return SETTINGS;
	}

	/**
	 * Saves the current settings to file. Creates any missing folders
	 * or files.
	 */
	public void saveSettings()
	{
		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;

		File appFolder = new File( Constants.APP_FOLDER );

		if ( !appFolder.exists() )
			appFolder.mkdir();

		try
		{
			fileWriter = new FileWriter( FILENAME );
			buffWriter = new BufferedWriter( fileWriter );

			buffWriter.write( "nick=" + me.getNick() );
			buffWriter.newLine();
			buffWriter.write( "owncolor=" + ownColor );
			buffWriter.newLine();
			buffWriter.write( "syscolor=" + sysColor );
			buffWriter.newLine();
			buffWriter.write( "logging=" + logging );
			buffWriter.newLine();
			buffWriter.write( "sound=" + sound );
			buffWriter.newLine();
			// Properties does not support loading back slash, so replace with forward slash
			buffWriter.write( "browser=" + browser.replaceAll( "\\\\", "/" ) );
			buffWriter.newLine();
			buffWriter.write( "smileys=" + smileys );
			buffWriter.newLine();
			buffWriter.write( "lookAndFeel=" + lookAndFeel );
			buffWriter.newLine();
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString() );
			errorHandler.showError( "Settings could not be saved:\n " + e );
		}

		finally
		{
			try
			{
				if ( buffWriter != null )
					buffWriter.flush();
			}

			catch ( final IOException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}

			try
			{
				if ( fileWriter != null )
					fileWriter.flush();
			}

			catch ( final IOException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}

			try
			{
				if ( buffWriter != null )
					buffWriter.close();
			}

			catch ( final IOException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}

			try
			{
				if ( fileWriter != null )
					fileWriter.close();
			}

			catch ( final IOException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}
		}
	}

	/**
	 * Loads the settings from file.
	 * If some values are not found in the settings, the default is used instead.
	 */
	private void loadSettings()
	{
		FileInputStream fileStream = null;

		try
		{
			Properties fileContents = new Properties();
			fileStream = new FileInputStream( FILENAME );
			fileContents.load( fileStream );

			String tmpNick = fileContents.getProperty( "nick" );

			if ( tmpNick != null && Tools.isValidNick( tmpNick ) )
			{
				me.setNick( tmpNick.trim() );
			}

			try
			{
				ownColor = Integer.parseInt( fileContents.getProperty( "owncolor" ) );
			}

			catch ( final NumberFormatException e )
			{
				LOG.log( Level.WARNING, "Could not read setting for owncolor.." );
			}

			try
			{
				sysColor = Integer.parseInt( fileContents.getProperty( "syscolor" ) );
			}

			catch ( final NumberFormatException e )
			{
				LOG.log( Level.WARNING, "Could not read setting for syscolor.." );
			}

			logging = Boolean.valueOf( fileContents.getProperty( "logging" ) );
			browser = fileContents.getProperty( "browser" );
			lookAndFeel = fileContents.getProperty( "lookAndFeel" );

			if ( fileContents.getProperty( "sound" ) != null ) // Defaults to true
				sound = Boolean.valueOf( fileContents.getProperty( "sound" ) );

			if ( fileContents.getProperty( "smileys" ) != null ) // Defaults to true
				smileys = Boolean.valueOf( fileContents.getProperty( "smileys" ) );
		}

		catch ( final FileNotFoundException e )
		{
			LOG.log( Level.WARNING, "Could not find " + FILENAME + ", using default settings." );
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString(), e );
		}

		finally
		{
			try
			{
				if ( fileStream != null )
					fileStream.close();
			}

			catch ( final IOException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}
		}
	}

	/**
	 * Gets the application user.
	 *
	 * @return The application user.
	 */
	public User getMe()
	{
		return me;
	}

	/**
	 * Gets the color used for the user's own messages.
	 *
	 * @return The color for own messages.
	 */
	public int getOwnColor()
	{
		return ownColor;
	}

	/**
	 * Sets the color used for the user's own messages.
	 * Listeners are notified of the change.
	 *
	 * @param ownColor The color for own messages.
	 */
	public void setOwnColor( final int ownColor )
	{
		if ( this.ownColor != ownColor )
		{
			this.ownColor = ownColor;
			fireSettingChanged( "ownColor" );
		}
	}

	/**
	 * Gets the color used for system messages.
	 *
	 * @return The color for system messages.
	 */
	public int getSysColor()
	{
		return sysColor;
	}

	/**
	 * Sets the color used for system messages.
	 * Listeners are notified of the change.
	 *
	 * @param sysColor The color for system messages.
	 */
	public void setSysColor( final int sysColor )
	{
		if ( this.sysColor != sysColor )
		{
			this.sysColor = sysColor;
			fireSettingChanged( "sysColor" );
		}
	}

	/**
	 * Checks if sound is enabled.
	 *
	 * @return If sound is enabled.
	 */
	public boolean isSound()
	{
		return sound;
	}

	/**
	 * Sets if sound is enabled.
	 * Listeners are notified of the change.
	 *
	 * @param sound If sound is enabled.
	 */
	public void setSound( final boolean sound )
	{
		if ( this.sound != sound )
		{
			this.sound = sound;
			fireSettingChanged( "sound" );
		}
	}

	/**
	 * Checks if logging is enabled.
	 *
	 * @return If logging is enabled.
	 */
	public boolean isLogging()
	{
		return logging;
	}

	/**
	 * Sets if logging is enabled.
	 * Listeners are notified of the change.
	 *
	 * @param logging If logging is enabled.
	 */
	public void setLogging( final boolean logging )
	{
		if ( this.logging != logging )
		{
			this.logging = logging;
			fireSettingChanged( "logging" );
		}
	}

	/**
	 * Gets the chosen browser for opening urls.
	 *
	 * @return The chosen browser.
	 */
	public String getBrowser()
	{
		return browser;
	}

	/**
	 * Sets the chosen browser for opening urls.
	 *
	 * @param browser The chosen browser.
	 */
	public void setBrowser( final String browser )
	{
		this.browser = browser;
	}

	/**
	 * Checks if smileys are enabled.
	 *
	 * @return If smileys are enabled.
	 */
	public boolean isSmileys()
	{
		return smileys;
	}

	/**
	 * Sets if smileys are enabled.
	 *
	 * @param smileys If smileys are enabled.
	 */
	public void setSmileys( final boolean smileys )
	{
		this.smileys = smileys;
	}

	/**
	 * Gets the chosen look and feel.
	 *
	 * @return The chosen look and feel.
	 */
	public String getLookAndFeel()
	{
		return lookAndFeel;
	}

	/**
	 * Sets the chosen look and feel.
	 *
	 * @param lookAndFeel The chosen look and feel.
	 */
	public void setLookAndFeel( final String lookAndFeel )
	{
		this.lookAndFeel = lookAndFeel;
	}

	/**
	 * Notifies the listeners that <code>setting</code> has changed.
	 *
	 * @param setting The setting that has changed.
	 */
	private void fireSettingChanged( final String setting )
	{
		for ( SettingsListener listener : listeners )
		{
			listener.settingChanged( setting );
		}
	}

	/**
	 * Adds a listener for changes to the settings.
	 *
	 * @param listener The listener to add.
	 */
	public void addSettingsListener( final SettingsListener listener )
	{
		listeners.add( listener );
	}

	/**
	 * Removes a listener for changes to the settings.
	 *
	 * @param listener The listener to remove.
	 */
	public void removeSettingsListener( final SettingsListener listener )
	{
		listeners.remove( listener );
	}
}
