package util.config.reader.elements;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.config.reader.elements.spreadSheet.Service;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:51:28 PM
 * 
 * @author ravij
 * @since
 */
public class SpreadSheetImpl implements SpreadSheet {
    private static final Logger logger = Logger
	    .getLogger(SpreadSheetImpl.class);

    private String sheetName;
    private List<Service> services;

    /**
     * Constructor
     * 
     * @param sheetName
     * @param service
     */
    public SpreadSheetImpl(String sheetName) {
	this.sheetName = sheetName;
	this.services = new ArrayList<Service>();
    }

    /**
     * Constructor
     * 
     * @param sheetName
     * @param service
     */
    public SpreadSheetImpl(String sheetName, Service s) {
	this.sheetName = sheetName;
	this.services = new ArrayList<Service>();
	addService(s);
    }

    @Override
    public void addService(Service s) {
	this.services.add(s);
    }

    /**
     * @param serviceName
     * @return service
     */
    @Override
    public Service getServiceByName(String serviceName) {

	for (Service service : services) {
	    if (service.getName().equals(serviceName)) {
		return service;
	    }
	}
	logger.error("Unable to find service (" + serviceName + ") in sheet ("
		+ this.sheetName + ")");
	return null;
    }

    /**
     * Returns the
     * 
     * @return the services
     */
    @Override
    public List<Service> getServices() {
	return services;
    }

    /**
     * Returns the sheetName
     * 
     * @return the sheetName
     */
    @Override
    public String getSheetName() {
	return sheetName;
    }
}
