
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

package net.usikkert.kouchat.net;

import java.io.File;

import net.usikkert.kouchat.misc.CommandException;
import net.usikkert.kouchat.misc.Settings;
import net.usikkert.kouchat.misc.Topic;
import net.usikkert.kouchat.misc.User;
import net.usikkert.kouchat.util.Validate;

/**
 * This class gives access to sending the different kinds of messages
 * that this application supports. Both multicast, and normal udp.
 *
 * @author Christian Ihle
 */
public class Messages
{
	/** The network service used for sending the actual messages. */
	private final NetworkService networkService;

	/** The application user. */
	private final User me;

	/** Settings. */
	private final Settings settings;

	/**
	 * Constructor.
	 *
	 * @param networkService The network service used for sending the actual messages.
	 */
	public Messages( final NetworkService networkService )
	{
		Validate.notNull( networkService, "Network service can not be null" );
		this.networkService = networkService;
		settings = Settings.getSettings();
		me = settings.getMe();
	}

	/**
	 * Sends a message notifying other clients that this client is still alive.
	 *
	 * <p>Note: the network will be checked if this fails!</p>
	 */
	public void sendIdleMessage()
	{
		String msg = me.getCode() + "!IDLE#" + me.getNick() + ":";
		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
			checkNetwork();
	}

	/**
	 * Sends a message to change the topic.
	 *
	 * <p>Note: the network will be checked if this fails!</p>
	 *
	 * @param topic The new topic to send.
	 */
	public void sendTopicChangeMessage( final Topic topic )
	{
		String msg = createTopicMessage( topic );
		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
			checkNetwork();
	}

