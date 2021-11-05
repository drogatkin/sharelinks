package com.walletwizz.sharelinks.ux;

import org.aldan3.model.ProcessException;
import org.aldan3.data.DODelegator;
import org.aldan3.model.DataObject;
import org.aldan3.annot.DataRelation;
import com.beegman.webbee.block.SqlTabular;

import com.walletwizz.sharelinks.model.SharelinksModel;
@DataRelation(query="select id,name,link,description from link")
public class Links extends SqlTabular<DataObject,SharelinksModel> {
}
