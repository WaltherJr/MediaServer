if (window.trustedTypes && window.trustedTypes.createPolicy && !window.trustedTypes.defaultPolicy) {
    window.trustedTypes.createPolicy('default', {
        createHTML: string => string
        // Optional, only needed for script (url) tags
        //,createScriptURL: string => string
        //,createScript: string => string,
    });
}

var css = document.createElement('style');
css.innerHTML = '#logo-icon { border: 2px solid red !important; } ytd-popup-container, div.video-ads { display: none !important; }';
document.querySelector('head').appendChild(css);
