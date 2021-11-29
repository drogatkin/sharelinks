function wsinit(ur) {
	// TODO change parameters to pass context name
	WSAPI.init(ur, "refresh")
//	WSAPI.addListener('newdata', updatelist)
	WSAPI.subscribe('refresh')
}

function refreshList() {
	loadInnerPage('/sharelinks/webbee/', '#Links', '#payload')
}

function extra_actions(s, ctx) {
	// location.host
	wsinit('ws'+s+'://'+location.hostname+':'+location.port+ctx)
}