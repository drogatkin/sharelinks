function wsinit(host,port) {
	// TODO change parameters to pass context name
	WSAPI.init('ws://'+host+':'+port+'/sharelinks', "refresh")
//	WSAPI.addListener('newdata', updatelist)
	WSAPI.subscribe('refresh')
}

function refreshList() {
	loadInnerPage('/sharelinks/webbee/', '#Links', '#payload')
}

function extra_actions() {
	// location.host
	wsinit(location.hostname, location.port)
}