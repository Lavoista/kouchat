
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

import java.io.File;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import net.usikkert.kouchat.Constants;
import net.usikkert.kouchat.net.FileReceiver;
import net.usikkert.kouchat.net.FileSender;
import net.usikkert.kouchat.net.FileTransfer;
import net.usikkert.kouchat.net.TransferList;
import net.usikkert.kouchat.ui.UserInterface;
import net.usikkert.kouchat.util.Tools;

/**
 * Parses and executes commands. A command starts with a slash, and can
 * have arguments.
 *
 * @author Christian Ihle
 */
public class CommandParser
{
	private final Controller controller;
	private final UserInterface ui;
	private final MessageController msgController;
	private final User me;
	private final TransferList tList;

	/**
	 * Constructor.
	 *
	 * @param controller The controller.
	 * @param ui The user interface.
	 */
	public CommandParser( final Controller controller, final UserInterface ui )
	{
		this.controller = controller;
		this.ui = ui;

		msgController = ui.getMessageController();
		me = Settings.getSettings().getMe();
		tList = controller.getTransferList();
	}

	/**
	 * Command: <em>/topic &lt;optional new topic&gt;</em>.
	 * Prints the current topic if no arguments are supplied,
	 * or changes the topic. To remove the topic, use a space as the argument.
	 *
	 * @param args Nothing, or the new topic.
	 */
	private void cmdTopic( final String args )
	{
		if ( args.length() == 0 )
		{
			Topic topic = controller.getTopic();

			if ( topic.getTopic().equals( "" ) )
			{
				msgController.showSystemMessage( "No topic set" );
			}

			else
			{
				String date = Tools.dateToString( new Date( topic.getTime() ), "HH:mm:ss, dd. MMM. yy" );
				msgController.showSystemMessage( "Topic is: " + topic.getTopic() + " (set by " + topic.getNick() + " at " + date + ")" );
			}
		}

		else
		{
			try
			{
				fixTopic( args );
			}

			catch ( final CommandException e )
			{
				msgController.showSystemMessage( e.getMessage() );
			}
		}
	}

	/**
	 * Command: <em>/away &lt;away message&gt;</em>.
	 * Set status to away.
	 *
	 * @param args The away message.
	 */
	private void cmdAway( final String args )
	{
		if ( me.isAway() )
		{
			msgController.showSystemMessage( "/away - you are already away: '" + me.getAwayMsg() + "'" );
		}

		else
		{
			if ( args.trim().length() == 0 )
			{
				msgController.showSystemMessage( "/away - missing argument <away message>" );
			}

			else
			{
				try
				{
					controller.changeAwayStatus( me.getCode(), true, args.trim() );
					ui.changeAway( true );
					msgController.showSystemMessage( "You went away: " + me.getAwayMsg() );
				}

				catch ( final CommandException e )
				{
					msgController.showSystemMessage( e.getMessage() );
				}
			}
		}
	}

	/**
	 * Command: <em>/back</em>.
	 * Set status to not away.
	 */
	private void cmdBack()
	{
		if ( me.isAway() )
		{
			try
			{
				controller.changeAwayStatus( me.getCode(), false, "" );
				ui.changeAway( false );
				msgController.showSystemMessage( "You came back" );
			}

			catch ( final CommandException e )
			{
				msgController.showSystemMessage( e.getMessage() );
			}
		}

		else
		{
			msgController.showSystemMessage( "/back - you are not away" );
		}
	}

	/**
	 * Command: <em>/clear</em>.
	 * Clear all the text from the chat.
	 */
	private void cmdClear()
	{
		ui.clearChat();
	}

	/**
	 * Command: <em>/about</em>.
	 * Show information about the application.
	 */
	private void cmdAbout()
	{
		msgController.showSystemMessage( "This is " + Constants.APP_NAME + " v" + Constants.APP_VERSION
				+ ", by " + Constants.AUTHOR_NAME + " - " + Constants.AUTHOR_MAIL
				+ " - " + Constants.APP_WEB );
	}

	/**
	 * Command: <em>/help</em>.
	 * Shows a list of commands.
	 */
	private void cmdHelp()
	{
		showCommands();
	}

