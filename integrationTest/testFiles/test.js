const newman = require('newman');

let errorFlag = false;
newman.run({
    collection: require('./collection.json'),
    reporters: 'cli',
    iterationCount: 2,
    bail: true,
    abortOnFailure: true
}, function (err, summary) {
        if(summary.run.failures.length > 0){
            console.log("Collection run failed!");
            process.exit(-1);
        }
        else{
            console.log('collection run complete! All test passed.');
        }
    });
