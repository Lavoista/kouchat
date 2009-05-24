
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.usikkert.kouchat.Constants;
import net.usikkert.kouchat.event.FileTransferListener;
import net.usikkert.kouchat.misc.User;
import net.usikkert.kouchat.util.ByteCounter;
import net.usikkert.kouchat.util.Loggers;

/**
 * This is a class for receiving files from other users.
 *
 * <p>To receive a file, a server socket has to be opened,
 * to wait for incoming transfers.</p>
 *
 * @author Christian Ihle
 */
public class FileReceiver implements FileTransfer
{
	/** The logger. */
	private static final Logger LOG = Loggers.NETWORK_LOG;

	/** The user sending the file. */
	private final User user;

	/** The file size in bytes. */
	private final long size;

	/** The file from the user. */
	private final File file;

	/** Keeps count of the transfer speed. */
	private final ByteCounter bCounter;

	/** Percent of the file received. */
	private int percent;

	/** Number of bytes received. */
	private long transferred;

	/** If the file was successfully received. */
	private boolean received;

	/** If the file transfer is canceled. */
	private boolean cancel;

	/** The file transfer listener. */
	private FileTransferListener listener;

	/** The server socket waiting for an incoming connection. */
	private ServerSocket sSock;

	/** The socket connection to the other user. */
	private Socket sock;

	/** The output stream to the file. */
	private FileOutputStream fos;

	/** The input stream from the other user. */
	private InputStream is;

	/**
	 * Constructor. Creates a new file receiver.
	 *
	 * @param user The user which sends the file.
	 * @param file The file the user is sending.
	 * @param size The size of the file, in bytes.
	 */
	public FileReceiver( final User user, final File file, final long size )
	{
		this.user = user;
		this.file = file;
		this.size = size;

		bCounter = new ByteCounter();
	}

	/**
	 * Starts a server connection which the sender can use to connect
	 * for transferring the file, and returns the opened port.
	 *
	 * @return The port which the sender can connect to.
	 * @throws ServerException If the server could not be started.
	 */
	public int startServer() throws ServerException
	{
		int port = Constants.NETWORK_FILE_TRANSFER_PORT;
		boolean done = false;
		int counter = 0;

		while ( !done && counter < 10 )
		{
			try
			{
				sSock = new ServerSocket( port );
				TimeoutThread tt = new TimeoutThread();
				tt.start();
				done = true;
			}

			catch ( final IOException e )
			{
				LOG.log( Level.WARNING, "Could not open " + port, e );
				port++;
			}

			finally
			{
				counter++;
			}
		}

		if ( !done )
			throw new ServerException( "Could not start server" );

		return port;
	}

	/**
	 * Waits for an incoming connection, then receives the
	 * file from the other user.
	 *
	 * @return If the file transfer was successful.
	 */
	public boolean transfer()
	{
		listener.statusConnecting();

		received = false;
		cancel = false;

		try
		{
			if ( sSock != null )
			{
				sock = sSock.accept();
				listener.statusTransferring();
				fos = new FileOutputStream( file );
				is = sock.getInputStream();

				byte[] b = new byte[1024];
				transferred = 0;
				percent = 0;
				int tmpTransferred = 0;
				int tmpPercent = 0;
				int transCounter = 0;
				bCounter.reset();

				while ( ( tmpTransferred = is.read( b ) ) != -1 && !cancel )
				{
					fos.write( b, 0, tmpTransferred );
					transferred += tmpTransferred;
					percent = (int) ( ( transferred * 100 ) / size );
					bCounter.update( tmpTransferred );
					transCounter++;

					if ( percent > tmpPercent || transCounter >= 250 )
					{
						transCounter = 0;
						tmpPercent = percent;
						listener.transferUpdate();
					}
				}

				if ( !cancel && transferred == size )
				{
					received = true;
					listener.statusCompleted();
				}

				else
				{
					listener.statusFailed();
				}
			}
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString() );
			listener.statusFailed();
		}

