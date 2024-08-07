// Webbee (C) 2016 Dmitriy Rogatkin
///////   application model class   //////////
// TODO modify the file for the application purpose
package com.walletwizz.sharelinks.model;

import java.util.Properties;

import javax.sql.DataSource;

import org.aldan3.data.DOService;

import com.beegman.buzzbee.NotificationServiceImpl;
import com.beegman.webbee.base.BaseBehavior;
import com.beegman.webbee.model.AppModel;
import com.walletwizz.sharelinks.model.util.SharelinksBehavior;

public class SharelinksModel extends AppModel {

	public static final String NOTIF_CHANNEL = "Channel";
	
	public static NotificationServiceImpl notifService;
	
	@Override
	public String getAppName() {
		return "Sharelinks";
	}
	
	@Override
	protected String getServletName() {
		return "sharelinks servlet";
	}
 
	@Override
	protected DOService createDataService(DataSource datasource) {
		return new DOService(datasource) {

                     @Override      
                     public String normalizeElementName(String name) {
                         return name.toUpperCase();
                     }
                     
                     @Override
		     protected int getInsertUpdateVariant() {
		          return 2;
		     }
                     
                     @Override
                     protected String modifyColumn () {
                 		return " ALTER ";
                 	}

                };
        }

	@Override
        public BaseBehavior getCommonBehavior() {
		return new SharelinksBehavior ();
	}

	@Override
	protected void initServices() {
		super.initServices();
		register(notifService = new NotificationServiceImpl().init(new Properties(), this).start());
		//notifService = getService(NotifServ.class); // for websocket endpoint
		//System.out.printf("init service %s%n", notifService);
	}
	
	@Override
	protected void deactivateServices() {
		super.deactivateServices();
		((NotificationServiceImpl) unregister(getService(notifService.getPreferredServiceName()))).destroy();
		notifService =  null;
	}

}