package thredds.server.config;

import thredds.catalog.InvCatalog;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
public class TdsCatConfig
{
  private static org.slf4j.Logger log =
          org.slf4j.LoggerFactory.getLogger( TdsCatConfig.class );

  private final TdsContext tdsContext;
  public TdsCatConfig( TdsContext tdsContext)
  {
    this.tdsContext = tdsContext;
  }

  public void init()
  {

  }

  public void reinit()
  {

  }

  public void reinitPartial( String catalogPath )
  {

  }

  public InvCatalog getCatalog( String catalogPath )
  {
    return null;
  }

}
