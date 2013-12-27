package util.config.reader.elements;

import java.util.List;

import util.config.reader.elements.spreadSheet.Service;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:51:28 PM
 * 
 * @author ravij
 * @since
 */
public interface SpreadSheet {

    void addService(Service s);

    Service getServiceByName(String serviceName);

    List<Service> getServices();

    String getSheetName();
}
