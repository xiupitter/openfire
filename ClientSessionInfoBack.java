package com.xiupitter.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.jivesoftware.openfire.Connection;
import org.jivesoftware.openfire.multiplex.ConnectionMultiplexerManager;
import org.jivesoftware.openfire.session.LocalClientSession;
import org.jivesoftware.util.cache.ExternalizableUtil;
import org.xmpp.packet.JID;
import org.xmpp.packet.Presence;

public class ClientSessionInfoBack implements Externalizable{
	
    private Presence presence;
    private String defaultList;
    private String activeList;
    private boolean offlineFloodStopped;
    /**
     * Flag indicating if this session has been initialized yet (upon first available transition).
     */
    private boolean initialized;

    /**
     * Flag that indicates if the session was available ever.
     */
    private boolean wasAvailable;
    
    private int conflictCount;

    
    //////
    /**
     * The utf-8 charset for decoding and encoding Jabber packet streams.
     */
    protected static String CHARSET = "UTF-8";

    /**
     * The Address this session is authenticated as.
     */
    private JID address;

    /**
     * The stream id for this session (random and unique).
     */
    private String streamID;

    /**
     * The current session status.
     */
    protected int status;

    /**
     * The connection that this session represents.
     */
    protected Connection conn;

    private String serverName;

    private long startDate;

    private long lastActiveDate;
    private long clientPacketCount;
    private long serverPacketCount;
    
    private String hostName;
    private String hostAddress;
    /**
	 * Session temporary data. All data stored in this <code>Map</code> disapear when session
	 * finishes.
	 */
	private final Map<String, Object> sessionData =null;


    public ClientSessionInfoBack() {
    }

    public ClientSessionInfoBack(LocalClientSession session) {
        presence = session.getPresence();
        defaultList = session.getDefaultList() != null ? session.getDefaultList().getName() : null;
        activeList = session.getActiveList() != null ? session.getActiveList().getName() : null;
        offlineFloodStopped = session.isOfflineFloodStopped();
        initialized = session.isInitialized();
        wasAvailable = session.wasAvailable();
        conflictCount = session.incrementConflictCount()-1;
        address = session.getAddress();
        streamID = session.getStreamID().getID();
        status = session.getStatus();
        serverName = session.getServerName();
        startDate  = session.getCreationDate().getTime();
        lastActiveDate = session.getLastActiveDate().getTime();
        clientPacketCount = session.getNumClientPackets();
        serverPacketCount = session.getNumServerPackets();
        try {
			hostName = session.getConnection().getHostName();
	        hostAddress = session.getConnection().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public Presence getPresence() {
        return presence;
    }

    public String getDefaultList() {
        return defaultList;
    }

    public String getActiveList() {
        return activeList;
    }

    public boolean isOfflineFloodStopped() {
        return offlineFloodStopped;
    }

    public boolean isInitialized() {
		return initialized;
	}

	public boolean isWasAvailable() {
		return wasAvailable;
	}

	public int getConflictCount() {
		return conflictCount;
	}

	public JID getAddress() {
		return address;
	}

	public String getStreamID() {
		return streamID;
	}

	public int getStatus() {
		return status;
	}

	public String getServerName() {
		return serverName;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getLastActiveDate() {
		return lastActiveDate;
	}

	public long getClientPacketCount() {
		return clientPacketCount;
	}

	public long getServerPacketCount() {
		return serverPacketCount;
	}

	public String getHostName() {
		return hostName;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableUtil.getInstance().writeSerializable(out, (DefaultElement) presence.getElement());
        ExternalizableUtil.getInstance().writeBoolean(out, defaultList != null);
        if (defaultList != null) {
            ExternalizableUtil.getInstance().writeSafeUTF(out, defaultList);
        }
        ExternalizableUtil.getInstance().writeBoolean(out, activeList != null);
        if (activeList != null) {
            ExternalizableUtil.getInstance().writeSafeUTF(out, activeList);
        }
        ExternalizableUtil.getInstance().writeBoolean(out, offlineFloodStopped);
        
        ExternalizableUtil.getInstance().writeBoolean(out, initialized);
        ExternalizableUtil.getInstance().writeBoolean(out, wasAvailable);
        ExternalizableUtil.getInstance().writeInt(out, conflictCount);
        ExternalizableUtil.getInstance().writeSerializable(out, address);
        ExternalizableUtil.getInstance().writeSafeUTF(out, streamID);
        ExternalizableUtil.getInstance().writeInt(out, status);
        ExternalizableUtil.getInstance().writeSafeUTF(out, serverName);
        ExternalizableUtil.getInstance().writeLong(out, startDate);
        ExternalizableUtil.getInstance().writeLong(out, lastActiveDate);
        ExternalizableUtil.getInstance().writeLong(out, clientPacketCount);
        ExternalizableUtil.getInstance().writeLong(out, serverPacketCount);
        ExternalizableUtil.getInstance().writeSafeUTF(out, hostAddress);
        ExternalizableUtil.getInstance().writeSafeUTF(out, hostName);

    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Element packetElement = (Element) ExternalizableUtil.getInstance().readSerializable(in);
        presence = new Presence(packetElement, true);
        if (ExternalizableUtil.getInstance().readBoolean(in)) {
            defaultList = ExternalizableUtil.getInstance().readSafeUTF(in);
        }
        if (ExternalizableUtil.getInstance().readBoolean(in)) {
            activeList = ExternalizableUtil.getInstance().readSafeUTF(in);
        }
        offlineFloodStopped = ExternalizableUtil.getInstance().readBoolean(in);
        
        initialized = ExternalizableUtil.getInstance().readBoolean(in);
        wasAvailable = ExternalizableUtil.getInstance().readBoolean(in);
        conflictCount =  ExternalizableUtil.getInstance().readInt(in);
        address =(JID) ExternalizableUtil.getInstance().readSerializable(in);
        streamID = ExternalizableUtil.getInstance().readSafeUTF(in);
        status =  ExternalizableUtil.getInstance().readInt(in);
        serverName = ExternalizableUtil.getInstance().readSafeUTF(in);
        startDate = ExternalizableUtil.getInstance().readLong(in);
        lastActiveDate = ExternalizableUtil.getInstance().readLong(in);
        clientPacketCount = ExternalizableUtil.getInstance().readLong(in);
        serverPacketCount = ExternalizableUtil.getInstance().readLong(in);
        hostAddress = ExternalizableUtil.getInstance().readSafeUTF(in);
        hostName = ExternalizableUtil.getInstance().readSafeUTF(in);
    }
}
