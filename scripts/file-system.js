var fs = require('fs');

fs.readFile('IL.json', 'utf8', function (err, data) {
    if (err) {
        console.log('Error: ' + err);
        return;
    }
    //data = JSON.parse(data);

    var cities = JSON.parse(data);
    for (var j in cities) {
        console.log('city:' + cities[j].name + ', latitude: ' + cities[j].primary_latitude + ', longitude: ' + cities[j].primary_longitude);
    }
});
