package de.complex.clxproductsync.soap;

import de.complex.clxproductsync.soap.datacheck.DatacheckPort;
import de.complex.clxproductsync.soap.datacheck.DatacheckService;
import de.complex.clxproductsync.soap.transfer.TransferPort;
import de.complex.clxproductsync.soap.transfer.TransferService;
import de.complex.tools.config.ApplicationConfig;
import jakarta.xml.ws.BindingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PortFactory {

    private static final Logger LOG = LogManager.getLogger(PortFactory.class);

    public void setUrl(BindingProvider bp, PortPath endpointPath) {
        String endpointURL = ApplicationConfig.getValue("ws_url") + "/" + endpointPath.getUrlPath();
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
    }

    public void showWsdl(String path) throws RuntimeException {

        InputStream instr = PortFactory.class.getClassLoader().getResourceAsStream(path);

        // reading the files with buffered reader
        InputStreamReader strrd = new InputStreamReader(instr);

        BufferedReader rr = new BufferedReader(strrd);

        String line;

        // outputting each line of the file.
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if (!((line = rr.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sb.append(line).append("\n");
        }
        LOG.debug("Wsdl from {} \n {}", path, sb.toString());
    }

    public TransferPort getTransferPort() {
        PortPath path = PortPath.TRANSFER;
        TransferService service = new TransferService();
        TransferPort port = service.getTransferPort();
        setUrl((BindingProvider) port, path);
        return port;
    }

    public DatacheckPort getDatacheckPort() {
        PortPath path = PortPath.DATACHECK;
        DatacheckService service = new DatacheckService();
        DatacheckPort port = service.getDatacheckPort();
        setUrl((BindingProvider) port, path);
        return port;
    }

}
