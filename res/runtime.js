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
