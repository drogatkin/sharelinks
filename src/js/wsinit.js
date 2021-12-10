function wsinit(ur) {
	//console.log('WSAPI initialized as '+ WSAPI)
	// alert('extra for '+ctx+' and '+WSAPI)
	// TODO change parameters to pass context name
	WSAPI.init(ur, "refresh", sub_to_refresh)
//	WSAPI.addListener('newdata', updatelist)
	
}

function sub_to_refresh() {
//	alert('conn op')
	WSAPI.subscribe('refresh')
}

function refreshList() {
	loadInnerPage('/sharelinks/webbee/', '#Links', '#payload')
}

function extra_actions(s, ctx) {
	// location.host
	wsinit('ws'+s+'://'+location.hostname+':'+location.port+ctx)
}