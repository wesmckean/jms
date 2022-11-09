package org.rwmckean.common.jms;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;


import java.net.URL;
import java.util.Collection;

import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.naming.NamingException;
import javax.naming.Reference;

/**
 * This class is responsible for providing queue connectivity to MQ for
 * the ESS application. See the comment on createConnection() for 
 * the primary reason this class exists.
 * @author psaladna
 *
 */
public class QueueConnectionFactory implements javax.jms.QueueConnectionFactory {
    private final MQQueueConnectionFactory qcf;
    private String username;
    private String password;
    private boolean userAuthenticationMQCSP = false;
    private int connectionMode = WMQConstants.WMQ_CM_BINDINGS;

    public boolean isUserAuthenticationMQCSP() {
        return userAuthenticationMQCSP;
    }

    public void setUserAuthenticationMQCSP(boolean userAuthenticationMQCSP) {
        this.userAuthenticationMQCSP = userAuthenticationMQCSP;
    }

    public int getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(int connectionMode) {
        this.connectionMode = connectionMode;
    }
        
    public QueueConnectionFactory() {
         qcf = new MQQueueConnectionFactory();
    }
        
    public String getPassword() {
    return password;
    }

    public void setPassword(String password) {
            this.password = password;                
    }

    public String getUsername() {
            return username;
    }

    public void setUsername(String username) {
            this.username = username;
    }

