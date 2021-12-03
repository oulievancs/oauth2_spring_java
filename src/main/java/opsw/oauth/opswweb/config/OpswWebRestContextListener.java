/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opsw.oauth.opswweb.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import opsw.core.logging.OpswLogger;
import opsw.web.OpswWebUtils;

/**
 *
 * @author e.oulis
 */
public class OpswWebRestContextListener implements ServletContextListener
{

  @Override
  public void contextInitialized(ServletContextEvent sce)
  {
    /* g.kabardis 15/5/2019 
    για να κανουμε τεστ τα url 
ΤΟ OPSWWEBREST  που τρεχει στον  glassfish.oss.gr ΔΕΝ  ΜΠΟΡΟΥΜΕ ΝΑ ΤΟΝ ΧΤΥΠΗΣΟΥΜΕ ΑΠΟ ΤΟ ΕΣΩΤΕΡΙΚΟ ΔΙΚΤΙΟ
ΠΡΕΠΕΙ ΝΑ ΜΠΟΥΜΕ στοn  Mobileserver.oss.gr
http://glassfish.oss.gr/OPSWWEBREST/opsw/erp/apotmob/web/restap/syncFilesAll/v1/PSTATION1/?maxitemsread=5
η  να μπουμε με radmin sto  10.2.1.2
     kai na to xtypisoyme apo ton chrome 
http://10.2.1.2:8080/OPSWWEBREST/opsw/erp/gymn/web/restap/gyplana/v1?date=2018-11-06&cmacco=68082&booksw=my
     */

    //--- Sakis 20-Nov-2018. Άλλαξα αυτό με το παρκάτω. ---//
    //Logger log = Logger.getLogger("opsw.apps.web.OpswWebRest.logger");
    //OpswLogger.setLogger(log);
    OpswWebUtils.LoggerWebCreate(sce.getServletContext(), "opsw.apps.web.OpswOAuth.logger");
    //-----------------------------------------------------//

    OpswLogger.getLogger().debug(" OpswOAuthstart  -1");
    
    try
    {
      //OpswJpa.OpswEMFCreate(OpswJpa.JTA_DATASOURCE_OPSW_BASE);

      //LcSystemGen.findAndSetPdfFontsFolder(OpswWebUtils.findAndSetPdfFontsFolder(sce.getServletContext())); // 08-Aug-2019 r.vlachou
    }
    catch (Exception e)
    {
      OpswLogger.getLogger().error(e.getMessage(), e);
    }
    
    OpswLogger.getLogger().debug(" OpswOAuthstart  -2");

  }

  @Override
  public void contextDestroyed(ServletContextEvent sce)
  {
    //--- ENTITY MANAGER -----------------------------------------------------//
    //OpswJpa.OPSWEMFClose(OpswJpa.JTA_DATASOURCE_MINLO);
    
    //OpswJpa.OPSWEMFCloseAll();
  }
}
