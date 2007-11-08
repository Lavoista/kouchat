
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

package net.usikkert.kouchat.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

/**
 * Shows a popup menu with copy and select all menu items.
 * 
 * @author Christian Ihle
 */
public class ChatPopup extends JPopupMenu implements MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	private JMenuItem copyMI, selectAllMI;
	private JTextPane chatTP;

	/**
	 * Constructor. Creates the menu.
	 * 
	 * @param chatTP The text pane to use the popup on.
	 */
	public ChatPopup( JTextPane chatTP )
	{
		this.chatTP = chatTP;

		copyMI = new JMenuItem( new DefaultEditorKit.CopyAction() );
		copyMI.setText( "Copy" );
		copyMI.setMnemonic( 'C' );
		copyMI.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, KeyEvent.CTRL_MASK ) );

		selectAllMI = new JMenuItem( "Select All" );
		selectAllMI.setMnemonic( 'A' );
		selectAllMI.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, KeyEvent.CTRL_MASK ) );

		add( copyMI );
		add( selectAllMI );

		chatTP.addMouseListener( this );
		selectAllMI.addActionListener( this );
	}

	@Override
	public void mouseClicked( MouseEvent e ) {}

	@Override
	public void mouseEntered( MouseEvent e ) {}

	@Override
	public void mouseExited( MouseEvent e ) {}

	@Override
	public void mousePressed( MouseEvent e ) {}

	/**
	 * Shows the popup menu if right mouse button was used.
	 */
	@Override
	public void mouseReleased( MouseEvent e )
	{
		if ( isPopupTrigger( e ) )
		{
			if ( chatTP.getSelectedText() == null )
				copyMI.setEnabled( false );
			else
				copyMI.setEnabled( true );

			if ( chatTP.getText().length() == 0 )
				selectAllMI.setEnabled( false );
			else
				selectAllMI.setEnabled( true );

			show( chatTP, e.getX(), e.getY() );
		}
	}

	/**
	 * Selects all the text.
	 */
	@Override
	public void actionPerformed( ActionEvent e )
	{
		chatTP.requestFocusInWindow();
		chatTP.selectAll();
	}
}
