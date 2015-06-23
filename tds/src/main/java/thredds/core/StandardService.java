/* Copyright */
package thredds.core;

import thredds.client.catalog.ServiceType;

/**
 * These are the services that the TDS can do
 *
         <entry key="cdmRemote" value="true"/>
         <entry key="cdmrFeatureGrid" value="true"/>
         <entry key="dap4" value="true"/>
         <entry key="httpServer" value="true"/>
         <entry key="resolver" value="true"/>
         <entry key="netcdfSubsetGrid" value="true"/>
         <entry key="opendap" value="true"/>
         <entry key="wcs" value="true"/>
         <entry key="wms" value="true"/>
         <entry key="iso" value="true"/>
         <entry key="ncml" value="true"/>
         <entry key="uddc" value="true"/>
 *
 * @author caron
 * @since 4/29/2015
 */
public enum StandardService {
  cdmRemote(ServiceType.CdmRemote, "/cdmremote/"),
  cdmrFeatureGrid(ServiceType.CdmrFeature, "/cdmrfeature/grid/"),
  dap4(ServiceType.DAP4, "/dap4/"),
  httpServer(ServiceType.HTTPServer, "/fileServer/"),
  resolver(ServiceType.Resolver, ""),
  netcdfSubsetGrid(ServiceType.NetcdfSubset, "/ncss/grid/"),    // heres a wrinkle
  netcdfSubsetPoint(ServiceType.NetcdfSubset, "/ncss/point/"),
  opendap(ServiceType.OPENDAP, "/dodsC/"),
  wcs(ServiceType.WCS, "/wcs/"),
  wms(ServiceType.WMS, "/wms/"),

  iso(ServiceType.ISO, "/iso/"),
  ncml(ServiceType.NCML, "/ncml/"),
  uddc(ServiceType.UDDC, "/uddc/");

  static public StandardService getStandardServiceIgnoreCase(String typeS) {
    for (StandardService s : values())
      if (s.toString().equalsIgnoreCase(typeS)) return s;
    return null;
  }

  final ServiceType type;
  final String base;

  StandardService(ServiceType type, String base) {
    this.type = type;
    this.base = base;
  }

  public ServiceType getType() {
    return type;
  }

  public String getBase() {
    return base;
  }
}