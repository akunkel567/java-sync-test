/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Monitor.java
 *
 * Created on 11.03.2009, 11:27:29
 */
package de.complex.clxproductsync.monitor.client;

import de.complex.tools.config.ApplicationConfig;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Timer;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author kunkel
 */
public class Monitor extends javax.swing.JFrame implements ActionListener {

	private static Logger logger = Logger.getLogger(Monitor.class);
	WsServerService ws = null;
	JClxWebSyncWebServices port = null;

	/** Creates new form Monitor */
	public Monitor() {
		DOMConfigurator.configureAndWatch("conf/log4j.xml", 60 * 1000);

		if (!ApplicationConfig.loadConfig("conf/monitor.conf")) {
			Monitor.logger.fatal("Config Error...!");
			System.err.println("Config Error...!");
			System.exit(1);
		}

		String ws_url = ApplicationConfig.getValue("ws_url");
		String ws_port = ApplicationConfig.getValue("ws_port");

		URL url = null;
		try {
			url = new URL("http://" + ws_url + ":" + ws_port + "/services?wsdl");
			Monitor.logger.info("Webservice Url: " + url);
			ws = new WsServerService(url, new QName("http://server.monitor.jclxwebsync.complex.de/", "WsServerService"));
		} catch (MalformedURLException ex) {
			Monitor.logger.fatal("Url: " + url, ex);
			System.err.println("FatalError: Url: " + url);
			System.exit(1);
		}

		port = ws.getJClxWebSyncWebServicesPort();

		initComponents();

		jTextField1.setText(port.getStatusText());
		this.setButtonText(port.getStatus());

		Timer t = new Timer(10000, this);
		t.start();
	}

	private void togglePause() {
		jButton1.setEnabled(false);
		boolean pause = port.getStatus();
		Monitor.logger.info("setPause: " + pause);

		port.setPause(new Holder(!pause));

		this.setButtonText(!pause);
		this.setStatusText();

		jButton1.setEnabled(true);
	}

	private void setStatusText() {
		jTextField1.setText(port.getStatusText());
	}

	private void checkStatus() {
		setButtonText(port.getStatus());
		this.setStatusText();
	}

	private void setButtonText(boolean status){
		if(status){
			jButton1.setText("Start");
		} else {
			jButton1.setText("Pause");
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jTextField1 = new javax.swing.JTextField();
      jButton1 = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("jClxWebSync - Monitor");

      jTextField1.setText("jTextField1");

      jButton1.setText("Start");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
         }
      });
      jButton1.addAncestorListener(new javax.swing.event.AncestorListener() {
         public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
         }
         public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            jButton1AncestorAdded(evt);
         }
         public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addGap(33, 33, 33)
                  .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(layout.createSequentialGroup()
                  .addGap(61, 61, 61)
                  .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(43, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGap(29, 29, 29)
            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(21, Short.MAX_VALUE))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		this.togglePause();
		this.setStatusText();
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jButton1AncestorAdded
		// TODO add your handling code here:
	}//GEN-LAST:event_jButton1AncestorAdded

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new Monitor().setVisible(true);
			}
		});
	}

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JTextField jTextField1;
   // End of variables declaration//GEN-END:variables

	public void actionPerformed(ActionEvent e) {
		this.checkStatus();

	}
}