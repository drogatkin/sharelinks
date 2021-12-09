package com.walletwizz.sharelinks.ux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;
//import java.util.stream.builder;
import java.io.InputStreamReader;
import javax.json.JsonReader;
import javax.json.JsonArray;
import javax.json.Json;

import org.aldan3.util.DataConv;
import org.aldan3.util.Sql;
import org.aldan3.annot.Inject;
import org.aldan3.data.DODelegator;
import org.aldan3.model.ProcessException;

import com.beegman.buzzbee.NotifServ;
import com.beegman.buzzbee.NotifServ.NotifException;
import com.beegman.buzzbee.WebEvent;
import com.beegman.webbee.block.Conversational;
import com.walletwizz.sharelinks.model.link;

import com.walletwizz.sharelinks.model.SharelinksModel;

public class Sync extends Conversational<Stream<link>, DODelegator<link>[], SharelinksModel> {

	@Override
	protected Stream<link> readModel() {
		Stream.Builder<link> builder = Stream.builder();
		try (JsonReader jsonReader = Json
				.createReader(new InputStreamReader(req.getInputStream(), DataConv.ifNull(getEncoding(), "utf-8")))) {
			JsonArray array = jsonReader.readArray();

			for (int i = 0, size = array.size(); i < size; i++) {
				builder.accept((link) fillPojo(new link(getAppModel()), array.getJsonObject(i)));
			}

		} catch (Exception e) {

		}
		return builder.build();
	}

	@Override
	protected DODelegator<link>[] process(Stream<link> ask) {
		ArrayList<DODelegator<link>> result = new ArrayList<>();
		String by = userAgent.length() > "mobile:android:".length() ? userAgent.substring("mobile:android:".length()) : "undefined";
		ask.forEach(l -> {
			DODelegator<link> sdo1 = new DODelegator<>(l);
			l.updated_by = by;
			log("Link: %s", null, l);
			try {
				l.updated_date = new Date();
				if (l.sync_id == 0) { // it seems the item never was synchronized
					l.sync_id = l.id;
					getAppModel().getDOService().addObject(sdo1, "id");
					log("Added with: %d for %d", null, sdo1.get("id"), l.sync_id);
					// l.name += " from web";
					result.add(sdo1);
				} else {
					l.id = l.sync_id;
					getAppModel().getDOService().updateObjectsLike(new DODelegator<link>(l, null, "", "id"), sdo1);
				}
				// notify web we have some changes
				sendNote();
			} catch (ProcessException pe) {
				log("", pe);
			}
		});
		// is there modified since?
		long modifiedSince = req.getDateHeader("If-Modified-Since");

		if (modifiedSince > -1) {
			try {
				Collection<DODelegator<link>> records = getAppModel().getDOService().getObjectsByQuery(
						"select name,description,updated_date,id,updated_by,link,sync_id from link where updated_date > " + Sql.toSqlValue(
								new Date(modifiedSince), getAppModel().getDOService().getInlineDatePattern()) + " and updated_by <> '" + Sql.escapeQuote(by) + "' and updated_by is not null",
						0, -1, () -> new DODelegator<>(new link(getAppModel())));
				log("Interested in %d changes since: %s", null, records != null ? records.size() : -1, new Date(modifiedSince).toString());
				for (DODelegator<link> l : records) {
					link link = l.getPrincipal();
					//link.id = link.sync_id;
					//link.sync_id = link.id;
					link.sync_id = 0;
					result.add(l);
					//log("Added %s", null, l);
				}
			} catch (ProcessException e) {
				log("", e);
			}
		}

		return result.toArray(DODelegator[]::new);
	}

	@Inject
	NotifServ ns;

	void sendNote() {
		ns = SharelinksModel.notifService;
		try {
			if (ns != null) {
				WebEvent we;
				ns.publish(we = new WebEvent().setAction("refreshList").setId(getProperties().getProperty(SharelinksModel.NOTIF_CHANNEL))); // it is like an event builder
				log("An update event sent to %s/%s %s", null, ns, getProperties().getProperty(SharelinksModel.NOTIF_CHANNEL), we);
			}
		} catch (NotifException e) {
			log("error in publish %s", e, getProperties().getProperty(SharelinksModel.NOTIF_CHANNEL));
		}
	}

}