	/**
	 * Sends a message with the current topic.
	 *
	 * @param topic The current topic to send.
	 */
	public void sendTopicRequestedMessage( final Topic topic )
	{
		String msg = createTopicMessage( topic );
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to set the user as away, including the away message.
	 *
	 * <p>Note: the network will be checked if this fails!</p>
	 *
	 * @param awayMsg The away message to set.
	 */
	public void sendAwayMessage( final String awayMsg )
	{
		String msg = me.getCode() + "!AWAY#" + me.getNick() + ":" + awayMsg;
		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
			checkNetwork();
	}

	/**
	 * Sends a message to set the user as back from away.
	 *
	 * <p>Note: the network will be checked if this fails!</p>
	 */
	public void sendBackMessage()
	{
		String msg = me.getCode() + "!BACK#" + me.getNick() + ":";
		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
			checkNetwork();
	}

	/**
	 * Sends a normal chat message, that is part of the main chat.
	 *
	 * <p>Note: the network will be checked, and the user notified if this fails!</p>
	 *
	 * @param chatMsg The message for the main chat.
	 * @throws CommandException If the message was not sent successfully.
	 */
	public void sendChatMessage( final String chatMsg ) throws CommandException
	{
		String msg = me.getCode() + "!MSG#" + me.getNick() + ":"
				+ "[" + settings.getOwnColor() + "]"
				+ chatMsg;

		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
		{
			checkNetwork();
			notifyUser( "Failed to send message: " + chatMsg );
		}
	}

	/**
	 * Sends a message to log this client on the network.
	 */
	public void sendLogonMessage()
	{
		String msg = me.getCode() + "!LOGON#" + me.getNick() + ":";
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to log this client off the network.
	 */
	public void sendLogoffMessage()
	{
		String msg = me.getCode() + "!LOGOFF#" + me.getNick() + ":";
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message asking the other clients to identify themselves.
	 */
	public void sendExposeMessage()
	{
		String msg = me.getCode() + "!EXPOSE#" + me.getNick() + ":";
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to identify this client.
	 */
	public void sendExposingMessage()
	{
		String msg = me.getCode() + "!EXPOSING#" + me.getNick() + ":" + me.getAwayMsg();
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to ask for the current topic.
	 */
	public void sendGetTopicMessage()
	{
		String msg = me.getCode() + "!GETTOPIC#" + me.getNick() + ":";
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to notify that the user is writing.
	 */
	public void sendWritingMessage()
	{
		String msg = me.getCode() + "!WRITING#" + me.getNick() + ":";
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to notify that the user has stopped writing.
	 */
	public void sendStoppedWritingMessage()
	{
		String msg = me.getCode() + "!STOPPEDWRITING#" + me.getNick() + ":";
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to change the nick name of the user.
	 *
	 * <p>Note: the network will be checked if this fails!</p>
	 *
	 * @param newNick The new nick to send.
	 */
	public void sendNickMessage( final String newNick )
	{
		String msg = me.getCode() + "!NICK#" + newNick + ":";
		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
			checkNetwork();
	}

	/**
	 * Sends a message to inform that another user has logged on with
	 * the same nick name as this user.
	 *
	 * @param crashNick The nick name that is already in use by the user.
	 */
	public void sendNickCrashMessage( final String crashNick )
	{
		String msg = me.getCode() + "!NICKCRASH#" + me.getNick() + ":" + crashNick;
		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a message to notify the file sender that you aborted the file transfer.
	 *
	 * @param user The user sending a file.
	 * @param fileHash The unique hash code of the file.
	 * @param fileName The name of the file.
	 */
	public void sendFileAbort( final User user, final int fileHash, final String fileName )
	{
		String msg = me.getCode() + "!SENDFILEABORT#" + me.getNick() + ":"
				+ "(" + user.getCode() + ")"
				+ "{" + fileHash + "}"
				+ fileName;

		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
			checkNetwork();
	}

	/**
	 * Sends a message to notify the file sender that you
	 * accepted the file transfer.
	 *
	 * <p>Note: the network will be checked, and the user notified if this fails!</p>
	 *
	 * @param user The user sending a file.
	 * @param port The port the file sender can connect to on this client
	 * 		       to start the file transfer.
	 * @param fileHash The unique hash code of the file.
	 * @param fileName The name of the file.
	 * @throws CommandException If the message was not sent successfully.
	 */
	public void sendFileAccept( final User user, final int port,
			final int fileHash, final String fileName ) throws CommandException
	{
		String msg = me.getCode() + "!SENDFILEACCEPT#" + me.getNick() + ":"
				+ "(" + user.getCode() + ")"
				+ "[" + port + "]"
				+ "{" + fileHash + "}"
				+ fileName;

		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
		{
			checkNetwork();
			notifyUser( "Failed to accept file transfer from " + user.getNick() + ": " + fileName );
		}
	}

	/**
	 * Sends a message to notify another user that you want to send a file.
	 *
	 * <p>Note: the network will be checked, and the user notified if this fails!</p>
	 *
	 * @param user The user asked to receive a file.
	 * @param file The file to send.
	 * @throws CommandException If the message was not sent successfully.
	 */
	public void sendFile( final User user, final File file ) throws CommandException
	{
		String msg = me.getCode() + "!SENDFILE#" + me.getNick() + ":"
				+ "(" + user.getCode() + ")"
				+ "[" + file.length() + "]"
				+ "{" + file.hashCode() + "}"
				+ file.getName();

		boolean sent = networkService.sendMulticastMsg( msg );

		if ( !sent )
		{
			checkNetwork();
			notifyUser( "Failed to send file to " + user.getNick() + ": " + file.getName() );
		}
	}

	/**
	 * Sends a message with extra client information:
	 *
	 * <ul>
	 *   <li>Name of the client.</li>
	 *   <li>Client uptime.</li>
	 *   <li>Operating system.</li>
	 *   <li>Port to connect to for private chat.</li>
	 * </ul>
	 */
	public void sendClient()
	{
		String msg = me.getCode() + "!CLIENT#" + me.getNick() + ":"
				+ "(" + me.getClient() + ")"
				+ "[" + ( System.currentTimeMillis() - me.getLogonTime() ) + "]"
				+ "{" + me.getOperatingSystem() + "}"
				+ "<" + me.getPrivateChatPort() + ">";

		networkService.sendMulticastMsg( msg );
	}

	/**
	 * Sends a private message to a user.
	 *
	 * <p>Note: the network will be checked, and the user notified if this fails!</p>
	 *
	 * @param privMsg The private message to send.
	 * @param user The user to send the message to.
	 * @throws CommandException If the message was not sent successfully.
	 */
	public void sendPrivateMessage( final String privMsg, final User user ) throws CommandException
	{
		String msg = me.getCode() + "!PRIVMSG#" + me.getNick() + ":"
				+ "(" + user.getCode() + ")"
				+ "[" + settings.getOwnColor() + "]"
				+ privMsg;

		boolean sent = networkService.sendUDPMsg( msg, user.getIpAddress(), user.getPrivateChatPort() );

		if ( !sent )
		{
			checkNetwork();
			notifyUser( "Failed to send private message to " + user.getNick() + ": " + privMsg );
		}
	}

	/**
	 * Creates a new message for sending the topic.
	 *
	 * @param topic The topic to use in the message.
	 * @return The new message.
	 */
	private String createTopicMessage( final Topic topic )
	{
		return me.getCode() + "!TOPIC#" + me.getNick() + ":"
				+ "(" + topic.getNick() + ")"
				+ "[" + topic.getTime() + "]"
				+ topic.getTopic();
	}

	/**
	 * Informs the user that the message could not be delivered.
	 *
	 * @param infoMsg The message to give the user.
	 * @throws CommandException The exception returned with the message.
	 */
	private void notifyUser( final String infoMsg ) throws CommandException
	{
		throw new CommandException( infoMsg );
	}

	/**
	 * Asks the network service to check the network status.
	 */
	private void checkNetwork()
	{
		networkService.checkNetwork();
	}
}