	/**
	 * Command: <em>/whois &lt;nick&gt;</em>.
	 * Show information about a user.
	 *
	 * @param args The user to show information about.
	 */
	private void cmdWhois( final String args )
	{
		if ( args.trim().length() == 0 )
		{
			msgController.showSystemMessage( "/whois - missing argument <nick>" );
		}

		else
		{
			String[] argsArray = args.split( "\\s" );
			String nick = argsArray[1].trim();

			User user = controller.getUser( nick );

			if ( user == null )
			{
				msgController.showSystemMessage( "/whois - no such user '" + nick + "'" );
			}

			else
			{
				String info = "/whois - " + user.getNick();

				if ( user.isAway() )
					info += " (Away)";

				info += ":\nIP address: " + user.getIpAddress();

				if ( user.getHostName() != null )
					info +=  "\nHost name: " + user.getHostName();

				info += "\nClient: " + user.getClient()
						+ "\nOperating System: " + user.getOperatingSystem()
						+ "\nOnline: " + Tools.howLongFromNow( user.getLogonTime() );

				if ( user.isAway() )
					info += "\nAway message: " + user.getAwayMsg();

				msgController.showSystemMessage( info );
			}
		}
	}

	/**
	 * Command: <em>/send &lt;nick&gt; &lt;file&gt;</em>.
	 * Send a file to a user.
	 *
	 * @param args First argument is the user to send to, and the second is
	 * the file to send to the user.
	 */
	private void cmdSend( final String args )
	{
		String[] argsArray = args.split( "\\s" );

		if ( argsArray.length <= 2 )
		{
			msgController.showSystemMessage( "/send - missing arguments <nick> <file>" );
		}

		else
		{
			String nick = argsArray[1];
			User user = controller.getUser( nick );

			if ( user != me )
			{
				if ( user == null )
				{
					msgController.showSystemMessage( "/send - no such user '" + nick + "'" );
				}

				else
				{
					String file = "";

					for ( int i = 2; i < argsArray.length; i++ )
					{
						file += argsArray[i] + " ";
					}

					file = file.trim();
					File sendFile = new File( file );

					if ( sendFile.exists() && sendFile.isFile() )
					{
						try
						{
							sendFile( user, sendFile );
						}

						catch ( final CommandException e )
						{
							msgController.showSystemMessage( e.getMessage() );
						}
					}

					else
					{
						msgController.showSystemMessage( "/send - no such file '" + file + "'" );
					}
				}
			}

			else
			{
				msgController.showSystemMessage( "/send - no point in doing that!" );
			}
		}
	}

	/**
	 * Command: <em>/receive &lt;nick&gt; &lt;file&gt;</em>.
	 * Accept a file transfer request from a user and start the transfer.
	 *
	 * @param args First argument is the other user in the file transfer,
	 * and the second is the file being transferred.
	 */
	private void cmdReceive( final String args )
	{
		String[] argsArray = args.split( "\\s" );

		if ( argsArray.length <= 2 )
		{
			msgController.showSystemMessage( "/receive - missing arguments <nick> <file>" );
			return;
		}

		String nick = argsArray[1];
		User user = controller.getUser( nick );

		if ( user == null )
		{
			msgController.showSystemMessage( "/receive - no such user '" + nick + "'" );
			return;
		}

		if ( user == me )
		{
			msgController.showSystemMessage( "/receive - no point in doing that!" );
			return;
		}

		String filename = "";

		for ( int i = 2; i < argsArray.length; i++ )
		{
			filename += argsArray[i] + " ";
		}

		filename = filename.trim();
		FileReceiver fileReceiver = tList.getFileReceiver( user, filename );

		if ( fileReceiver == null )
		{
			msgController.showSystemMessage( "/receive - no such file '" + filename + "' offered by " + nick );
			return;
		}

		if ( fileReceiver.isAccepted() )
		{
			msgController.showSystemMessage( "/receive - already receiving '" + filename + "' from " + nick );
			return;
		}

		File file = fileReceiver.getFile();

		if ( file.exists() )
		{
			int counter = 1;
			File newFile = null;

			do
			{
				String newName = file.getParent() + File.separator + filename + "." + counter;
				newFile = new File( newName );
				counter++;
			}
			while ( newFile.exists() );

			msgController.showSystemMessage( "/receive - file '" + filename + "' already exists - renaming to '" + newFile.getName() + "'" );
			fileReceiver.setFile( newFile );
		}

		fileReceiver.accept();
	}

