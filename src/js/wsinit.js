function wsinit(ur) {
	//console.log('WSAPI initialized as '+ WSAPI)
	// alert('extra for '+ctx+' and '+WSAPI)
	WSAPI.init(ur, "refresh", sub_to_refresh)
//	WSAPI.addListener('newdata', updatelist)
}

function sub_to_refresh() {
//	alert('conn op')
	WSAPI.subscribe('refresh')
}

function refreshList() {
	loadInnerPage('/sharelinks/webbee/', '#Links', '#payload', update_search)
}

function update_search() {
	const node = document.getElementById('search')
	if (node.value)
		search_link(node.value,true)
}

function extra_actions(s, ctx) {
	// location.host
	wsinit('ws'+s+'://'+location.hostname+':'+location.port+ctx)
	const node = document.getElementById('search')
    node.addEventListener('keydown', function onEvent(event) {
	    if (event.key === "Enter") {
		    search_link(node.value,true)
	        return false
	    }
    })
}

function search_link(s,ci) {
	var tab = document.querySelector('#links')
	var mark
	if (!!ci && s)
	    s = s.toLowerCase()
	for (var i = 0, row; row = tab.rows[i]; i++) {
		mark = 0
		if (s)
	       for (var j = 0, col; col = row.cells[j]; j++) {
		       if (col.nodeName === 'TH')
                   break
	           if (ci && col.innerHTML.toLowerCase().includes(s) || col.innerHTML.includes(s) ) {
		              //console.log(col.innerHTML)
                      mark = 0
		              break
	           } else {
		          mark = 1
	           }
	       }  
       if (mark)
           row.style.display = 'none'
       else
          row.style.display = ''
    }
}