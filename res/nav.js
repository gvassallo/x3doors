/**
 * The nav stuff
 */
function nav(){
	
	'use strict';

	var body = document.body,
		mask = document.createElement("div"),
        toggleSlideBottom = document.querySelector( ".toggle-slide-bottom" ),
        slideMenuBottom = document.querySelector( ".slide-menu-bottom" ),
        activeNav
	;
	mask.className = "mask";
    /* slide menu bottom */
	toggleSlideBottom.addEventListener( "click", function(){
		classie.add( body, "smb-open" );
		document.body.appendChild(mask);
		activeNav = "smb-open";
	} );
    /* hide active menu if mask is clicked */
	mask.addEventListener( "click", function(){
		classie.remove( body, activeNav );
		activeNav = "";
		document.body.removeChild(mask);
	} );
    /* hide active menu if close menu button is clicked */
	[].slice.call(document.querySelectorAll(".close-menu")).forEach(function(el,i){
		el.addEventListener( "click", function(){
			classie.remove( body, activeNav );
			activeNav = "";
			document.body.removeChild(mask);
		} );
	});


}
