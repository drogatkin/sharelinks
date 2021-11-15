package com.walletwizz.sharelinks.ux;

import java.util.ArrayList;
import java.util.stream.Stream;
//import java.util.stream.builder;
import java.io.InputStreamReader;
import javax.json.JsonReader;
import javax.json.JsonArray;
import javax.json.Json;

import org.aldan3.util.DataConv;
import org.aldan3.data.DODelegator;
import org.aldan3.model.ProcessException;

import com.beegman.webbee.block.Conversational;
import com.walletwizz.sharelinks.model.link;

import com.walletwizz.sharelinks.model.SharelinksModel;

public class Sync extends Conversational<Stream<link>, DODelegator<link>[], SharelinksModel> {
	
	@Override
	protected Stream<link> readModel() {
		Stream.Builder<link> builder = Stream.builder();
		try( JsonReader jsonReader =  Json.createReader(
				new InputStreamReader(req.getInputStream(), DataConv.ifNull(getEncoding(), "utf-8")))) {
			JsonArray array = jsonReader.readArray();
			
			 for (int i = 0, size = array.size(); i < size; i++)
			    {
			      builder.accept((link)fillPojo(new link(getAppModel()), array.getJsonObject(i)));
			    }
			
		
		} catch(Exception e) {
			
		}
		return builder.build();
	}
	
	@Override
	protected DODelegator<link>[] process(Stream<link> ask) {
		ArrayList<DODelegator<link>> result = new ArrayList<>();
		ask.forEach(l -> {
			DODelegator<link> sdo1 = new DODelegator<>(l);
			log("Link: %s", null, l);
			try {
				if(l.sync_id == 0) { // it seems the item never was synchronized
					l.sync_id = l.id ;
					getAppModel().getDOService().addObject(sdo1, "id");
					log("Added with: %d for %d", null, sdo1.get("id"), l.sync_id);
					//l.name += " from web";
					result.add(sdo1);
				} else {
					l.id = l.sync_id;
					getAppModel().getDOService().updateObjectsLike(new DODelegator<link>(l, null, "", "id"), sdo1);
				}
					
			} catch (ProcessException pe) {
				log("", pe);
			}
		});
		
		return result.toArray(DODelegator[]::new);	
		}

}
