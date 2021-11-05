package com.walletwizz.sharelinks.ux;

import java.util.stream.Stream;
import java.io.InputStreamReader;
import javax.json.JsonReader;
import javax.json.JsonArray;
import javax.json.Json;

import org.aldan3.util.DataConv;

import com.beegman.webbee.block.Conversational;
import com.walletwizz.sharelinks.model.link;
import com.walletwizz.sharelinks.model.SharelinksModel;

public class Sync extends Conversational<Stream<link>, Stream<link>, SharelinksModel> {
	
	protected Stream<link> exchange(Stream<link> ask) {
		return null;	
		}
	
	protected Stream<link> readModel() {
		try( JsonReader jsonReader =  Json.createReader(
				new InputStreamReader(req.getInputStream(), DataConv.ifNull(getEncoding(), "utf-8")))) {
			JsonArray array = jsonReader.readArray();
			link[] links = new link[array.size()];
			
			return Stream.of(links);
		} catch(Exception e) {
			
		}
		return Stream.of();
	}

}
