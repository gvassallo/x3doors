	var $element = document.getElementById('someUniqueId');
	var debug = false;
	var pick_mode_info;
	var nav_mode_info;
	var ab_info;

    x3dom.runtime.ready = function() {
    };

    x3dom.runtime.noBackendFound = function() {
        alert('Custom no backend present function');
    };

	function init(event) {

        $element.runtime.ready = function() {
            alert('Per element custom .runtime.ready() function.');
        };

        $element.runtime.enterFrame = function() {
        };

		updateAbInfo('Viewpoint');
		updatePickInfo();
		updateNavInfo();
	}

	function updatePickInfo() {
		pick_mode_info = document.getElementById('pick_mode_info');
		pick_mode_info.innerHTML = $element.runtime.pickMode() + '/' + $element.runtime.pickMode({'internal':true});
	}


	function updateNavInfo() {
		nav_mode_info = document.getElementById('nav_mode_info');
		nav_mode_info.innerHTML = $element.runtime.navigationType();
	}

	function updateAbInfo(typeName) {
		var bindable = $element.runtime.getActiveBindable(typeName);
		ab_info = document.getElementById('ab_info');
		ab_info.innerHTML = bindable.tagName + " / " + bindable.getAttribute('description');
	}

	function toggleStats(link) {
		stats = $element.runtime.statistics();
		if (stats) {
			$element.runtime.statistics(false);
			link.innerHTML = 'Show statistics';
		} else {
			$element.runtime.statistics(true);
			link.innerHTML = 'Hide statistics';
		}
	}

	function toggleDebug(link) {
		if (debug) {
			$element.runtime.debug(false);
			link.innerHTML = 'Show debug';
		} else {
			$element.runtime.debug(true);
			link.innerHTML = 'Hide debug';
		}
		debug = !debug
	}

    function dumpTree() {
        if (x3dom.docs) {
            document.getElementById('types').innerHTML = x3dom.docs.getNodeTreeInfo();
        } else {
            alert("Documentation module not loaded")
        }
    }

    function dumpComponents() {
        if (x3dom.docs) {
            document.getElementById('types').innerHTML =x3dom.docs.getComponentInfo();
        } else {
            alert("Documentation module not loaded")

        }
    }


 function classie() {

'use strict';

// class helper functions from bonzo https://github.com/ded/bonzo

function classReg( className ) {
  return new RegExp("(^|\\s+)" + className + "(\\s+|$)");
}

// classList support for class management
// altho to be fair, the api sucks because it won't accept multiple classes at once
var hasClass, addClass, removeClass;

if ( 'classList' in document.documentElement ) {
  hasClass = function( elem, c ) {
    return elem.classList.contains( c );
  };
  addClass = function( elem, c ) {
    elem.classList.add( c );
  };
  removeClass = function( elem, c ) {
    elem.classList.remove( c );
  };
}
else {
  hasClass = function( elem, c ) {
    return classReg( c ).test( elem.className );
  };
  addClass = function( elem, c ) {
    if ( !hasClass( elem, c ) ) {
      elem.className = elem.className + ' ' + c;
    }
  };
  removeClass = function( elem, c ) {
    elem.className = elem.className.replace( classReg( c ), ' ' );
  };
}

function toggleClass( elem, c ) {
  var fn = hasClass( elem, c ) ? removeClass : addClass;
  fn( elem, c );
}

var classie = {
  // full names
  hasClass: hasClass,
  addClass: addClass,
  removeClass: removeClass,
  toggleClass: toggleClass,
  // short names
  has: hasClass,
  add: addClass,
  remove: removeClass,
  toggle: toggleClass
};

// transport
if ( typeof define === 'function' && define.amd ) {
  // AMD
  define( classie );
} else {
  // browser global
  window.classie = classie;
}

}

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
