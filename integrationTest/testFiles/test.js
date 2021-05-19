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
        errorFlag = true;
}
    if(!errorFlag){
        console.log('collection run complete! All test passed.');
    } else{
        console.log("Collection run failed!");
        process.exit(-1);
    }
});
