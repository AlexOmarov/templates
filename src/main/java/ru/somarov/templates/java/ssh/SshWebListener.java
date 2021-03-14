package ru.somarov.templates.java.ssh;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Листенер состояний контекста. При инициализации контекста создает ssh-туннель.
 */

@WebListener
public class SshWebListener implements ServletContextListener {


        private SshConnection connection;



        public SshWebListener()
        {
            super();
        }

        public void contextInitialized(ServletContextEvent arg0)
        {
            System.out.println("Context initialized ... !");
            try
            {
                connection = new SshConnection();
                System.out.println("Context initialized final ... !");
            }
            catch (Throwable e)
            {
                e.printStackTrace(); // error connecting SSH server
            }
        }


        public void contextDestroyed(ServletContextEvent arg0)
        {
            System.out.println("Context destroyed ... !");
            connection.closeSSH(); // disconnect
        }


}
