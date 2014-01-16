var page = require('webpage').create();
var system = require('system');

var source = system.args[1];


var providers = {
  guardian: {
    url: 'http://www.theguardian.com/uk?view=mobile',
    extractor: function () {
      var containers = [].slice.call(document.querySelectorAll('.item'));
      return containers.map(function(container) {
        var link = container.querySelector('.item__link');
        var o = offsets(container);
        return {
          headline: link.textContent.trim().replace(/\s+/g, ' '),
          url: link.href,
          height: container.clientHeight,
          width: container.clientWidth,
          top: o.top,
          left: o.left
        };
      });

      function offsets(node) {
        if (!node || node === document.documentElement) {
          return {top: 0, left: 0};
        } else {
          var parentOffsets = offsets(node.parentNode);
          return {
            top:  node.offsetTop  + parentOffsets.top,
            left: node.offsetLeft + parentOffsets.left
          };
        }
      }
    }
  },
  nytimes: {
    url: 'http://www.nytimes.com/',
    extractor: function() {
      var containers = [].slice.call(document.querySelectorAll('.story:not(.advertisement)'));
      return containers.map(function(container) {
        // FIXME: don't capture section
        var link = container.querySelector('a');
        var o = offsets(container);
        return {
          headline: link && link.textContent.trim().replace(/\s+/g, ' '),
          url: link && link.href,
          height: container.clientHeight,
          width: container.clientWidth,
          top: o.top,
          left: o.left
        };
      });

      function offsets(node) {
        if (!node || node === document.documentElement) {
          return {top: 0, left: 0};
        } else {
          var parentOffsets = offsets(node.parentNode);
          return {
            top:  node.offsetTop  + parentOffsets.top,
            left: node.offsetLeft + parentOffsets.left
          };
        }
      }
    }
  },
  mailonline: {
    url: 'http://www.dailymail.co.uk/home/index.html',
    extractor: function() {
      var containers = [].slice.call(document.querySelectorAll('.article'));
      return containers.map(function(container) {
        var link = container.querySelector('h2 a');
        var o = offsets(container);
        return {
          headline: link && link.textContent.trim().replace(/\s+/g, ' '),
          url: link && link.href,
          height: container.clientHeight,
          width: container.clientWidth,
          top: o.top,
          left: o.left
        };
      });

      function offsets(node) {
        if (!node || node === document.documentElement) {
          return {top: 0, left: 0};
        } else {
          var parentOffsets = offsets(node.parentNode);
          return {
            top:  node.offsetTop  + parentOffsets.top,
            left: node.offsetLeft + parentOffsets.left
          };
        }
      }
    }
  },
  telegraph: {
  }
};

var provider = providers[source];
var url = provider.url;
// console.log("Opening", url, "for", source);
page.open(url, function (status) {
  if (status === 'success') {
    // console.log("Page: ", page.title);
    var storyContainers = page.evaluate(provider.extractor);
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
