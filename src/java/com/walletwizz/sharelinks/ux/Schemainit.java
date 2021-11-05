package com.walletwizz.sharelinks.ux;

import com.beegman.webbee.block.Systemsetup;
import com.beegman.webbee.model.Setup;
import com.walletwizz.sharelinks.model.SharelinksModel;

public class Schemainit extends Systemsetup<Setup, SharelinksModel> {

	@Override
	protected String getDefaultModelPackage() {
             return getAppModel().getClass().getPackage().getName();
        }
}
