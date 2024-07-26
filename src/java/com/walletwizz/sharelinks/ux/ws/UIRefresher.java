package com.walletwizz.sharelinks.ux.ws;

import javax.websocket.CloseReason;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.beegman.buzzbee.LogImpl;
import com.beegman.buzzbee.NotificationService.NotifException;
import com.beegman.buzzbee.WebEvent;
import com.beegman.buzzbee.web.NotifEndPoint;
import com.walletwizz.sharelinks.model.SharelinksModel;

@ServerEndpoint(value = "/notif/web/{servName}", encoders = NotifEndPoint.WebEventEncoder.class, decoders=UIRefresher.ClientMesDecoder.class)
public class UIRefresher extends NotifEndPoint {
	
	public static class ClientMessage {
		public String id;
		public String op;
		public Object data;
		@Override
		public String toString() {			
			return ""+id+"/"+op+"/"+data;
		}
		
	} 
	
	public UIRefresher() {
		ns = SharelinksModel.notifService;
	}

	@OnMessage
	public void fromClient(ClientMessage mes, Session s, @PathParam("servName") String servName) {
		LogImpl.log.debug("got message %s for %s from %s", mes.id, servName, s.getPathParameters());
		ses = s;
		
		if (ns != null)
			try {
				switch(mes.op) {
				case "subscribe":
					if (!notifIds.contains(servName)) {
						ns.subscribe(servName, this);
						notifIds.add(servName);
						LogImpl.log.debug("Subscribed to %s", servName);
					}
					break;
				}
			} catch (NotifException e) {
				LogImpl.log.error(e, "");
			}
	}
	
	@OnClose
	@Override
	public void unsubscribe(@PathParam("servName") String servName, CloseReason reason) {
		super.unsubscribe(servName, reason);
	}


	protected WebEvent build(String topic, JsonObject data) {
		String func = null;
		if (data.containsKey("name"))
			func = data.getString("name");
		ArrayList<String> params = new ArrayList<>();
		if (data.containsKey("params"))
			data.getJsonArray("params").getValuesAs(JsonValue.class).forEach(v -> params.add(v.toString()));
		return new WebEvent().setId(topic).setAction(func).setAttributes(params.toArray());
	}
	
	public static class ClientMesDecoder implements Decoder.Text<ClientMessage> {

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void init(EndpointConfig arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public ClientMessage decode(String arg0) throws DecodeException {
			JsonReader reader = Json.createReader(new StringReader(arg0));
			JsonObject  obj = reader.readObject();
			ClientMessage result = new ClientMessage();
			result.op = obj.getString("op");
			result.id = obj.getString("id");
			if (obj.containsKey("data"))
			result.data = obj.getJsonObject("data");
			return result;
		}

		@Override
		public boolean willDecode(String arg0) {
			return arg0.startsWith("{") || arg0.startsWith("[");
		}	
	}

}
