var http = require('http');
var fs = require('fs');
var util = require('util');

var 
//states = ['AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA', 'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD', 'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY'],
states = ['IL'],
url = 'http://api.sba.gov/geodata/city_links_for_state_of/{state}.json';
host = 'api.sba.gov';
path = '/geodata/city_links_for_state_of/{state}.json';

for (var i in states) {
    var options = {
        host: host,
        path: path.replace(/\{state\}/, states[i]),
        port: 80,
        method: 'GET'
    };
    console.log(options);
    var req = http.request(options, function(res) {
        console.log('STATUS: ' + res.statusCode);
        //console.log('HEADERS: ' + JSON.stringify(res.headers));
        res.setEncoding('utf8');
        var data = "";
        res.on('data', function (chunk) {
            data += chunk;
        });
        res.on('end', function() {
            var cities = JSON.parse(data);
            (function(e) {
                writeSqlQueryToFile(states[e], cities);
            })(i);
        });
    });

    req.on('error', function(e) {
        console.log('problem with request: ' + e.message);
    });

    req.end();
};

function writeSqlQueryToFile(state, cities) {
    var sql = "";
    var fileName = state + '.sql';
    for (var j in cities) {
        var city = cities[j];
        sql += util.format('INSERT INTO citys (name, latitude, longitude, county, state_abbreviation, state) VALUES (\'%s\', %d, %d, \'%s\', \'%s\', \'%s\');\n', 
                           (city.name).replace(/'/, '\'\''),
                           (city.primary_latitude).replace(/'/, '\'\''),
                           (city.primary_longitude).replace(/'/, '\'\''),
                           (city.county_name).replace(/'/, '\'\''),
                           (city.state_abbreviation).replace(/'/, '\'\''),
                           (city.state_name).replace(/'/, '\'\''));
    }
    
    fs.writeFile(fileName, sql, function(err) {
        if (err) throw err;
        console.log(fileName + ' saved!');
    });
}