	/**
	 * Command: <em>/reject &lt;nick&gt; &lt;file&gt;</em>.
	 * Reject a file transfer request from a user and abort the transfer.
	 *
	 * @param args First argument is the other user in the file transfer,
	 * and the second is the file being transferred.
	 */
	private void cmdReject( final String args )
	{
		String[] argsArray = args.split( "\\s" );

		if ( argsArray.length <= 2 )
		{
			msgController.showSystemMessage( "/reject - missing arguments <nick> <file>" );
			return;
		}

		String nick = argsArray[1];
		User user = controller.getUser( nick );

		if ( user == null )
		{
			msgController.showSystemMessage( "/reject - no such user '" + nick + "'" );
			return;
		}

		if ( user == me )
		{
			msgController.showSystemMessage( "/reject - no point in doing that!" );
			return;
		}

		String file = "";

		for ( int i = 2; i < argsArray.length; i++ )
		{
			file += argsArray[i] + " ";
		}

		file = file.trim();
		FileReceiver fileReceiver = tList.getFileReceiver( user, file );

		if ( fileReceiver == null )
		{
			msgController.showSystemMessage( "/reject - no such file '" + file + "' offered by " + nick );
			return;
		}

		if ( fileReceiver.isAccepted() )
		{
			msgController.showSystemMessage( "/reject - already receiving '" + file + "' from " + nick );
			return;
		}

		fileReceiver.reject();
	}

	/**
	 * Command: <em>/cancel &lt;nick&gt; &lt;file&gt;</em>.
	 * Cancel an ongoing file transfer with a user.
	 *
	 * @param args First argument is the other user in the file transfer,
	 * and the second is the file being transferred.
	 */
	private void cmdCancel( final String args )
	{
		String[] argsArray = args.split( "\\s" );

		if ( argsArray.length <= 2 )
		{
			msgController.showSystemMessage( "/cancel - missing arguments <nick> <file>" );
			return;
		}

		String nick = argsArray[1];
		User user = controller.getUser( nick );

		if ( user == null )
		{
			msgController.showSystemMessage( "/cancel - no such user '" + nick + "'" );
			return;
		}

		if ( user == me )
		{
			msgController.showSystemMessage( "/cancel - no point in doing that!" );
			return;
		}

		String file = "";

		for ( int i = 2; i < argsArray.length; i++ )
		{
			file += argsArray[i] + " ";
		}

		file = file.trim();
		FileTransfer fileTransfer = tList.getFileTransfer( user, file );

		if ( fileTransfer == null )
		{
			msgController.showSystemMessage( "/cancel - no such file transfer of '" + file + "' with " + nick );
			return;
		}

		if ( fileTransfer instanceof FileReceiver )
		{
			FileReceiver fileReceiver = (FileReceiver) fileTransfer;

			if ( !fileReceiver.isAccepted() && !fileReceiver.isRejected() )
			{
				msgController.showSystemMessage( "/cancel - transfer of '" + file + "' from " + nick + " has not started yet" );
				return;
			}
		}

		cancelFileTransfer( fileTransfer );
	}

	/**
	 * Command: <em>/msg &lt;nick&gt; &lt;msg&gt;</em>.
	 * Send a private message to a user.
	 *
	 * @param args The first argument is the user to send to, and the
	 * second is the private message to the user.
	 */
	private void cmdMsg( final String args )
	{
		String[] argsArray = args.split( "\\s" );

		if ( argsArray.length <= 2 )
		{
			msgController.showSystemMessage( "/msg - missing arguments <nick> <msg>" );
		}

		else
		{
			String nick = argsArray[1];
			User user = controller.getUser( nick );

			if ( user == null )
			{
				msgController.showSystemMessage( "/msg - no such user '" + nick + "'" );
			}

			else if ( user == me )
			{
				msgController.showSystemMessage( "/msg - no point in doing that!" );
			}

			else if ( user.getPrivateChatPort() == 0 )
			{
				msgController.showSystemMessage( "/msg - " + user.getNick() + " can't receive private chat messages" );
			}

			else
			{
				String privmsg = "";

				for ( int i = 2; i < argsArray.length; i++ )
				{
					privmsg += argsArray[i] + " ";
				}

				privmsg = privmsg.trim();

				try
				{
					controller.sendPrivateMessage( privmsg, user );
					msgController.showPrivateOwnMessage( user, privmsg );
				}

				catch ( final CommandException e )
				{
					msgController.showSystemMessage( e.getMessage() );
				}
			}
		}
	}

