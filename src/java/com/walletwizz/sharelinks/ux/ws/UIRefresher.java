package com.walletwizz.sharelinks.ux.ws;

import javax.websocket.server.ServerEndpoint;

import com.beegman.buzzbee.web.NotifEndPoint;
import com.walletwizz.sharelinks.model.SharelinksModel;

@ServerEndpoint(value = "/notif/web/{servName}", encoders = NotifEndPoint.WebEventEncoder.class)
public class UIRefresher extends NotifEndPoint {
	public UIRefresher() {
		ns = SharelinksModel.notifService;
	}

}
