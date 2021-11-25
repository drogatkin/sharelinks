function wsinit(host,port) {
	WSAPI.init('ws://'+host+':'+port+'/sharelinks', "refresh")
//	WSAPI.addListener('newdata', updatelist)
	
}

function refreshList() {
	loadInnerPage('/sharelinks/webbee/', '#Links', '#payload')
}

function extra_actions() {
	// location.host
	wsinit(location.hostname, location.port)
}