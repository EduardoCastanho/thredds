/* Copyright */
package ucar.nc2.ft2.coverage.grid;

import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateTransform;
import ucar.nc2.dataset.TransformType;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDataset;
import ucar.nc2.dt.GridDataset.Gridset;
import ucar.nc2.dt.GridDatatype;
import ucar.unidata.util.Parameter;

import java.io.IOException;
import java.util.*;

/**
 * Adapt ucar.nc2.dt.GridDataset to ucar.nc2.ft.cover.grid.GridCoverageDatasetIF
 *
 * @author caron
 * @since 5/1/2015
 */
public class DtGridDatasetAdapter implements GridCoverageDatasetIF {
  private GridDataset proxy;
  private List<GridCoverage> grids;

  public DtGridDatasetAdapter(GridDataset proxy) {
    this.proxy = proxy;

    grids = new ArrayList<>();
    for (GridDatatype dtGrid : proxy.getGrids())
      grids.add(new Grid(dtGrid));
  }

  @Override
  public String getName() {
    return proxy.getLocation();
  }

  @Override
  public List<GridCoverage> getGrids() {
    return grids;
  }

  @Override
  public GridCoverage findCoverage(String name) {
    for (GridCoverage grid : grids)
      if (grid.getName().equalsIgnoreCase(name)) return grid;
    return null;
  }

  @Override
  public List<Attribute> getGlobalAttributes() {
    return proxy.getGlobalAttributes();
  }

  @Override
  public List<ucar.nc2.ft2.coverage.grid.GridCoordSys> getCoordSys() {
    List<ucar.nc2.ft2.coverage.grid.GridCoordSys> result = new ArrayList<>();
    for (Gridset gset : proxy.getGridsets()) {
      result.add(new CoordSys(gset.getGeoCoordSystem()));
    }
    return result;
  }

  @Override
  public List<GridCoordTransform> getCoordTransforms() {
    Set<String> transformNames = new HashSet<>();
    List<ucar.nc2.ft2.coverage.grid.GridCoordTransform> result = new ArrayList<>();
    for (Gridset gset : proxy.getGridsets()) {
      ucar.nc2.dt.GridCoordSystem gcs = gset.getGeoCoordSystem();
      for (ucar.nc2.dataset.CoordinateTransform ct : gcs.getCoordinateTransforms())
        if (!transformNames.contains(ct.getName())) {
          result.add(new Transform(ct));
          transformNames.add(ct.getName());
        }
    }
    return result;
  }

  @Override
  public List<GridCoordAxis> getCoordAxes() {
    Set<String> axisNames = new HashSet<>();
    List<ucar.nc2.ft2.coverage.grid.GridCoordAxis> result = new ArrayList<>();
    for (Gridset gset : proxy.getGridsets()) {
      ucar.nc2.dt.GridCoordSystem gcs = gset.getGeoCoordSystem();
      for (ucar.nc2.dataset.CoordinateAxis axis : gcs.getCoordinateAxes())
        if (!axisNames.contains(axis.getFullName())) {
          result.add(new Axis(axis));
          axisNames.add(axis.getFullName());
        }
    }
    return result;
  }

  private class Grid extends GridCoverage {
    ucar.nc2.dt.GridDatatype dtGrid;

    Grid(ucar.nc2.dt.GridDatatype dtGrid) {
      this.dtGrid = dtGrid;
      setName(dtGrid.getName());
      setAtts(dtGrid.getAttributes());
      setDataType(dtGrid.getDataType());
      setCoordSysName(dtGrid.getCoordinateSystem().getName());
      setUnits(dtGrid.getUnitsString());
      setDescription(dtGrid.getDescription());
    }

    @Override
    public Array readData(GridSubset subset) throws IOException {
      int level = -1;
      for (Map.Entry<String, String> entry : subset.getEntries()) {
        switch (entry.getKey()) {
          case "Z":
            double val = Double.parseDouble(entry.getValue());
            level = findZcoordIndexFromValue( val);
            break;
        }
      }
      return dtGrid.readDataSlice(0, 0, 0, level, -1, -1);
    }

    int findZcoordIndexFromValue(double val) {
      GridCoordSystem gcs = dtGrid.getCoordinateSystem();
      CoordinateAxis1D zaxis = gcs.getVerticalAxis();
      return zaxis.findCoordElement(val);
    }
  }

  private class CoordSys extends GridCoordSys {
    ucar.nc2.dt.GridCoordSystem dtCoordSys;

    CoordSys(ucar.nc2.dt.GridCoordSystem dtCoordSys) {
      this.dtCoordSys = dtCoordSys;
      setName(dtCoordSys.getName());
      for (CoordinateTransform ct : dtCoordSys.getCoordinateTransforms())
        addTransformName(ct.getName());
      for (CoordinateAxis axis : dtCoordSys.getCoordinateAxes()) // LOOK should be just the grid axes
        addAxisName(axis.getFullName());
    }
  }

  private class Transform extends GridCoordTransform {
    ucar.nc2.dataset.CoordinateTransform dtCoordTransform;

    Transform(ucar.nc2.dataset.CoordinateTransform dtCoordTransform) {
      super(dtCoordTransform.getName());
      this.dtCoordTransform = dtCoordTransform;
      setIsHoriz(dtCoordTransform.getTransformType() == TransformType.Projection);
      for (Parameter p : dtCoordTransform.getParameters())
        addAttribute(new Attribute(p));
    }
  }

  private class Axis extends GridCoordAxis {
    ucar.nc2.dataset.CoordinateAxis dtCoordAxis;

    Axis(ucar.nc2.dataset.CoordinateAxis dtCoordAxis) {
      this.dtCoordAxis = dtCoordAxis;
      setName(dtCoordAxis.getFullName());
      setDataType(dtCoordAxis.getDataType());
      setAxisType(dtCoordAxis.getAxisType());
      setUnits(dtCoordAxis.getUnitsString());
      setDescription(dtCoordAxis.getDescription());

      setNvalues(dtCoordAxis.getSize());
      if (dtCoordAxis instanceof CoordinateAxis1D) { // LOOK for grid, should always be true
        CoordinateAxis1D axis1D = (CoordinateAxis1D) dtCoordAxis;
        setStartValue(axis1D.getCoordValue(0));
        setEndValue(axis1D.getCoordValue((int)axis1D.getSize()-1));
        setIsRegular(axis1D.isRegular());
      }
    }

    @Override
    public double[] readValues() throws IOException {
      Array data = dtCoordAxis.read();
      setValues((double[])data.get1DJavaArray(double.class));
      return getValues();
    }
  }


}