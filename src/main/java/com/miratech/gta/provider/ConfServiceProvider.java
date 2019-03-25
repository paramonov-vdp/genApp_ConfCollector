package com.miratech.gta.provider;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.miratech.gta.model.wrappers.CustomException;

import java.util.EventObject;

/**
 * Class provide enpoint connection for collectors
 */
public class
ConfServiceProvider implements ChannelListener {

    private String host;
    private int port;
    private String userName;
    private String password;
    private Endpoint endpoint;
    private ConfServerProtocol protocol;
    private IConfService confService;

    /**
     * Creating new provider with parameters, given from environment.
     *
     * @param host     - host, where environment working
     * @param port     - port of main component, in genesys - configuration server
     * @param userName username to access main component
     * @param password - password to access main component.
     */
    public ConfServiceProvider(String host, int port, String userName, String password) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Method trying to initiate connection by parameters, given in constructor of provider.
     */
    public void initConnection() {
        endpoint = new Endpoint(host, port);
        endpoint.getConfiguration().setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, "UTF-8");
        protocol = new ConfServerProtocol(endpoint);
        confService = ConfServiceFactory.createConfService(protocol);
        protocol.setClientName("default");
        protocol.setClientApplicationType(CfgAppType.CFGSCE.ordinal());

        protocol.setUserName(userName);

        protocol.setUserPassword(password);
        protocol.addChannelListener(this);
        try {
            protocol.open();
        } catch (Exception e) {
            try {
                throw new CustomException("Something wrong with connection");
            } catch (CustomException e1) {
                e1.getMessage();
            }
        }
    }

    /**
     * Method return opened connection to endpoint.
     *
     * @return connection interface.
     */
    public IConfService getConfService() throws ProtocolException, InterruptedException {
        return confService;
    }

    /**
     * Signalize in console about opening channel.
     */
    public void onChannelOpened(final EventObject eventObject) {
        System.out.println(eventObject);
    }

    /**
     * Signalize in console about closed channel.
     */
    public void onChannelClosed(final ChannelClosedEvent channelClosedEvent) {
        System.out.println(channelClosedEvent);
    }

    /**
     * Signalize in console about error in channel
     */
    public void onChannelError(final ChannelErrorEvent channelErrorEvent) {
        System.out.println("Errors in channel");
    }

}
