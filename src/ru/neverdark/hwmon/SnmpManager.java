package ru.neverdark.hwmon;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by ufo on 06.03.17.
 */
public class SnmpManager {
    public static final int VERSION1 = SnmpConstants.version1;
    public static final int VERSION2 = SnmpConstants.version2c;

    private final int mVersion;
    private final String mIp;
    private final String mCommunity;
    private Snmp mSnmp;
    private TransportMapping mTransport;

    public SnmpManager(String ip, String community, int version) {
        mIp = String.format(Locale.US, "udp:%s/161", ip);
        mCommunity = community;
        mVersion = version;
    }

    public void stop() {
        try {
            if (mTransport != null) {
                mTransport.close();
                mTransport = null;
            }

            if (mSnmp != null) {
                mSnmp.close();
                mSnmp = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        mTransport = new DefaultUdpTransportMapping();
        mSnmp = new Snmp(mTransport);
        // Do not forget this line!
        mTransport.listen();
    }

    public String getAsString(String oid) throws IOException {
        ResponseEvent event = get(new OID[]{new OID(oid)});
        return event.getResponse().get(0).getVariable().toString();
    }

    public boolean getAsBoolean(String oid) throws IOException {
        ResponseEvent event = get(new OID[]{new OID(oid)});
        return event.getResponse().get(0).getVariable().toInt() == 1;
    }

    public int getAsInt(String oid) throws IOException {
        ResponseEvent event = get(new OID[]{new OID(oid)});
        return event.getResponse().get(0).getVariable().toInt();
    }

    /**
     * This method is capable of handling multiple OIDs
     *
     * @param oids
     * @return
     * @throws IOException
     */
    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = mSnmp.send(pdu, getTarget(), null);
        if (event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }

    /**
     * This method returns a Target, which contains information about where the
     * data should be fetched and how.
     *
     * @return
     */
    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(mIp);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(mCommunity));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(15000);
        target.setVersion(mVersion);
        return target;
    }
}
