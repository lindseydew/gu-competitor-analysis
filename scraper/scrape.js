var childProcess = require('child_process');
var phantomjs = require('phantomjs');
var path = require('path');
var http = require('http');
var binPath = phantomjs.path;

var source = 'guardian';
var childArgs = [
  path.join(__dirname, 'script.js'), source
];

console.log("Source: ", source);
childProcess.execFile(binPath, childArgs, function(err, stdout, stderr) {
  var links = stdout;
  console.log("Got " +links.length+ " links, posting...");

  var options = {
    hostname: 'localhost',
    port: 9000,
    path: '/insert/' + source,
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  };
  var post_req = http.request(options, function(res) {
    res.setEncoding('utf8');
    res.on('data', function (chunk) {
      console.log('Response: ' + chunk);
    });
    res.on('end', function() {
      console.log("Sent");
    });
  });
  post_req.write(links);
  post_req.end();
});
