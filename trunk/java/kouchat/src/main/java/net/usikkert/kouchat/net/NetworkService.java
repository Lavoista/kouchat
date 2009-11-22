
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

package net.usikkert.kouchat.net;

import java.net.NetworkInterface;

import net.usikkert.kouchat.event.NetworkConnectionListener;
import net.usikkert.kouchat.event.ReceiverListener;

/**
 * This class has services for connecting to the network.
 *
 * @author Christian Ihle
 */
public class NetworkService implements NetworkConnectionListener
{
	/** The thread responsible for keeping the network connection up. */
	private final ConnectionWorker connectionWorker;

	/** The multicast message sender. */
	private final MessageSender messageSender;

	/** The multicast message receiver. */
	private final MessageReceiver messageReceiver;

	/** The private message sender. */
	private final UDPSender udpSender;

	/** The private message receiver. */
	private final UDPReceiver udpReceiver;

	/**
	 * Constructor.
	 */
	public NetworkService()
	{
		messageReceiver = new MessageReceiver();
		messageSender = new MessageSender();
		connectionWorker = new ConnectionWorker();
		udpReceiver = new UDPReceiver();
		udpSender = new UDPSender();
		connectionWorker.registerNetworkConnectionListener( this );
	}

	/**
	 * Starts the thread responsible for connecting to the network.
	 */
	public void connect()
	{
		connectionWorker.start();
	}

	/**
	 * Stops the thread responsible for connecting to the network.
	 */
	public void disconnect()
	{
		connectionWorker.stop();
	}

	/**
	 * Gets the connection worker.
	 *
	 * @return The connection worker.
	 */
	public ConnectionWorker getConnectionWorker()
	{
		return connectionWorker;
	}

	/**
	 * Checks if the connection thread is alive.
	 *
	 * @return If the connection thread is alive.
	 */
	public boolean isConnectionWorkerAlive()
	{
		return connectionWorker.isAlive();
	}

	/**
	 * Checks if the network is up.
	 *
	 * @return If the network is up.
	 */
	public boolean isNetworkUp()
	{
		return connectionWorker.isNetworkUp();
	}

	/**
	 * Registers the listener as a connection listener.
	 *
	 * @param listener The listener to register.
	 */
	public void registerNetworkConnectionListener( final NetworkConnectionListener listener )
	{
		connectionWorker.registerNetworkConnectionListener( listener );
	}

	/**
	 * Register a listener for incoming messages from the network.
	 *
	 * @param listener The listener to register.
	 */
	public void registerMessageReceiverListener( final ReceiverListener listener )
	{
		messageReceiver.registerReceiverListener( listener );
	}

	/**
	 * Register a listener for incoming UDP messages from the network.
	 *
	 * @param listener The listener to register.
	 */
	public void registerUDPReceiverListener( final ReceiverListener listener )
	{
		udpReceiver.registerReceiverListener( listener );
	}

	/**
	 * Send a message with multicast, to all users.
	 *
	 * @param message The message to send.
	 * @return If the message was sent or not.
	 */
	public boolean sendMulticastMsg( final String message )
	{
		return messageSender.send( message );
	}

	/**
	 * Send a message with UDP, to a single user.
	 *
	 * @param message The message to send.
	 * @param ip The ip address of the user.
	 * @param port The port to send the message to.
	 * @return If the message was sent or not.
	 */
	public boolean sendUDPMsg( final String message, final String ip, final int port )
	{
		return udpSender.send( message, ip, port );
	}

	/**
	 * Checks the state of the network, and tries to keep the best possible
	 * network connection up.
	 */
	public void checkNetwork()
	{
		connectionWorker.checkNetwork();
	}

	/**
	 * Stops all senders and receivers.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void networkWentDown( final boolean silent )
	{
		udpSender.stopSender();
		udpReceiver.stopReceiver();
		messageSender.stopSender();
		messageReceiver.stopReceiver();
	}

	/**
	 * Starts all senders and receivers.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void networkCameUp( final boolean silent )
	{
		udpSender.startSender();
		udpReceiver.startReceiver();
		NetworkInterface currentNetworkInterface = connectionWorker.getCurrentNetworkInterface();
		messageSender.startSender( currentNetworkInterface );
		messageReceiver.startReceiver( currentNetworkInterface );
	}
}