	/**
	 * Command: <em>/nick &lt;new nick&gt;</em>.
	 * Changes your nick name.
	 *
	 * @param args The nick to change to.
	 */
	private void cmdNick( final String args )
	{
		if ( args.trim().length() == 0 )
		{
			msgController.showSystemMessage( "/nick - missing argument <nick>" );
		}

		else
		{
			String[] argsArray = args.split( "\\s" );
			String nick = argsArray[1].trim();

			if ( !nick.equals( me.getNick() ) )
			{
				if ( controller.isNickInUse( nick ) )
				{
					msgController.showSystemMessage( "/nick - '" + nick + "' is in use by someone else" );
				}

				else if ( !Tools.isValidNick( nick ) )
				{
					msgController.showSystemMessage( "/nick - '" + nick + "' is not a valid nick name. (1-10 letters)" );
				}

				else
				{
					try
					{
						controller.changeMyNick( nick );
						msgController.showSystemMessage( "You changed nick to " + me.getNick() );
						ui.showTopic();
					}

					catch ( final CommandException e )
					{
						msgController.showSystemMessage( e.getMessage() );
					}
				}
			}

			else
			{
				msgController.showSystemMessage( "/nick - you are already called '" + nick + "'" );
			}
		}
	}

	/**
	 * Command: <em>/users</em>.
	 * Shows a list of connected users.
	 */
	private void cmdUsers()
	{
		UserList list = controller.getUserList();
		String userList = "";

		for ( int i = 0; i < list.size(); i++ )
		{
			User user = list.get( i );
			userList += user.getNick();

			if ( i < list.size() - 1 )
				userList += ", ";
		}

		msgController.showSystemMessage( "Users: " + userList );
	}

	/**
	 * Command: <em>/transfers</em>.
	 * Shows a list of all transfers and their status.
	 */
	private void cmdTransfers()
	{
		List<FileSender> fsList = tList.getFileSenders();
		List<FileReceiver> frList = tList.getFileReceivers();

		StringBuilder transferInfo = new StringBuilder();

		if ( fsList.size() > 0 )
		{
			transferInfo.append( "\n- Sending:" );

			for ( FileSender fs : fsList )
			{
				appendTransferInfo( fs, transferInfo, "to" );
			}
		}

		if ( frList.size() > 0 )
		{
			transferInfo.append( "\n- Receiving:" );

			for ( FileReceiver fr : frList )
			{
				appendTransferInfo( fr, transferInfo, "from" );
			}
		}

		if ( transferInfo.length() == 0 )
			transferInfo.append( " no active file transfers" );

		msgController.showSystemMessage( "File transfers:" + transferInfo.toString() );
	}

	/**
	 * Command: <em>/quit</em>.
	 * Quits the application.
	 */
	private void cmdQuit()
	{
		ui.quit();
	}

	/**
	 * Adds a new line with information about the file transfer.
	 *
	 * @param fileTransfer The file transfer to add info about.
	 * @param transferInfo The string builder to add the info to.
	 * @param direction To or from.
	 */
	private void appendTransferInfo( final FileTransfer fileTransfer, final StringBuilder transferInfo, final String direction )
	{
		transferInfo.append( "\n  " );
		transferInfo.append( fileTransfer.getFile().getName() );
		transferInfo.append( " [" + Tools.byteToString( fileTransfer.getFileSize() ) + "]" );
		transferInfo.append( " (" + fileTransfer.getPercent() + "%, " );
		transferInfo.append( Tools.byteToString( fileTransfer.getSpeed() ) + "/s)" );
		transferInfo.append( " " + direction + " " );
		transferInfo.append( fileTransfer.getUser().getNick() );
	}

	/**
	 * Command: <em>//&lt;text&gt;</em>.
	 * Sends the text as a message, instead of parsing it as a command.
	 *
	 * @param line The text starting with a slash.
	 */
	private void cmdSlash( final String line )
	{
		String message = line.replaceFirst( "/", "" );

		try
		{
			controller.sendChatMessage( message );
			msgController.showOwnMessage( message );
		}

		catch ( final CommandException e )
		{
			msgController.showSystemMessage( e.getMessage() );
		}
	}

	/**
	 * Command: <em>/'anything'</em>.
	 * The command was not recognized by the parser.
	 *
	 * @param command The unknown command.
	 */
	private void cmdUnknown( final String command )
	{
		msgController.showSystemMessage( "Unknown command '" + command + "'. Type /help for a list of commands" );
	}

	/**
	 * Updates the topic. If the new topic is empty, the topic will be removed.
	 *
	 * @param newTopic The new topic to use.
	 * @throws CommandException If there was a problem changing the topic.
	 */
	public void fixTopic( final String newTopic ) throws CommandException
	{
		Topic topic = controller.getTopic();
		String trimTopic = newTopic.trim();

		if ( !trimTopic.equals( topic.getTopic().trim() ) )
		{
			controller.changeTopic( trimTopic );

			if ( trimTopic.length() > 0 )
				msgController.showSystemMessage( "You changed the topic to: " + trimTopic );
			else
				msgController.showSystemMessage( "You removed the topic" );

			ui.showTopic();
		}
	}

