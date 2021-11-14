// Webbee (C) 2016 Dmitriy Rogatkin
///////   application common behavior class   //////////
// TODO modify the file for the application behavior purpose
package com.walletwizz.sharelinks.model.util;
import javax.servlet.http.HttpServletRequest;

import com.beegman.webbee.base.BaseBehavior;
import com.walletwizz.sharelinks.model.SharelinksModel;

public class SharelinksBehavior extends BaseBehavior<SharelinksModel> {
    public SharelinksBehavior () {
         super();
               
        isPublic = true;

    }
    
    public boolean isMobileApp(HttpServletRequest req) {
		String userAgent = req.getHeader("user-agent");

		return "mobile:android".equals(userAgent) || "mobile:ios".equals(userAgent)
				|| "mobile:windows".equals(userAgent) || (userAgent != null && userAgent.startsWith("android:"));
	}
}