    private void updatePreconnectionProperties() {
        try { 
            qcf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, userAuthenticationMQCSP); 
            qcf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, connectionMode);
        } catch(JMSException ex) {}
    }

    /**
     * <i><b>Implementation includes a WAS 6.1 hack:</b></i><br/>
     * In WAS 6.1 even the call to getConnection()
     * passes a username/password. This causes authentication
     * exceptions in the following two scenerios:
     * 		<ul>
     * 			<li>If a username/password is not configured in MQ</li>
     *  		<li>If authentication is not turned on in MQ.</li>
     *  	</ul> 
     * The username/password passed is the authenticated user&apos;s, or some 
     * default username and password. When this happens, we receive the 
     * following exception:
     * <p/>
     * <pre>
     * [1/29/07 16:09:49:811 CST] 00000033 ServletWrappe E   SRVE0068E: Uncaught 
     * exception thrown in one of the service methods of the servlet: 
     * spring/controller. Exception thrown : 
     * org.springframework.web.util.NestedServletException: Request processing 
     * failed; nested exception is org.springframework.jms.JmsSecurityException:
     * MQJMS2013: invalid security authentication supplied for MQQueueManager; 
     * nested exception is javax.jms.JMSSecurityException: MQJMS2013: invalid 
     * security authentication supplied for MQQueueManager; nested exception 
     * is com.ibm.mq.MQException: MQJE001: An MQException occurred: Completion 
     * Code 2, Reason 2035 MQJE036: Queue manager rejected connection attempt 
     * Caused by: org.springframework.jms.JmsSecurityException: MQJMS2013: invalid 
     * security authentication supplied for MQQueueManager; nested exception is 
     * javax.jms.JMSSecurityException: MQJMS2013: invalid security authentication 
     * supplied for MQQueueManager; nested exception is com.ibm.mq.MQException: 
     * MQJE001: An MQException occurred: Completion Code 2, Reason 2035 MQJE036: 
     * Queue manager rejected connection attempt Caused by: javax.jms.JMSSecurityException: 
     * MQJMS2013: invalid security authentication supplied for MQQueueManager
     *         at com.ibm.mq.jms.MQConnection.createQM(MQConnection.java:2022)
     *         at com.ibm.mq.jms.MQConnection.createQMNonXA(MQConnection.java:1496)        
     *         at com.ibm.mq.jms.MQQueueConnection.<init>(MQQueueConnection.java:150)        
     *         at com.ibm.mq.jms.MQQueueConnectionFactory.createQueueConnection(MQQueueConnectionFactory.java:185)
     * </pre>
     * <p/>
     * The solution is to explicity call the createConnection(username, password)
     * underneath createConnection() to explicity pass a blank username/password combination,
     * subverting WebSphere&apos;s attempt at automagically injecting security 
     * credentials into every call to createConnection(). If a username/password is required,
     * use the setUsername(), setPassword() methods of this class.
     */
    public Connection createConnection() throws JMSException {
        updatePreconnectionProperties();
        return qcf.createConnection(username, password);
    }

    public Connection createConnection(String arg0, String arg1) throws JMSException {
        updatePreconnectionProperties();
        return qcf.createConnection(arg0, arg1);
    }

    public QueueConnection createQueueConnection() throws JMSException {
        updatePreconnectionProperties();
        return qcf.createQueueConnection();
    }

    public QueueConnection createQueueConnection(String arg0, String arg1) throws JMSException {
        updatePreconnectionProperties();
        return qcf.createQueueConnection(arg0, arg1);
    }

    public Reference getReference() throws NamingException {
        return qcf.getReference();
    }

    public String getBrokerCCSubQueue() throws JMSException {
        return qcf.getBrokerCCSubQueue();
    }

    public String getBrokerControlQueue() throws JMSException {
        return qcf.getBrokerControlQueue();
    }

    public String getBrokerPubQueue() throws JMSException {
        return qcf.getBrokerPubQueue();
    }

    public String getBrokerQueueManager() throws JMSException {
        return qcf.getBrokerQueueManager();
    }

    public String getBrokerSubQueue() throws JMSException {
        return qcf.getBrokerSubQueue();
    }

    public int getBrokerVersion() throws JMSException {
        return qcf.getBrokerVersion();
    }

    public URL getCCDTURL() {
        return qcf.getCCDTURL();
    }

    public int getCCSID() {
        return qcf.getCCSID();
    }

    public String getChannel() {
        return qcf.getChannel();
    }

    public long getCleanupInterval() throws JMSException {
        return qcf.getCleanupInterval();
    }

    public int getCleanupLevel() throws JMSException {
        return qcf.getCleanupLevel();
    }

    public String getClientID() {
        return qcf.getClientID();
    }

    @Deprecated
    public String getClientId() {
        return qcf.getClientId();
    }

    public int getCloneSupport() throws JMSException {
        return qcf.getCloneSupport();
    }

    public byte[] getConnTag() {
        return qcf.getConnTag();
    }

    public String getDescription() {
        return qcf.getDescription();
    }

    public int getDirectAuth() throws JMSException {
        return qcf.getDirectAuth();
    }

    public int getFailIfQuiesce() {
        return qcf.getFailIfQuiesce();
    }

    public Collection<Object> getHdrCompList() {
        return qcf.getHdrCompList();
    }

    public String getHdrCompListAsString() {
        return qcf.getHdrCompListAsString();
    }

    public String getHostName() {
        return qcf.getHostName();
    }

    public String getLocalAddress() {
        return qcf.getLocalAddress();
    }

    public boolean getMapNameStyle() {
        return qcf.getMapNameStyle();
    }

    public int getMaxBufferSize() throws JMSException {
        return qcf.getMaxBufferSize();
    }

    public int getMessageRetention() throws JMSException {
        return qcf.getMessageRetention();
    }

    public int getMessageSelection() throws JMSException {
        return qcf.getMessageSelection();
    }

    public int getMQConnectionOptions() {
        return qcf.getMQConnectionOptions();
    }

    public int getMsgBatchSize() {
        return qcf.getMsgBatchSize();
    }

    public Collection getMsgCompList() {
        return qcf.getMsgCompList();
    }

    public String getMsgCompListAsString() {
        return qcf.getMsgCompListAsString();
    }

    public int getMulticast() throws JMSException {
        return qcf.getMulticast();
    }

    @Deprecated
    public boolean getOptimisticPublication() throws JMSException {
        return qcf.getOptimisticPublication();
    }

    @Deprecated
    public boolean getOutcomeNotification() throws JMSException {
        return qcf.getOutcomeNotification();
    }

    public int getPollingInterval() {
        return qcf.getPollingInterval();
    }

    public int getPort() {
        return qcf.getPort();
    }

    @Deprecated
    public int getProcessDuration() throws JMSException {
        return qcf.getProcessDuration();
    }

    public String getProviderVersion() throws JMSException {
        return qcf.getProviderVersion();
    }

    public String getProxyHostName() throws JMSException {
        return qcf.getProxyHostName();
    }

    public int getProxyPort() throws JMSException {
        return qcf.getProxyPort();
    }

    public int getPubAckInterval() throws JMSException {
        return qcf.getPubAckInterval();
    }

    public String getQueueManager() {
        return qcf.getQueueManager();
    }

    public String getReceiveExit() {
        return qcf.getReceiveExit();
    }

    public String getReceiveExitInit() {
        return qcf.getReceiveExitInit();
    }

    @Deprecated
    public int getReceiveIsolation() throws JMSException {
        return qcf.getReceiveIsolation();
    }

    public int getRescanInterval() {
        return qcf.getRescanInterval();
    }

    public String getSecurityExit() {
        return qcf.getSecurityExit();
    }

    public String getSecurityExitInit() {
        return qcf.getSecurityExitInit();
    }

    public int getSendCheckCount() throws JMSException {
        return qcf.getSendCheckCount();
    }

    public String getSendExit() {
        return qcf.getSendExit();
    }

    public String getSendExitInit() {
        return qcf.getSendExitInit();
    }

    public int getShareConvAllowed() throws JMSException {
        return qcf.getShareConvAllowed();
    }

    public boolean getSparseSubscriptions() throws JMSException {
        return qcf.getSparseSubscriptions();
    }

    public Collection getSSLCertStores() throws JMSException {
        return qcf.getSSLCertStores();
    }

    public String getSSLCertStoresAsString() throws JMSException {
        return qcf.getSSLCertStoresAsString();
    }

    public String getSSLCipherSuite() {
        return qcf.getSSLCipherSuite();
    }

    public boolean getSSLFipsRequired() {
        return qcf.getSSLFipsRequired();
    }

    public String getSSLPeerName() {
        return qcf.getSSLPeerName();
    }

    public int getSSLResetCount() {
        return qcf.getSSLResetCount();
    }

    public Object getSSLSocketFactory() {
        return qcf.getSSLSocketFactory();
    }

    public int getStatusRefreshInterval() throws JMSException {
        return qcf.getStatusRefreshInterval();
    }

    public int getSubscriptionStore() throws JMSException {
        return qcf.getSubscriptionStore();
    }

    public boolean getSyncpointAllGets() {
        return qcf.getSyncpointAllGets();
    }

    public boolean getTargetClientMatching() {
        return qcf.getTargetClientMatching();
    }

    public String getTemporaryModel() throws JMSException {
        return qcf.getTemporaryModel();
    }

    public String getTempTopicPrefix() throws JMSException {
        return qcf.getTempTopicPrefix();
    }

    public String getTempQPrefix() throws JMSException {
        return qcf.getTempQPrefix();
    }

    public int getTransportType() {
        return qcf.getTransportType();
    }

    @Deprecated
    public boolean getUseConnectionPooling() {
        return qcf.getUseConnectionPooling();
    }

    public int getVersion() {
        return qcf.getVersion();
    }

    public int getWildcardFormat() throws JMSException {
        return qcf.getWildcardFormat();
    }

    public String getAppName() {
        return qcf.getAppName();
    }

    public int getAsyncExceptions() {
        return qcf.getAsyncExceptions();
    }

    public void setBrokerCCSubQueue(String queueName) throws JMSException {
        qcf.setBrokerCCSubQueue(queueName);
    }

    public void setBrokerControlQueue(String queueName) throws JMSException {
        qcf.setBrokerControlQueue(queueName);
    }

    public void setBrokerPubQueue(String queueName) throws JMSException {
        qcf.setBrokerPubQueue(queueName);
    }

    public void setBrokerQueueManager(String queueManagerName) throws JMSException {
        qcf.setBrokerQueueManager(queueManagerName);
    }

    public void setBrokerSubQueue(String queueName) throws JMSException {
        qcf.setBrokerSubQueue(queueName);
    }

    public void setBrokerVersion(int version) throws JMSException {
        qcf.setBrokerVersion(version);
    }

    public void setCCDTURL(URL url) {
        qcf.setCCDTURL(url);
    }

    public void setCCSID(int ccsid) throws JMSException {
        qcf.setCCSID(ccsid);
    }

    public void setChannel(String channelName) throws JMSException {
        qcf.setChannel(channelName);
    }

    public void setCleanupInterval(long interval) throws JMSException {
        qcf.setCleanupInterval(interval);
    }

    public void setCleanupLevel(int level) throws JMSException {
        qcf.setCleanupLevel(level);
    }

    public void setClientID(String id) {
        qcf.setClientID(id);
    }

    @Deprecated
    public void setClientId(String id) {
        qcf.setClientId(id);
    }

    public void setCloneSupport(int type) throws JMSException {
        qcf.setCloneSupport(type);
    }

    public void setConnTag(byte[] cTag) {
        qcf.setConnTag(cTag);
    }

    public void setDescription(String desc) {
        qcf.setDescription(desc);
    }

    public void setDirectAuth(int authority) throws JMSException {
        qcf.setDirectAuth(authority);
    }

    public void setFailIfQuiesce(int fiq) throws JMSException {
        qcf.setFailIfQuiesce(fiq);
    }

    public void setHdrCompList(Collection<?> compList) throws JMSException {
        qcf.setHdrCompList(compList);
    }

    public void setHdrCompList(String compList) throws JMSException {
        qcf.setHdrCompList(compList);
    }

    public void setHostName(String hostname) {
        qcf.setHostName(hostname);
    }

    public void setLocalAddress(String address) throws JMSException {
        qcf.setLocalAddress(address);
    }

    public void setMapNameStyle(boolean style) {
        qcf.setMapNameStyle(style);
    }

    public void setMaxBufferSize(int size) throws JMSException {
        qcf.setMaxBufferSize(size);
    }

    public void setMessageRetention(int mRet) throws JMSException {
        qcf.setMessageRetention(mRet);
    }

    public void setMessageSelection(int selection) throws JMSException {
        qcf.setMessageSelection(selection);
    }

    public void setMQConnectionOptions(int cTagOpt) throws JMSException {
        qcf.setMQConnectionOptions(cTagOpt);
    }

    public void setMsgBatchSize(int size) throws JMSException {
        qcf.setMsgBatchSize(size);
    }

    public void setMsgCompList(Collection<?> compList) throws JMSException {
        qcf.setMsgCompList(compList);
    }

    public void setMsgCompList(String compList) throws JMSException {
        qcf.setMsgCompList(compList);
    }

    public void setMulticast(int multicast) throws JMSException {
        qcf.setMulticast(multicast);
    }

    @Deprecated
    public void setOptimisticPublication(boolean newVal) throws JMSException {
        qcf.setOptimisticPublication(newVal);
    }

    @Deprecated
    public void setOutcomeNotification(boolean newVal) throws JMSException {
        qcf.setOutcomeNotification(newVal);
    }

    public void setPollingInterval(int interval) throws JMSException {
        qcf.setPollingInterval(interval);
    }

    public void setPort(int port) throws JMSException {
        qcf.setPort(port);
    }

    @Deprecated
    public void setProcessDuration(int newVal) throws JMSException {
        qcf.setProcessDuration(newVal);
    }

    public void setProviderVersion(String version) throws JMSException {
        qcf.setProviderVersion(version);
    }

    public void setProxyHostName(String hostName) throws JMSException {
        qcf.setProxyHostName(hostName);
    }

    public void setProxyPort(int proxyPort) throws JMSException {
        qcf.setProxyPort(proxyPort);
    }

    public void setPubAckInterval(int interval) throws JMSException {
        qcf.setPubAckInterval(interval);
    }

    public void setQueueManager(String queueManagerName) throws JMSException {
        qcf.setQueueManager(queueManagerName);
    }

    public void setReceiveExit(String receiveExit) {
        qcf.setReceiveExit(receiveExit);
    }

    public void setReceiveExitInit(String data) {
        qcf.setReceiveExitInit(data);
    }

    @Deprecated
    public void setReceiveIsolation(int newVal) throws JMSException {
        qcf.setReceiveIsolation(newVal);
    }

    public void setRescanInterval(int interval) throws JMSException {
        qcf.setRescanInterval(interval);
    }

    public void setSecurityExit(String securityExit) {
        qcf.setSecurityExit(securityExit);
    }

    public void setSecurityExitInit(String data) {
        qcf.setSecurityExitInit(data);
    }

    public void setSendCheckCount(int interval) throws JMSException {
        qcf.setSendCheckCount(interval);
    }

    public void setSendExit(String sendExit) {
        qcf.setSendExit(sendExit);
    }

    public void setSendExitInit(String data) {
        qcf.setSendExitInit(data);
    }

    public void setShareConvAllowed(int shared) throws JMSException {
        qcf.setShareConvAllowed(shared);
    }

    public void setSparseSubscriptions(boolean sparse) throws JMSException {
        qcf.setSparseSubscriptions(sparse);
    }

    public void setSSLCertStores(Collection<?> stores) {
        qcf.setSSLCertStores(stores);
    }

    public void setSSLCertStores(String stores) throws JMSException {
        qcf.setSSLCertStores(stores);
    }

    public void setSSLCipherSuite(String cipherSuite) {
        qcf.setSSLCipherSuite(cipherSuite);
    }

    public void setSSLFipsRequired(boolean required) {
        qcf.setSSLFipsRequired(required);
    }

    public void setSSLPeerName(String peerName) throws JMSException {
        qcf.setSSLPeerName(peerName);
    }

    public void setSSLResetCount(int bytes) throws JMSException {
        qcf.setSSLResetCount(bytes);
    }

    public void setSSLSocketFactory(Object sf) {
        qcf.setSSLSocketFactory(sf);
    }

    public void setStatusRefreshInterval(int interval) throws JMSException {
        qcf.setStatusRefreshInterval(interval);
    }

    public void setSubscriptionStore(int flag) throws JMSException {
        qcf.setSubscriptionStore(flag);
    }

    public void setSyncpointAllGets(boolean flag) {
        qcf.setSyncpointAllGets(flag);
    }

    public void setTargetClientMatching(boolean matchClient) {
        qcf.setTargetClientMatching(matchClient);
    }

    public void setTemporaryModel(String queueName) throws JMSException {
        qcf.setTemporaryModel(queueName);
    }

    public void setTempTopicPrefix(String prefix) throws JMSException {
        qcf.setTempTopicPrefix(prefix);
    }

    public void setTempQPrefix(String newTempQPrefix) throws JMSException {
        qcf.setTempQPrefix(newTempQPrefix);
    }

    public void setTransportType(int type) throws JMSException {
        qcf.setTransportType(type);
    }

    @Deprecated
    public void setUseConnectionPooling(boolean usePooling) {
        qcf.setUseConnectionPooling(usePooling);
    }

    public void setVersion(int version) throws JMSException {
        qcf.setVersion(version);
    }

    public void setWildcardFormat(int format) throws JMSException {
        qcf.setWildcardFormat(format);
    }

    public void setAppName(String name) throws JMSException {
        qcf.setAppName(name);
    }

    public void setAsyncExceptions(int flags) throws JMSException {
        qcf.setAsyncExceptions(flags);
    }

    public int getClientReconnectOptions() throws JMSException {
        return qcf.getClientReconnectOptions();
    }

    public void setClientReconnectOptions(int options) throws JMSException {
        qcf.setClientReconnectOptions(options);
    }

    public String getConnectionNameList() throws JMSException {
        return qcf.getConnectionNameList();
    }

    public void setConnectionNameList(String hosts) throws JMSException {
        qcf.setConnectionNameList(hosts);
    }

    public int getClientReconnectTimeout() throws JMSException {
        return qcf.getClientReconnectTimeout();
    }

    public void setClientReconnectTimeout(int timeout) throws JMSException {
        qcf.setClientReconnectTimeout(timeout);
    }

    @Override
    public JMSContext createContext() {
        return qcf.createContext();
    }

    @Override
    public JMSContext createContext(String userName, String password) {
        return qcf.createContext(userName, password);
    }

    @Override
    public JMSContext createContext(String userName, String password, int sessionMode) {
        return qcf.createContext(userName, password, sessionMode);
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        return qcf.createContext(sessionMode);
    }
}