	/**
	 * Sends a file to a user.
	 *
	 * @param user The user to send to.
	 * @param file The file to send to the user.
	 * @throws CommandException If there was a problem sending the file.
	 */
	public void sendFile( final User user, final File file ) throws CommandException
	{
		controller.sendFile( user, file );
		FileSender fileSend = new FileSender( user, file );
		ui.showTransfer( fileSend );
		tList.addFileSender( fileSend );
		String size = Tools.byteToString( file.length() );
		msgController.showSystemMessage( "Trying to send the file "
				+ file.getName() + " [" + size + "] to " + user.getNick() );
	}

	/**
	 * Cancels a file transfer, even if the file transfer has not been
	 * answered by the other user yet.
	 *
	 * @param fileTransfer The file transfer to cancel.
	 */
	public void cancelFileTransfer( final FileTransfer fileTransfer )
	{
		fileTransfer.cancel();

		if ( fileTransfer instanceof FileSender )
		{
			FileSender fs = (FileSender) fileTransfer;

			// This means that the other user has not answered yet
			if ( fs.isWaiting() )
			{
				File file = fs.getFile();
				User user = fs.getUser();

				msgController.showSystemMessage( "You cancelled sending of "
						+ file.getName() + " to " + user.getNick() );
				tList.removeFileSender( fs );
				controller.sendFileAbort( user, file.hashCode(), file.getName() );
			}
		}
	}

	/**
	 * Shows a list of all the supported commands, with a short description.
	 */
	public void showCommands()
	{
		msgController.showSystemMessage( Constants.APP_NAME + " commands:\n"
				+ "/about - information about " + Constants.APP_NAME + "\n"
				+ "/away <away message> - set status to away\n"
				+ "/back - set status to not away\n"
				+ "/cancel <nick> <file> - cancel an ongoing file transfer with a user\n"
				+ "/clear - clear all the text from the chat\n"
				+ "/help - show this help message\n"
				+ "/msg <nick> <msg> - send a private message to a user\n"
				+ "/nick <new nick> - changes your nick name\n"
				+ "/quit - quit from the chat\n"
				+ "/receive <nick> <file> - accept a file transfer request from a user\n"
				+ "/reject <nick> <file> - reject a file transfer request from a user\n"
				+ "/send <nick> <file> - send a file to a user\n"
				+ "/topic <optional new topic> - prints the current topic, or changes the topic\n"
				+ "/transfers - shows a list of all file transfers and their status\n"
				+ "/users - show the user list\n"
				+ "/whois <nick> - show information about a user\n"
				+ "//<text> - send the text as a normal message, with a single slash" );
	}

	/**
	 * Parses the line to split the command from the arguments.
	 * The command is then checked against valid options and redirected
	 * to the appropriate method.
	 *
	 * @param line The command in its raw form.
	 */
	public void parse( final String line )
	{
		String command = "";

		if ( line.contains( " " ) )
			command = line.substring( 1, line.indexOf( ' ' ) );
		else
			command = line.substring( 1, line.length() );

		if ( command.length() > 0 )
		{
			String args = line.replaceFirst( "/" + Pattern.quote( command ), "" );

			if ( command.equals( "topic" ) )
				cmdTopic( args );
			else if ( command.equals( "away" ) )
				cmdAway( args );
			else if ( command.equals( "back" ) )
				cmdBack();
			else if ( command.equals( "clear" ) )
				cmdClear();
			else if ( command.equals( "about" ) )
				cmdAbout();
			else if ( command.equals( "help" ) )
				cmdHelp();
			else if ( command.equals( "whois" ) )
				cmdWhois( args );
			else if ( command.equals( "send" ) )
				cmdSend( args );
			else if ( command.equals( "receive" ) )
				cmdReceive( args );
			else if ( command.equals( "reject" ) )
				cmdReject( args );
			else if ( command.equals( "cancel" ) )
				cmdCancel( args );
			else if ( command.equals( "msg" ) )
				cmdMsg( args );
			else if ( command.equals( "nick" ) )
				cmdNick( args );
			else if ( command.equals( "users" ) )
				cmdUsers();
			else if ( command.equals( "transfers" ) )
				cmdTransfers();
			else if ( command.equals( "quit" ) )
				cmdQuit();
			else if ( command.startsWith( "/" ) )
				cmdSlash( line );
			else
				cmdUnknown( command );
		}

		else
			cmdUnknown( command );
	}
}
