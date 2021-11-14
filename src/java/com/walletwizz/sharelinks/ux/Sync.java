package com.walletwizz.sharelinks.ux;

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

public class Sync extends Conversational<Stream<link>, Stream<link>, SharelinksModel> {
	
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
	protected Stream<link> process(Stream<link> ask) {
		ask.forEach(l -> {
			DODelegator<link> sdo1 = new DODelegator<>(l);
			log("Link: %s", null, l);
			try {
				if(l.id == 0) {
					getAppModel().getDOService().addObject(sdo1);
				}
			} catch (ProcessException pe) {
				log("", pe);
			}
		});
		
		return (Stream.<link>builder()).build();	
		}

}
