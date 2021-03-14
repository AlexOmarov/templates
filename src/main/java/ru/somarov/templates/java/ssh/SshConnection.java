package ru.somarov.templates.java.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.util.Properties;


/**
 * Создание SSH - подключения.
 */

class SshConnection {


    private Session session;

    void closeSSH()
    {
        session.disconnect();
    }


    SshConnection() throws JSchException {

        JSch jsch = new JSch();

        session = jsch.getSession(SshConfig.SSH_USER, SshConfig.SSH_REMOTE_SERVER, SshConfig.SSH_REMOTE_PORT);
        session.setPassword(SshConfig.PASSWORD);

        if(new File(SshConfig.KNOWN_HOSTS_PATH).exists()){
            jsch.setKnownHosts(SshConfig.KNOWN_HOSTS_PATH);
        }else{
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        }

        session.connect(); //ssh connection established!
        session.setPortForwardingL(SshConfig.LOCAl_PORT, SshConfig.DATABASE_REMOTE_SERVER, SshConfig.REMOTE_PORT);
        System.out.println("Forwarding executed");
     }





}
