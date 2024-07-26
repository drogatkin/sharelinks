package com.walletwizz.sharelinks.model;

import java.util.Date;

import org.aldan3.annot.DBField;
import org.aldan3.annot.DataRelation;
import org.aldan3.annot.FormField;
import org.aldan3.annot.FormField.FieldType;

import com.beegman.webbee.util.SimpleCoordinator;
// TODO insert it conditionally when drop downs requested
import com.beegman.webbee.util.GenericResourceOptions;

@DataRelation
public class link extends SimpleCoordinator<SharelinksModel> {
	public link(SharelinksModel m) {
		super(m);
	}

	@DBField(key = true, auto = 1)
	@FormField(presentType = FieldType.Hidden)
	public long id;
	
	@DBField(index = true)
	@FormField(/*formFieldName="global_id"*/)
	public long sync_id;

	@DBField(size = 200, index=true)
	@FormField()
	public String name;
	
	@DBField(size = 508,unique=true,type="character")
	//@FormField(formFieldName="url")
	@FormField
	public String link;

	@DBField(size = 4000)
	@FormField(presentSize = 68, presentRows = 6)
	public String description;

	@DBField(index=true)
	public Date updated_date;
	
	@DBField(size=160, index=true)
	public String  updated_by;
	
	@DBField
	public Date end_date;

	@Override
	public String toString() {
		return "link [id=" + id + ", sync_id=" + sync_id + ", name=" + name + ", link=" + link + ", description="
				+ description + ", updated_date=" + updated_date + " / " + updated_by + ", end_date=" + end_date + "]";
	}

}
         