		finally
		{
			stopReceiver();
		}

		return received;
	}

	/**
	 * Closes the connection to the user.
	 */
	private void stopReceiver()
	{
		try
		{
			if ( is != null )
			{
				is.close();
				is = null;
			}
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString(), e );
		}

		try
		{
			if ( fos != null )
				fos.flush();
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString(), e );
		}

		try
		{
			if ( fos != null )
			{
				fos.close();
				fos = null;
			}
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString(), e );
		}

		try
		{
			if ( sock != null )
			{
				sock.close();
				sock = null;
			}
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString(), e );
		}

		try
		{
			if ( sSock != null )
			{
				sSock.close();
				sSock = null;
			}
		}

		catch ( final IOException e )
		{
			LOG.log( Level.SEVERE, e.toString(), e );
		}
	}

	/**
	 * Checks if the file transfer has been canceled.
	 *
	 * @return If the file transfer has been canceled.
	 */
	@Override
	public boolean isCanceled()
	{
		return cancel;
	}

	/**
	 * Cancels the file transfer.
	 */
	@Override
	public void cancel()
	{
		cancel = true;
		stopReceiver();
		listener.statusFailed();
	}

	/**
	 * The percent of the file transfer that is completed.
	 *
	 * @return Percent completed.
	 */
	@Override
	public int getPercent()
	{
		return percent;
	}

	/**
	 * Checks if the file transfer is complete.
	 *
	 * @return If the file transfer is complete.
	 */
	@Override
	public boolean isTransferred()
	{
		return received;
	}

	/**
	 * Gets the file that is being transferred.
	 *
	 * @return The file.
	 */
	@Override
	public File getFile()
	{
		return file;
	}

	/**
	 * The other user, which sends a file.
	 *
	 * @return The other user.
	 */
	@Override
	public User getUser()
	{
		return user;
	}

	/**
	 * Number of bytes transferred.
	 *
	 * @return Bytes transferred.
	 */
	@Override
	public long getTransferred()
	{
		return transferred;
	}

	/**
	 * Gets the size of the file being transferred, in bytes.
	 *
	 * @return The file size.
	 */
	@Override
	public long getFileSize()
	{
		return size;
	}

	/**
	 * Gets the direction, which is receive.
	 *
	 * @return Receive, the direction of the file transfer.
	 */
	@Override
	public Direction getDirection()
	{
		return Direction.RECEIVE;
	}

	/**
	 * Gets the number of bytes transferred per second.
	 *
	 * @return The speed in bytes per second.
	 */
	@Override
	public long getSpeed()
	{
		return bCounter.getBytesPerSec();
	}

	/**
	 * Registers a file transfer listener, which will receive updates
	 * when certain events happen in the progression of the file transfer.
	 *
	 * @param listener The listener to register.
	 */
	@Override
	public void registerListener( final FileTransferListener listener )
	{
		this.listener = listener;
		listener.statusWaiting();
	}

	/**
	 * A thread for closing the server connection if no client
	 * has connected within 15 seconds.
	 *
	 * <p>This does not mean that the user only has 15 seconds to decide
	 * where to save the file. This timer is started after the user has
	 * decided, and waits for an automated response from the sender.
	 * If nothing has happened to the sender, the response should be very quick.</p>
	 */
	private class TimeoutThread extends Thread
	{
		/**
		 * Constructor. Sets the name of the thread.
		 */
		public TimeoutThread()
		{
			setName( "TimeoutThread" );
		}

		/**
		 * The thread. Sleeps for 15 seconds, and then closes the
		 * server connection if it is not already closed.
		 */
		@Override
		public void run()
		{
			try
			{
				sleep( 15000 );
			}

			catch ( final InterruptedException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}

			try
			{
				if ( sSock != null )
				{
					sSock.close();
					sSock = null;
				}
			}

			catch ( final IOException e )
			{
				LOG.log( Level.SEVERE, e.toString(), e );
			}
		}
	}
}