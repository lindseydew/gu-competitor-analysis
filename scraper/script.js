var system = require('system');

var source = system.args[1];


var providers = {
  guardian: {
    url: 'http://www.theguardian.com/uk?view=mobile',
    extractor: function () {
      var containers = [].slice.call(document.querySelectorAll('.item'));
      return containers.map(function(container) {
        var link = container.querySelector('.item__link');
        var pos = position(container);
        return {
          headline: link.textContent.trim().replace(/\s+/g, ' '),
          url: link.href,
          height: container.clientHeight,
          width: container.clientWidth,
          top: pos.top,
          left: pos.left
        };
      });

      function position(obj) {
        var curleft = 0;
        var curtop = 0;
        if (obj.offsetParent) {
          do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
          } while ((obj = obj.offsetParent));
        }
        return {top: Math.round(curtop), left: Math.round(curleft)};
      }
    }
  },
  nytimes: {
    url: 'http://www.nytimes.com/',
    extractor: function() {
      var containers = [].slice.call(document.querySelectorAll('.story:not(.advertisement)'));
      var timesMinuteContainer = document.querySelector('#timesMinuteContainer .story');
      containers = containers.filter(function(c) {
        return c !== timesMinuteContainer;
      });
      return containers.map(function(container) {
        var link = container.querySelector('.headline a') ||
                   container.querySelector('h2 a') ||
                   container.querySelector('h3 a') ||
                   container.querySelector('h4 a') ||
                   container.querySelector('h5 a') ||
                   container.querySelector('h6 a') ||
                   container.querySelector('a');
        var pos = position(container);
        return {
          headline: link && link.textContent.trim().replace(/\s+/g, ' '),
          url: link && link.href,
          height: container.clientHeight,
          width: container.clientWidth,
          top: pos.top,
          left: pos.left
        };
      });

      function position(obj) {
        var curleft = 0;
        var curtop = 0;
        if (obj.offsetParent) {
          do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
          } while ((obj = obj.offsetParent));
        }
        return {top: Math.round(curtop), left: Math.round(curleft)};
      }
    }
  },
  mailonline: {
    url: 'http://www.dailymail.co.uk/home/index.html',
    extractor: function() {
      var containers = [].slice.call(document.querySelectorAll('.article'));
      return containers.map(function(container) {
        var link = container.querySelector('h2 a');
        var pos = position(container);
        return {
          headline: link && link.textContent.trim().replace(/\s+/g, ' '),
          url: link && link.href,
          height: container.clientHeight,
          width: container.clientWidth,
          top: pos.top,
          left: pos.left
        };
      });

      function position(obj) {
        var curleft = 0;
        var curtop = 0;
        if (obj.offsetParent) {
          do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
          } while ((obj = obj.offsetParent));
        }
        return {top: Math.round(curtop), left: Math.round(curleft)};
      }
    }
  },
  telegraph: {
  }
};

var provider = providers[source];
var url = provider.url;
// console.log("Opening", url, "for", source);

var page = require('webpage').create();
page.viewportSize = {
  width: 1024,
  height: 768
};
page.zoomFactor = 1;
page.open(url, function (status) {
  if (status === 'success') {
    // console.log("Page: ", page.title);
    var storyContainers = page.evaluate(provider.extractor);
    console.log("--output--");
    console.log(JSON.stringify(storyContainers));
  } else {
    console.log("error opening the page");
  }

  //Page is loaded!
  phantom.exit();
});


/*

R2

      var allLinks = [].slice.call(document.querySelectorAll('.link-text'));
      var headlineLinks = allLinks.filter(function(link){
        var m = link.href.match(/https?:\/\/www.theguardian.com\/(.*)/);
        // contains a /
        return m && (m[1].indexOf('/') !== -1);
      });
      var linkContainers = headlineLinks.map(function(link) {
        var container = link;
        // while the only link in the container
        while (container.parentNode.querySelectorAll('.link-text').length === 1) {
          container = container.parentNode;
        }
        return container;
      });
      return document.title;

*/
